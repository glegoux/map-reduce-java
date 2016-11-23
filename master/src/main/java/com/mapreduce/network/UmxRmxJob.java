package com.mapreduce.network;

import com.mapreduce.system.SystemCommand.Result;

public class UmxRmxJob extends Thread {

  public String slaveName;
  public Result result;
  public boolean isRemote;
  public String[] args;
  public Cluster cluster;

  public UmxRmxJob(boolean isRemote, Cluster cluster, String threadName, String slaveName,
      String... args) {
    super(threadName);
    this.slaveName = slaveName;
    this.isRemote = isRemote;
    this.args = args;
    this.cluster = cluster;
  }

  @Override
  public void run() {
    if (isRemote) {
      this.result = SlaveHelper.executeRemote(slaveName, args);
    } else {
      this.result = SlaveHelper.executeLocal(args);
    }

    while (this.result.status != 0) {
      executeRandom();
    }
  }

  public void executeRandom() {
    String slaveName =
        cluster.slaveNames.get(SlaveHelper.chooseRandomSlaveIndex(cluster.slaveNames.size()));
    if (isRemote) {
      this.result = SlaveHelper.executeRemote(slaveName, args);
      System.out.println(result);
    } else {
      this.result = SlaveHelper.executeLocal(args);
    }
  }

}
