package com.mapreduce;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

  public static final String CONFIG_FILE = getAbsoluePath("config/config.properties");
  public static Properties CONFIG;
  static {
    try {
      CONFIG = new Properties();
      CONFIG.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      System.err.println(String.format("Missing configuration %s", CONFIG_FILE));
      System.exit(1);
    }
  }
  
  private static String getAbsoluePath(String pathName) {
    String prefixPathName = System.getenv().get("MAP_REDUCE_HOME");
    if (prefixPathName == null) {
      System.err.println(String.format("MAP_REDUCE_HOME environment variable doesn't exist"));
      System.exit(1);
    }
    if (Files.notExists(Paths.get(prefixPathName))) {
      System.err.println(String.format("%s doesn't exist", prefixPathName));
      System.exit(1);
    }
    String absolutePathName = Paths.get(prefixPathName, pathName).toString();
    if (Files.notExists(Paths.get(absolutePathName))) {
      System.err.println(String.format("%s doesn't exist", absolutePathName));
      System.exit(1);
    }
    return absolutePathName;
  }
  
  private static String getAbsoluePathFromProperty(String property) {
    String propertyValue = CONFIG.getProperty(property);
    if (propertyValue == null) {
      System.err.println(String.format("%s doesn't defined", property));
      System.exit(1);
    }
    return getAbsoluePath(propertyValue);
  }


  // system
  public static final String ENCODING = CONFIG.getProperty("encoding");
  public static final String OS = CONFIG.getProperty("os");

  // architecture
  public static final String SHARED_DIRECTORY_LOCATION = getAbsoluePathFromProperty("shared.directory.location");
  public static final String SLAVE_JAR_LOCATION = getAbsoluePathFromProperty("slave.jar.location");
  public static final String MASTER_JAR_LOCATION = getAbsoluePathFromProperty("master.jar.location");

}
