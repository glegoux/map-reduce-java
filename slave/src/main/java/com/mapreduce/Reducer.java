package com.mapreduce;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Reducer {

  public static int merge(String word, String... umxs) {
    List<String> words = new LinkedList<>();
    for (String umx : umxs) {
      words.addAll(Utils.readFile(Utils.getPath(umx)));
    }
    return Collections.frequency(words, word);
  }

  public static void persistSMX(String smx, String word, int frequency) throws IOException {
    File f = new File(Utils.getPath(smx));
    if (!f.exists()) {
      f.createNewFile();
    }
    FileWriter fw = new FileWriter(f.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    for (int i = 0; i < frequency; i++) {
      bw.write(word + System.getProperty("line.separator"));
    }
    bw.close();
  }

  public static void persistRMX(String rmx, String word, int frequency) throws IOException {
    File f = new File(Utils.getPath(rmx));
    if (!f.exists()) {
      f.createNewFile();
    }
    FileWriter fw = new FileWriter(f.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    String result = word.trim() + "," + frequency;
    System.out.println(result);
    bw.write(result + System.getProperty("line.separator"));
    bw.close();
  }

}
