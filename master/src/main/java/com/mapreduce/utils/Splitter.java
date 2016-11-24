package com.mapreduce.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.mapreduce.config.Config;

public class Splitter {

  public static int lineByLine(String filename) {
    int chunkNumber = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      for (String line; (line = br.readLine()) != null;) {
        File f = new File(Paths.get(Config.SHARED_DIRECTORY_LOCATION, "S" + chunkNumber).toString());
        if (!f.exists()) {
          f.createNewFile();
        }
        FileWriter fw = new FileWriter(f.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line + System.getProperty("line.separator"));
        bw.close();
        chunkNumber++;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return 0;
    }
    return chunkNumber - 1;
  }


}
