package com.mapreduce.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import com.mapreduce.config.Config;

public class Aggregator {

  public static String assemble(int numberOfwords) {

    String outputPathname = Paths.get(Config.SHARED_DIRECTORY_LOCATION , "output").toString();
    FileWriter fstream;
    BufferedWriter out = null;
    File output = new File(outputPathname);
    try {
      fstream = new FileWriter(output, true);
      out = new BufferedWriter(fstream);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    for (int wordNumber = 1; wordNumber <= numberOfwords; wordNumber++) {
      FileInputStream fis;
      try {
        fis = new FileInputStream(new File(Paths.get(Config.SHARED_DIRECTORY_LOCATION, "RM" + wordNumber).toString()));
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        String aLine;
        while ((aLine = in.readLine()) != null) {
          out.write(aLine);
          out.newLine();
        }

        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return outputPathname;

  }

}
