package com.mapreduce.network;

import java.util.Map;

import com.mapreduce.system.SystemCommand.Result;

public class SxUmxJob extends Thread {

  public String slaveName;
  public Result result;
  public boolean isRemote;
  public String[] args;
  public Map<Integer, String[]> umxMap;
  public Cluster cluster;

  public SxUmxJob(boolean isRemote, Cluster cluster, Map<Integer, String[]> umxMap,
      String threadName, String slaveName, String... args) {
    super(threadName);
    this.slaveName = slaveName;
    this.isRemote = isRemote;
    this.umxMap = umxMap;
    this.args = args;
    this.cluster = cluster;
  }

  @Override
  public void run() {
    if (this.isRemote) {
      this.result = SlaveHelper.executeRemote(this.slaveName, this.args);
      System.out.println(result);
    } else {
      this.result = SlaveHelper.executeLocal(this.args);
    }

    while (this.result.status != 0) {
      executeRandom();
    }

    int umx = Integer.parseInt(this.args[1]);
    String[] words = this.result.stdout.split("\\n");
    this.umxMap.put(umx, words);
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
