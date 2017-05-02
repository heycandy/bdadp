package com.chinasofti.ark.bdadp.util.process;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper code for building a process
 */
public class ArkBdadpProcessBuilder {

    private List<String> cmd = new ArrayList<String>();
    private Map<String, String> env = new HashMap<String, String>();
    private String workingDir = System.getProperty("user.dir");
    private Logger logger = LoggerFactory.getLogger(ArkBdadpProcess.class);

    private int stdErrSnippetSize = 30;
    private int stdOutSnippetSize = 30;

    public ArkBdadpProcessBuilder(String... command) {
        addArg(command);
    }

    public ArkBdadpProcessBuilder addArg(String... command) {
        for (String c : command) {
            cmd.add(c);
        }
        return this;
    }

    public ArkBdadpProcessBuilder setWorkingDir(String dir) {
        this.workingDir = dir;
        return this;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public ArkBdadpProcessBuilder setWorkingDir(File f) {
        return setWorkingDir(f.getAbsolutePath());
    }

    public ArkBdadpProcessBuilder addEnv(String variable, String value) {
        env.put(variable, value);
        return this;
    }

    public Map<String, String> getEnv() {
        return this.env;
    }

    public ArkBdadpProcessBuilder setEnv(Map<String, String> m) {
        this.env = m;
        return this;
    }

    public int getStdErrorSnippetSize() {
        return this.stdErrSnippetSize;
    }

    public ArkBdadpProcessBuilder setStdErrorSnippetSize(int size) {
        this.stdErrSnippetSize = size;
        return this;
    }

    public int getStdOutSnippetSize() {
        return this.stdOutSnippetSize;
    }

    public ArkBdadpProcessBuilder setStdOutSnippetSize(int size) {
        this.stdOutSnippetSize = size;
        return this;
    }

    public ArkBdadpProcessBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ArkBdadpProcess build() {
        return new ArkBdadpProcess(cmd, env, workingDir, logger);

    }

    public List<String> getCommand() {
        return this.cmd;
    }

    public String getCommandString() {
        return Joiner.on(" ").join(getCommand());
    }

    @Override
    public String toString() {
        return "ProcessBuilder(cmd = " + Joiner.on(" ").join(cmd) + ", env = " + env + ", cwd = "
                + workingDir + ")";
    }
}
