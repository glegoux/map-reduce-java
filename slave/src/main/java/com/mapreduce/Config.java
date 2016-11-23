package com.mapreduce;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  public static final String CONFIG_FILE = "/cal/homes/glegoux/map-reduce-env/config/config.properties";
  public static Properties CONFIG;
  static {
    try {
      CONFIG = new Properties();
      CONFIG.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      System.err.println(String.format("Missing configuration %s", CONFIG_FILE));
    }
  }

  // system
  public static final String ENCODING = CONFIG.getProperty("encoding");
  public static final String OS = CONFIG.getProperty("os");

  // architecture
  public static final String SHARED_DIRECTORY_LOCATION = CONFIG.getProperty("shared.directory.location");
  public static final String SLAVE_JAR_LOCATION = CONFIG.getProperty("slave.jar.location");
  public static final String MASTER_JAR_LOCATION = CONFIG.getProperty("master.jar.location");

}
