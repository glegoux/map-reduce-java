package com.mapreduce.utils;

import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.mapreduce.config.Config;


public class Cleaner {

  public static Set<String> excludedWords = Sets.newHashSet("-", "l", "le", "la", "les", "un",
      "une", "des", "de", "d", "au", "aux", "du", "mon", "ma", "mes", "m", "ton", "ta", "tes", "t",
      "se", "son", "sa", "ses", "s", "notre", "nos", "votre", "vos", "leur", "leurs", "ce",
      "cette", "ces", "a", "et", "ou", "or", "que", "n", "ne", "il", "ils", "elle", "elles",
      "vous", "nous");

  public static String clean(String filename) {
    String baseName = FilenameUtils.getBaseName(filename);
    String extension = FilenameUtils.getExtension(filename);
    String newName = String.format("%s-clean.%s", baseName, extension);
    String destLocation = Paths.get(Config.SHARED_DIRECTORY_LOCATION, newName).toString();
    List<String> lines = new ArrayList<>();
    for (String line : Utils.readFile(filename)) {
      String withoutAccent = Normalizer.normalize(line, Normalizer.Form.NFD);
      String withoutSimpleQuote = withoutAccent.toLowerCase().replaceAll("'", " ");
      String onlyWord = withoutSimpleQuote.toLowerCase().replaceAll("[^-a-z ]", "");
      String excludedWordsRgex = String.format("\\b(%s)\\b", Joiner.on('|').join(excludedWords));
      String withoutUselessWord = onlyWord.replaceAll(excludedWordsRgex, "");
      String withoutMultipleBlanks = withoutUselessWord.replaceAll("[ ]+", " ");
      if (line.isEmpty()) {
        continue;
      }
      lines.add(withoutMultipleBlanks.trim());
    }
    Utils.writeFile(destLocation, lines);
    return destLocation;
  }
}
