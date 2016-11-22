package com.mapreduce;

import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;


public class Cleaner {

  public static String SCRIPT_CLEANER_LOCATION = "./script/clean-text.sh";

  public static String clean(String filename) {
    String basename = FilenameUtils.getBaseName(filename);
    String extension = FilenameUtils.getExtension(filename);
    String cleanedFilename =
        Paths.get(Config.SHARED_DIRECTORY_LOCATION,
            String.format("%s-clean.%s", basename, extension)).toString();
    BasicSystemCommand.executeBash(String.format("%s %s > %s", SCRIPT_CLEANER_LOCATION, filename,
        cleanedFilename));
    return cleanedFilename;
  }


}
