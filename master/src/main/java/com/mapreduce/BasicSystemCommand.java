package com.mapreduce;

import static com.mapreduce.Config.DEFAULT_HOSTNAME_SSH;
import static com.mapreduce.Config.DEFAULT_USER_SSH;
import static com.mapreduce.Config.PYTHON;
import static com.mapreduce.Config.SHELL;
import static com.mapreduce.Config.SSH;
import static com.mapreduce.Config.SSH_PASS;
import static com.mapreduce.SystemCommand.Result;
import static com.mapreduce.SystemCommand.execute;

public class BasicSystemCommand {

  public static Result executeRemoteJar(String pahtToJar, String ip) {
    return execute(SSH, String.format("%s@%s", DEFAULT_USER_SSH, ip),
        String.format("java -jar %s", pahtToJar));
  }

  public static Result executeBySSH(String command) {
    return execute(SSH, String.format("%s@%s", DEFAULT_USER_SSH, DEFAULT_HOSTNAME_SSH), command);
  }

  public static Result executeBySSH(String user, String hostname, String command) {
    return execute(SSH, String.format("%s@%s", user, hostname), command);
  }

  public static Result executeBySSHPass(String user, String hostname, String password,
      String command) {
    return execute(SSH_PASS, "-p", password, SSH, String.format("%s@%s", user, hostname), command);
  }

  public static Result executeScriptBash(String pathToScript) {
    return execute(SHELL, pathToScript);
  }

  public static Result executeBash(String code) {
    return execute(SHELL, "-c", code);
  }

  public static Result executeScriptPython(String pathToScript) {
    return execute(PYTHON, pathToScript);
  }

  public static Result executePython(String code) {
    return execute(PYTHON, "-c", code);
  }

}
