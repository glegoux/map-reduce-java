package com.mapreduce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Aggregator {

  public static void assemble(int numberOfwords) {
    
    FileWriter fstream;
    BufferedWriter out = null;
    File output = new File(Config.SHARED_DIRECTORY_LOCATION + "output");
    try {
      fstream = new FileWriter(output, true);
      out = new BufferedWriter(fstream);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    for (int wordNumber = 1; wordNumber <= numberOfwords; wordNumber++) {
      FileInputStream fis;
      try {
        fis = new FileInputStream(new File(Config.SHARED_DIRECTORY_LOCATION + "RM" + wordNumber));
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

  }

}
