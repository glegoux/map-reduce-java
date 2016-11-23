package com.mapreduce.system;

import static com.mapreduce.config.Config.DEFAULT_USER_SSH;
import static com.mapreduce.config.Config.PYTHON;
import static com.mapreduce.config.Config.SHELL;
import static com.mapreduce.config.Config.SSH;
import static com.mapreduce.config.Config.SSH_PASS;
import static com.mapreduce.system.SystemCommand.execute;

import com.mapreduce.system.SystemCommand.Result;

public class BasicSystemCommand {

  public static Result executeRemoteJar(String pahtToJar, String hostname) {
    return execute(SSH, String.format("%s@%s", DEFAULT_USER_SSH, hostname),
        String.format("java -jar %s", pahtToJar));
  }

  public static Result executeBySSH(String hostname, String command) {
    return execute(SSH, String.format("%s@%s", DEFAULT_USER_SSH, hostname), command);
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
