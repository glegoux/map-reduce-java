package com.mapreduce;

import java.util.List;

import com.mapreduce.ip.IPReader;

public class App {
  public static void main(String[] args) {
    List<String> ips = IPReader.readFile("/com/mapreduce/ip/ip.txt");
    for (String ip : ips) {
      new SSHThread(String.format("Thread-%s", ip), ip).start();
    }
  }
}
