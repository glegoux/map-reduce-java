package com.mapreduce;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {

  public static void main(String[] args) throws IOException {

    if (args.length == 0) {
      System.err.println("Missing argument");
      System.exit(1);
    }

    String mode = args[0];

    if (Objects.equals(mode, "modeSXUMX")) {

      if (args.length != 2) {
        System.err.println("Missing or too many arguments");
        System.exit(1);
      }

      Integer sxNumber = Integer.parseInt(args[1]);
      List<String> words = Mapper.split("S" + sxNumber);
      Mapper.persist("UM" + sxNumber, words);

    } else if (Objects.equals(mode, "modeUMXSMX")) {

      if (args.length < 4) {
        System.err.println("Missing or too many arguments");
        System.exit(1);
      }

      String word = args[1];
      Integer smxNumber = Integer.parseInt(args[2]);
      String[] umxs = Arrays.copyOfRange(args, 3, args.length);
      int frequency = Reducer.merge(word, umxs);
      Reducer.persistSMX("SM" + smxNumber, word, frequency);
      Reducer.persistRMX("RM" + smxNumber, word, frequency);

    } else {

      System.err.println("Wrong argument");
      System.exit(1);

    }

  }

}
