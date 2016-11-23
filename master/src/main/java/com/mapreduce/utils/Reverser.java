package com.mapreduce.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reverser {

  public static Map<String, List<String>> umxMap(Map<Integer, String[]> umxMap) {

    Map<String, List<String>> wordMap = new HashMap<>();

    for (Map.Entry<Integer, String[]> entry : umxMap.entrySet()) {
      String umx = "UM" + entry.getKey();
      for (String word : entry.getValue()) {
        if (!wordMap.containsKey(word)) {
          wordMap.put(word, new ArrayList<String>());
        }
        wordMap.get(word).add(umx);
      }
    }

    return wordMap;

  }

}
