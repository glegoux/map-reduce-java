package com.mapreduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IPReader {
  
  public static String IP_FILENAME = "/com/mapreduce/network/ip.txt";

  public static List<String> readFile(String filename) {

    List<String> lines = new ArrayList<>();
    BufferedReader br = null;

    try {
      InputStream is = IPReader.class.getResourceAsStream(filename);
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader reader = new BufferedReader(isr);
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return lines;
  }

}
