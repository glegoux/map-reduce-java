package com.mapreduce;

import com.mapreduce.system.CommandSystem;

public class App {
  public static void main(String[] args) {
    //System.out.println("Hello"); 
    System.out.println(CommandSystem.executeBash("echo Hello"));
  }
}
