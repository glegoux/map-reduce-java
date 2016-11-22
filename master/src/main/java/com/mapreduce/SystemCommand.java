package com.mapreduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.google.common.base.Joiner;

public class SystemCommand {

  public static class Result {

    public String commandLine;
    public String stdout;
    public String stderr;
    public int status;

    public Result(String commandLine, String stdout, String stderr, int status) {
      this.commandLine = commandLine;
      this.stdout = stdout;
      this.stderr = stderr;
      this.status = status;
    }

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this);
    }

  }

  public static Result execute(String... command) {
    ProcessBuilder pb = new ProcessBuilder(command);
    String commandLine = Joiner.on(" ").join(command);
    try {
      Process shell = pb.start();
      shell.waitFor();
      String stdout = convertInputStream(shell.getInputStream());
      String stderr = convertInputStream(shell.getErrorStream());
      int status = shell.exitValue();
      return new Result(commandLine, stdout.toString(), stderr.toString(), status);
    } catch (IOException e) {
      return new Result(commandLine, "", "I/O error occurs", 127);
    } catch (InterruptedException e) {
      return new Result(commandLine, "", "Interrupted process", 130);
    }
  }

  private static String convertInputStream(InputStream is) throws IOException {
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line + '\n');
    }
    return sb.toString();
  }

}
