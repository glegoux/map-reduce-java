package com.mapreduce.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  public static final String CONFIG_FILE =
      "/cal/homes/glegoux/map-reduce-env/config/config.properties";
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

  // ssh
  public static final String SSH = CONFIG.getProperty("ssh");
  public static final String SSH_PASS = CONFIG.getProperty("sshpass");
  public static final String DEFAULT_USER_SSH = CONFIG.getProperty("ssh.user");
  public static final String DEFAULT_HOSTNAME_SSH = CONFIG.getProperty("ssh.hostname");
  public static final String SSH_TIMOUT = CONFIG.getProperty("ssh.timeout");

  // java
  public static final String JAVA = CONFIG.getProperty("java");

  // bash
  public static final String SHELL = CONFIG.getProperty("bash.shell");

  // python
  public static final String PYTHON = CONFIG.getProperty("python");

  // architecture
  public static final int THREAD_NUMBER = Integer.parseInt(CONFIG.getProperty("thread.number"));
  public static final String IP_LOCATION = CONFIG.getProperty("ip.location");
  public static final String SHARED_DIRECTORY_LOCATION = CONFIG
      .getProperty("shared.directory.location");
  public static final String SLAVE_JAR_LOCATION = CONFIG.getProperty("slave.jar.location");
  public static final String MASTER_JAR_LOCATION = CONFIG.getProperty("master.jar.location");

  // profiler
  public static final String PROFILER_LOG_LOCATION = CONFIG.getProperty("profiler.log.location");

}
