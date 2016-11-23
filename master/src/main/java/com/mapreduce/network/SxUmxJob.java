package com.mapreduce.network;

import java.util.Map;

import com.mapreduce.system.SystemCommand.Result;

public class SxUmxJob extends Thread {

  public String slaveName;
  public Result result;
  public boolean isRemote;
  public String[] args;
  public Map<Integer, String[]> umxMap;

  public SxUmxJob(boolean isRemote, Map<Integer, String[]> umxMap, String threadName,
      String slaveName, String... args) {
    super(threadName);
    this.slaveName = slaveName;
    this.isRemote = isRemote;
    this.umxMap = umxMap;
    this.args = args;
  }

  @Override
  public void run() {
    if (isRemote) {
      this.result = SlaveHelper.executeRemote(slaveName, args);
      System.out.println(result);
    } else {
      this.result = SlaveHelper.executeLocal(args);
    }

    int umx = Integer.parseInt(this.args[1]);
    String[] words = this.result.stdout.split("\\n");
    umxMap.put(umx, words);
  }

}
