package com.mapreduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.mapreduce.profiler.Profiler;
import com.mapreduce.utils.Aggregator;
import com.mapreduce.utils.Cleaner;
import com.mapreduce.utils.Displayer;
import com.mapreduce.utils.Reverser;
import com.mapreduce.utils.Splitter;

public class Main {

  public static Slave[] slaves;
  public static int numberOfChunks;
  public static boolean isRemote;
  public static int numberOfWords;
  public static String filename;
  public static Map<Integer, String[]> umxMap = new HashMap<>();
  public static Map<String, List<String>> wordMap = new HashMap<>();

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
    
    init(args[1]);
    splitting();
    mappping();
    shuffling();
    reducing();
    assembling();

  }

  @Profiler(name = "Init")
  public static void init(String filename) {
    Main.filename = Cleaner.clean(filename);
    int numberOfSlaves = numberOfChunks;
    slaves = new Slave[numberOfSlaves];
  }

  @Profiler(name = "Splitting")
  public static void splitting() {
    // Split input to make all Sx files
    numberOfChunks = Splitter.lineByLine(filename);
    slaves = new Slave[numberOfChunks];
  }

  @Profiler(name = "Mapping")
  public static void mappping() throws InterruptedException {
    // Map
    for (int chunkNumber = 1; chunkNumber <= numberOfChunks; chunkNumber++) {
      slaves[chunkNumber - 1] =
          new Slave(isRemote, "thread" + chunkNumber, "slave" + chunkNumber, "modeSXUMX",
              String.valueOf(chunkNumber));
      slaves[chunkNumber - 1].start();
    }

    // Wait for each treatment
    for (int i = 0; i < numberOfChunks; i++) {
      slaves[i].join();
    }

    // Make dictionnary
    for (Slave slave : slaves) {
      if (slave == null) {
        continue;
      }
      int umx = Integer.parseInt(slave.args[1]);
      String[] words = slave.result.stdout.split("\\n");
      umxMap.put(umx, words);
    }
  }

  @Profiler(name = "Shuffling")
  public static void shuffling() {
    // Make inversed dictionnary
    wordMap = Reverser.umxMap(umxMap);
    numberOfWords = wordMap.size();
  }

  @Profiler(name = "Reducing")
  public static void reducing() throws InterruptedException {
    int numberOfSlaves = numberOfWords;
    slaves = new Slave[numberOfSlaves];
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
      slaves[wordNumber - 1] =
          new Slave(isRemote, "thread" + wordNumber, "slave" + wordNumber, slaveArgs);
      slaves[wordNumber - 1].start();
      wordNumber++;
    }

    // Wait for each treatment
    for (int i = 0; i < numberOfWords; i++) {
      slaves[i].join();
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
