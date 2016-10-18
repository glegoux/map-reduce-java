package com.mapreduce;

import static com.mapreduce.system.CommandSystem.executeBash;
import static com.mapreduce.system.CommandSystem.executeScriptBash;
import static com.mapreduce.system.CommandSystem.executePython;
import static com.mapreduce.system.CommandSystem.executeScriptPython;
import static com.mapreduce.system.CommandSystem.executeBySSH;

import java.util.Objects;

import com.mapreduce.system.CommandSystem.Result;
import junit.framework.TestCase;

public class CommandSystemTest extends TestCase {

  public static String DATA_DIR = "src/test/java/com/mapreduce/data/";

  public void test01executeBashOK() {
    Result result = executeBash("echo $((2 + 3))");
    assertEquals(true, Objects.equals(result.stderr, ""));
    assertEquals(true, Integer.parseInt(result.stdout.trim()) == 5);
    assertEquals(true, result.status == 0);
  }

  public void test02executeBashKO() {
    Result result = executeBash("unknown");
    assertEquals(true, result.status > 0);
  }

  public void test03executeScriptBashOK() {
    Result result = executeScriptBash(DATA_DIR + "script.sh");
    assertEquals(true, Objects.equals(result.stderr, ""));
    assertEquals(true, Objects.equals(result.stdout.trim(), "hello"));
    assertEquals(true, result.status == 0);
  }

  public void test04executeScriptBashKO() {
    Result result = executeScriptBash("unknown");
    assertEquals(true, result.status > 0);
  }

  public void test05executePyhtonOK() {
    Result result = executePython("print(1 + 2)");
    assertEquals(true, Objects.equals(result.stderr, ""));
    assertEquals(true, Integer.parseInt(result.stdout.trim()) == 3);
    assertEquals(true, result.status == 0);
  }

  public void test06executePyhtonKO() {
    Result result = executePython("print 1 + 2");
    assertEquals(true, result.status > 0);
  }

  public void test07executeScriptPythonOK() {
    Result result = executeScriptPython(DATA_DIR + "script.py");
    assertEquals(true, Objects.equals(result.stderr, ""));
    assertEquals(true, Objects.equals(result.stdout.trim(), "hello"));
    assertEquals(true, result.status == 0);
  }

  public void test08executeScriptPythonKO() {
    Result result = executeScriptPython("unknown");
    assertEquals(true, result.status > 0);
  }

  public void test09executeBySSHOK() {
    Result result = executeBySSH("echo $((2+3))");
    assertEquals(true, Objects.equals(result.stderr, ""));
    assertEquals(true, Integer.parseInt(result.stdout.trim()) == 5);
    assertEquals(true, result.status == 0);
  }

  public void test10executeBySSHKO() {
    Result result = executeBySSH("unknown");
    assertEquals(true, result.status > 0);
  }
}
