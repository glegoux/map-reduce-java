package com.mapreduce.utils;

import com.mapreduce.system.BasicSystemCommand;
import com.mapreduce.system.SystemCommand.Result;

public class Displayer {

  public static String SCRIPT_SORT_LOCATION = "./script/sort.sh";

  public static void result(String filename) {
    Result result =
        BasicSystemCommand.executeBash(String.format("%s %s", SCRIPT_SORT_LOCATION, filename));
    System.out.println(result.stdout);
  }



}
