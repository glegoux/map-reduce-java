package com.mapreduce;

import static com.mapreduce.Config.DEFAULT_USER_SSH;
import static com.mapreduce.Config.SLAVE_JAR_LOCATION;
import static com.mapreduce.Config.SSH;

import com.mapreduce.system.SystemCommand;
import com.mapreduce.system.SystemCommand.Result;

public class Slave extends Thread {

  public String slaveName;
  public Result result;
  public boolean isRemote;
  public String[] args;

  public Slave(boolean isRemote, String threadName, String slaveName, String... args) {
    super(threadName);
    this.slaveName = slaveName;
    this.isRemote = isRemote;
    this.args = args;
  }

  @Override
  public void run() {
    if (isRemote) {
      this.result = executeRemote(slaveName);
    } else {
      this.result = executeLocal();
    }
  }

  public Result executeRemote(String slaveName) {
    String[] args = new String[this.args.length + 2];
    args[0] = String.format("%s %s@%s", SSH, DEFAULT_USER_SSH, slaveName);
    args[1] = String.format("/usr/bin/java -jar %s", SLAVE_JAR_LOCATION);
    System.arraycopy(this.args, 0, args, 2, this.args.length);
    return SystemCommand.execute(args);
  }

  public Result executeLocal() {
    String[] args = new String[this.args.length + 3];
    args[0] = Config.JAVA;
    args[1] = "-jar";
    args[2] = SLAVE_JAR_LOCATION;
    System.arraycopy(this.args, 0, args, 3, this.args.length);
    return SystemCommand.execute(args);
  }

}
