package com.mapreduce.network;

import static com.mapreduce.config.Config.DEFAULT_USER_SSH;
import static com.mapreduce.config.Config.SSH;
import static com.mapreduce.config.Config.SSH_TIMEOUT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mapreduce.config.Config;
import com.mapreduce.system.SystemCommand;
import com.mapreduce.system.SystemCommand.Result;
import com.mapreduce.utils.Utils;

public class Cluster {

  public List<String> slaveNames;

  public Cluster(int numberOfMachines) {
    this.slaveNames = Collections.synchronizedList(new ArrayList<String>());
    List<String> hostnames = Utils.readFile(Config.IP_LOCATION);
    for (String hostname : hostnames) {
      if (this.slaveNames.size() == numberOfMachines) {
        break;
      }
      Result result =
          SystemCommand.execute(SSH, DEFAULT_USER_SSH + "@" + hostname.trim(), "-o",
              String.format("ConnectTimeout=%s", SSH_TIMEOUT), "echo OK");
      if (result.status == 0) {
        slaveNames.add(hostname.trim());
      }
    }
    if (this.slaveNames.size() < numberOfMachines) {
      System.err.println("Not enough available machines");
      System.exit(1);;
    }
  }

}
