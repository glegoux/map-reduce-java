package com.mapreduce.network;

import com.mapreduce.system.SystemCommand.Result;

public class UmxRmxJob extends Thread {

  public String slaveName;
  public Result result;
  public boolean isRemote;
  public String[] args;

  public UmxRmxJob(boolean isRemote, String threadName, String slaveName, String... args) {
    super(threadName);
    this.slaveName = slaveName;
    this.isRemote = isRemote;
    this.args = args;
  }

  @Override
  public void run() {
    if (isRemote) {
      this.result = SlaveHelper.executeRemote(slaveName, args);
    } else {
      this.result = SlaveHelper.executeLocal(args);
    }
  }

}
