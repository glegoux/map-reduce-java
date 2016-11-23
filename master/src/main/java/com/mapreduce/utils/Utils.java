package com.mapreduce.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static int countFileLines(String filename) {
    return readFile(filename).size();
  }


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


  public static void writeFile(String filename, List<String> lines) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(filename, "UTF-8");
      for (String line : lines) {
        writer.println(line);
      }
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      System.out.println("Error during the writing of " + filename);
      System.exit(1);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

}
