package com.mapreduce.network;

import static com.mapreduce.config.Config.DEFAULT_USER_SSH;
import static com.mapreduce.config.Config.JAVA;
import static com.mapreduce.config.Config.SLAVE_JAR_LOCATION;
import static com.mapreduce.config.Config.SSH;

import java.util.Random;

import com.mapreduce.config.Config;
import com.mapreduce.system.SystemCommand;
import com.mapreduce.system.SystemCommand.Result;

public class SlaveHelper {

  public static int chooseSlaveIndex(int taskNumber, int numberOfSlaves) {
    return taskNumber % numberOfSlaves;
  }

  public static int chooseRandomSlaveIndex(int numberOfSlaves) {
    Random rand = new Random();
    return rand.nextInt(numberOfSlaves);
  }

  public static Result executeRemote(String slaveName, String... arguments) {
    String[] args = new String[arguments.length + 3];
    args[0] = SSH;
    args[1] = String.format("%s@%s", DEFAULT_USER_SSH, slaveName);
    args[2] = "export MAP_REDUCE_HOME=" + Config.MAP_REDUCE_HOME + ";";
    args[2] += JAVA;
    args[2] += " -jar ";
    args[2] += SLAVE_JAR_LOCATION;
    System.arraycopy(arguments, 0, args, 3, arguments.length);
    return SystemCommand.execute(args);
  }

  public static Result executeLocal(String... arguments) {
    String[] args = new String[arguments.length + 3];
    args[0] = JAVA;
    args[1] = "-jar";
    args[2] = SLAVE_JAR_LOCATION;
    System.arraycopy(arguments, 0, args, 3, arguments.length);
    return SystemCommand.execute(args);
  }

}
