package com.mapreduce.network;

import static com.mapreduce.config.Config.DEFAULT_USER_SSH;
import static com.mapreduce.config.Config.SSH;
import static com.mapreduce.config.Config.SSH_TIMOUT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mapreduce.config.Config;
import com.mapreduce.system.SystemCommand;
import com.mapreduce.system.SystemCommand.Result;
import com.mapreduce.utils.Utils;

public class Cluster {

  public List<String> slaveNames;

  public Cluster() {
    this.slaveNames = Collections.synchronizedList(new ArrayList<String>());
    List<String> hostnames = Utils.readFile(Config.IP_LOCATION);
    for (String hostname : hostnames) {
      Result result =
          SystemCommand.execute(SSH, DEFAULT_USER_SSH + "@" + hostname.trim(), "-o",
              String.format("ConnectTimeout=%s", SSH_TIMOUT), "echo OK");
      System.out.println(result);
      if (result.status == 0) {
        slaveNames.add(hostname.trim());
      }
    }
  }

}
