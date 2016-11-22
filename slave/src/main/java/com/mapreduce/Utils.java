package com.mapreduce;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static ArrayList<String> readFile(String filename) {
    List<String> lines = null;
    try {
      lines = Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8"));
    } catch (IOException e) {
      System.out.println("Error during the reading of " + filename);
      System.exit(1);
    }
    return (ArrayList<String>) lines;
  }

}
