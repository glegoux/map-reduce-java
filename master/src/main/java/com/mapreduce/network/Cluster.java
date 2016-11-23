package com.mapreduce.network;

import java.util.ArrayList;
import java.util.List;

import com.mapreduce.config.Config;
import com.mapreduce.system.BasicSystemCommand;
import com.mapreduce.system.SystemCommand.Result;
import com.mapreduce.utils.Utils;

public class Cluster {
  
  public static void main(String[] args) {
    Cluster cluster = new Cluster();
    System.out.println(cluster.slaveNames);
  }

  public List<String> slaveNames;

  public Cluster() {
    this.slaveNames = new ArrayList<>();
    List<String> hostnames = Utils.readFile(Config.IP_LOCATION);
    for (String hostname : hostnames) {
      Result result = BasicSystemCommand.executeBySSH(hostname.trim(), "echo OK");
      if (result.status == 0) {
        slaveNames.add(hostname.trim());
      }
    }
  }

}
