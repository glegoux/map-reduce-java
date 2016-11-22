package com.mapreduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

public class Main {

  public static void main(String[] args) throws InterruptedException {

    if (args.length == 0) {
      System.err.println("Missing argument");
      System.exit(1);
    }

    String mode = args[0];

    boolean isRemote = false;
    if (Objects.equal(mode, "local")) {
      isRemote = false;
    } else if (Objects.equal(mode, "remote")) {
      isRemote = true;
    } else {
      System.err.println("Wrong argument");
      System.exit(1);
    }

    // Split input to make all Sx files
    String filename = args[1];
    filename = Cleaner.clean(filename);
    int numberOfChunks = Splitter.lineByLine(filename);
    
    // Init
    int numberOfSlaves = numberOfChunks;
    Slave[] slaves = new Slave[numberOfSlaves];

    // Map
    for (int chunkNumber = 1; chunkNumber <= numberOfChunks; chunkNumber++) {
      slaves[chunkNumber - 1] = new Slave(isRemote,
                                          "thread" + chunkNumber,
                                          "slave" + chunkNumber,
                                          "modeSXUMX",
                                          String.valueOf(chunkNumber));
      slaves[chunkNumber - 1].start();
    }


    // Wait for each treatment
    for (int i = 0; i < numberOfChunks; i++) {
      slaves[i].join();
    }

    // Make dictionnary
    Map<Integer, String[]> umxMap = new HashMap<>();
    for (Slave slave : slaves) {
      if (slave == null) {
        continue;
      }
      int umx = Integer.parseInt(slave.args[1]);
      String[] words = slave.result.stdout.split("\\n");
      umxMap.put(umx, words);
    }

    // Make inversed dictionnary
    Map<String, List<String>> wordMap = Reverser.umxMap(umxMap);
    int numberOfWords = wordMap.size();
    
    numberOfSlaves = numberOfWords;
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
      slaves[wordNumber - 1] = new Slave(isRemote,
                                         "thread" + wordNumber,
                                         "slave" + wordNumber,
                                         slaveArgs);
      slaves[wordNumber - 1].start();
      wordNumber++;
    }

    // Wait for each treatment
    for (int i = 0; i < numberOfWords; i++) {
      slaves[i].join();
    }
    
    // Merge all RMx files in a single file output
    String outputPathname = Aggregator.assemble(numberOfWords);
    
    // Show result
    Displayer.result(outputPathname);

  }

}
