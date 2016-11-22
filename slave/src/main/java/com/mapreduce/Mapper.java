package com.mapreduce;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mapper {

  public static List<String> split(String sx) {
    ArrayList<String> lines = Utils.readFile(Config.SHARED_DIRECTORY_LOCATION + sx);
    List<String> words = new ArrayList<>();
    for (String line : lines) {
      for (String word : line.split("\\W+")) {
        words.add(word);
      }
    }
    return words;
  }

  public static void persist(String umx, List<String> words) throws IOException {
    File f = new File(Config.SHARED_DIRECTORY_LOCATION + umx);
    if (!f.exists()) {
      f.createNewFile();
    }
    FileWriter fw = new FileWriter(f.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    Set<String> keySet = new HashSet<>();
    for (String word : words) {
      if (!keySet.contains(word)) {
        keySet.add(word);
        System.out.println(word);
      }
      bw.write(word + System.getProperty("line.separator"));
    }
    bw.close();
  }

}
