package fr.telecom.system;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Joiner;

public class CommandSystem {

	public static void main(String[] args) {
		System.out.println(executeBash("pwd"));
		System.out.println(executeBySSH("pwd").commandLine);
	}

	public static final String CONFIG_FILE = "src/main/java/fr/telecom/system/config.properties";
	public static Properties CONFIG;
	static {
		try {
			CONFIG = CommandSystem.getConfig();
		} catch (IOException e) {
			System.err.println(String.format("Missing configuration %s", CONFIG_FILE));
		}
	}

	public static final String ENCODING = CONFIG.getProperty("encoding", "UTF-8");
	public static final String OS = CONFIG.getProperty("os", "Linux");

	public static final String SHELL = CONFIG.getProperty("bash.shell", "/bin/bash");

	public static final String PYTHON = CONFIG.getProperty("python", "/usr/bin/python3");

	public static final String SSH = CONFIG.getProperty("ssh", "/usr/bin/ssh");
	public static final String SSH_PASS = CONFIG.getProperty("sshpass", "/usr/bin/sshpass");

	public static final String DEFAULT_USER_SSH = CONFIG.getProperty("ssh.user", "glegoux");
	public static final String DEFAULT_HOSTNAME_SSH = CONFIG.getProperty("ssh.hostname", "ssh.enst.fr");

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

	public static Result executeBySSH(String command) {
		return execute(SSH, String.format("%s@%s", DEFAULT_USER_SSH, DEFAULT_HOSTNAME_SSH), command);
	}

	public static Result executeBySSH(String user, String hostname, String command) {
		return execute(SSH, String.format("%s@%s", user, hostname), command);
	}

	public static Result executeBySSHPass(String user, String hostname, String password, String command) {
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

	public static Properties getConfig() throws IOException {
		Properties prop = new Properties();
		InputStream is = new FileInputStream(CONFIG_FILE);
		prop.load(is);
		return prop;
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
