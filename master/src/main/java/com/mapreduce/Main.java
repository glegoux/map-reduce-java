package com.mapreduce;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.Objects;
import com.mapreduce.config.Config;
import com.mapreduce.network.Cluster;
import com.mapreduce.network.SlaveHelper;
import com.mapreduce.network.SxUmxJob;
import com.mapreduce.network.UmxRmxJob;
import com.mapreduce.profiler.Profiler;
import com.mapreduce.utils.Aggregator;
import com.mapreduce.utils.Cleaner;
import com.mapreduce.utils.Displayer;
import com.mapreduce.utils.Reverser;
import com.mapreduce.utils.Splitter;

public class Main {

  public static Cluster cluster;
  public static int numberOfChunks;
  public static int numberOfSlaves;
  public static ExecutorService executorService;
  public static boolean isRemote;
  public static int numberOfWords;
  public static String filename;
  public static Map<Integer, String[]> umxMap = new ConcurrentHashMap<>();
  public static Map<String, List<String>> wordMap = new ConcurrentHashMap<>();

  @Profiler(name = "Total")
  public static void main(String[] args) throws InterruptedException {

    if (args.length == 0) {
      System.err.println("Missing argument");
      System.exit(1);
    }

    String mode = args[0];

    isRemote = false;
    if (Objects.equal(mode, "local")) {
      isRemote = false;
    } else if (Objects.equal(mode, "remote")) {
      isRemote = true;
    } else {
      System.err.println("Wrong argument");
      System.exit(1);
    }

    String filename = args[1];

    init(filename);
    splitting();
    mappping();
    shuffling();
    reducing();
    assembling();

  }

  @Profiler(name = "Init")
  public static void init(String filename) {
    Main.filename = Cleaner.clean(filename);
    if (isRemote) {
      cluster = new Cluster();
      numberOfSlaves = cluster.slaveNames.size();
    }
  }

  @Profiler(name = "Splitting")
  public static void splitting() {
    // Split input to make all Sx files
    numberOfChunks = Splitter.lineByLine(filename);
  }

  @Profiler(name = "Mapping")
  public static void mappping() throws InterruptedException {
    executorService = Executors.newFixedThreadPool(Config.THREAD_NUMBER);
    // Map
    for (int chunkNumber = 1; chunkNumber <= numberOfChunks; chunkNumber++) {
      String slaveName = "slave" + chunkNumber;
      if (isRemote) {
        slaveName =
            cluster.slaveNames.get(SlaveHelper.chooseSlaveIndex(chunkNumber - 1, numberOfSlaves));
      }
      SxUmxJob slaveJob =
          new SxUmxJob(isRemote, umxMap, "thread" + chunkNumber, slaveName, "modeSXUMX",
              String.valueOf(chunkNumber));
      executorService.execute(slaveJob);
    }
    executorService.shutdown();

    // Wait for each treatment
    while (!executorService.isTerminated()) {
    }

  }

  @Profiler(name = "Shuffling")
  public static void shuffling() {
    // Make reversed dictionary
    wordMap = Reverser.umxMap(umxMap);
    numberOfWords = wordMap.size();
  }

  @Profiler(name = "Reducing")
  public static void reducing() throws InterruptedException {
    executorService = Executors.newFixedThreadPool(Config.THREAD_NUMBER);
    // Reduce
    int wordNumber = 1;
    for (Map.Entry<String, List<String>> entry : wordMap.entrySet()) {
      String slaveMode = "modeUMXSMX";
      String word = entry.getKey();
      String smxNumber = String.valueOf(wordNumber);
      String[] umxs = (String[]) entry.getValue().toArray(new String[entry.getValue().size()]);
      String[] slaveArgs = new String[3 + umxs.length];
      slaveArgs[0] = slaveMode;
      slaveArgs[1] = word;
      slaveArgs[2] = smxNumber;
      System.arraycopy(umxs, 0, slaveArgs, 3, umxs.length);
      String slaveName = "slave" + wordNumber;
      if (isRemote) {
        slaveName =
            cluster.slaveNames.get(SlaveHelper.chooseSlaveIndex(wordNumber - 1, numberOfSlaves));
      }
      UmxRmxJob slaveJob = new UmxRmxJob(isRemote, "thread" + wordNumber, slaveName, slaveArgs);
      executorService.execute(slaveJob);
      wordNumber++;
    }
    executorService.shutdown();

    // Wait for each treatment
    while (!executorService.isTerminated()) {
    }

  }

  @Profiler(name = "Assembling")
  public static void assembling() {
    // Merge all RMx files in a single file output
    String outputPathname = Aggregator.assemble(numberOfWords);

    // Show result
    Displayer.result(outputPathname);
  }

}
