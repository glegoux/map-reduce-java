package com.mapreduce.profiler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.mapreduce.config.Config;

public class Logging {

  public static Logger get() {
    Logger logger = null;
    try {
      logger = Logger.getLogger("");
      logger.setUseParentHandlers(false);
      LogManager.getLogManager().reset();
      FileHandler fileHandler = new FileHandler(Config.PROFILER_LOG_LOCATION, true);
      SimpleFormatter formatter = new SimpleFormatter();
      fileHandler.setFormatter(formatter);
      logger.addHandler(fileHandler);
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
    }
    return logger;
  }

}
