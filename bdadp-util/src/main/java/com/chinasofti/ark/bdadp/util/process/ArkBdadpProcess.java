package com.chinasofti.ark.bdadp.util.process;

import com.google.common.base.Joiner;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A less shitty version of java.lang.Process.
 * <p>
 * Output is read by seperate threads to avoid deadlock and logged to log4j loggers.
 */
public class ArkBdadpProcess {

    private final String workingDir;
    private final List<String> cmd;
    private final Map<String, String> env;
    private final Logger logger;

    private final CountDownLatch startupLatch;
    private final CountDownLatch completeLatch;

    private volatile int processId;
    private volatile Process process;

    private LogGobbler outputGobbler;
    private LogGobbler errorGobbler;

    public ArkBdadpProcess(final List<String> cmd, final Map<String, String> env,
                           final String workingDir, final Logger logger) {
        this.cmd = cmd;
        this.env = env;
        this.workingDir = workingDir;
        this.processId = -1;
        this.startupLatch = new CountDownLatch(1);
        this.completeLatch = new CountDownLatch(1);
        this.logger = logger;
    }

    /**
     * Execute this process, blocking until it has completed.
     */
    public void run() throws IOException {
        if (this.isStarted() || this.isComplete()) {
            throw new IllegalStateException("The process can only be used once.");
        }

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.directory(new File(workingDir));
        builder.environment().putAll(env);
        this.process = builder.start();
//		this.processId = processId(process);
        if (processId == 0) {
            logger.debug("Spawned thread with unknown process id");
        } else {
            logger.debug("Spawned thread with process id " + processId);
        }

        this.startupLatch.countDown();

        outputGobbler =
                new LogGobbler(new InputStreamReader(process.getInputStream()), logger, 30);
        errorGobbler =
                new LogGobbler(new InputStreamReader(process.getErrorStream()), logger, 30);

        outputGobbler.start();
        errorGobbler.start();
        int exitCode = -1;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            logger.info("Process interrupted. Exit code is " + exitCode, e);
        }

        completeLatch.countDown();
        if (exitCode != 0) {
//			throw new ProcessFailureException(exitCode, errorGobbler.getRecentLog());
        }

        // try to wait for everything to get logged out before exiting
        outputGobbler.awaitCompletion(5000);
        errorGobbler.awaitCompletion(5000);
    }

    /**
     * Await the completion of this process
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void awaitCompletion() throws InterruptedException {
        this.completeLatch.await();
    }

    /**
     * Await the start of this process
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void awaitStartup() throws InterruptedException {
        this.startupLatch.await();
    }

    /**
     * Get the process id for this process, if it has started.
     *
     * @return The process id or -1 if it cannot be fetched
     */
    public int getProcessId() {
        checkStarted();
        return this.processId;
    }

    public LogGobbler getOutputGobbler() {
        return this.outputGobbler;
    }

    public LogGobbler getErrorGobbler() {
        return this.errorGobbler;
    }

    /**
     * Attempt to kill the process, waiting up to the given time for it to die
     *
     * @param time The amount of time to wait
     * @param unit The time unit
     * @return true iff this soft kill kills the process in the given wait time.
     */
    public boolean softKill(final long time, final TimeUnit unit) throws InterruptedException {
        checkStarted();
        if (processId != 0 && isStarted()) {
            try {
                Runtime.getRuntime().exec("kill " + processId);
                return completeLatch.await(time, unit);
            } catch (IOException e) {
                logger.error("Kill attempt failed.", e);
            }
            return false;
        }
        return false;
    }

    /**
     * Force kill this process
     */
    public void hardKill() {
        checkStarted();
        if (isRunning()) {
            if (processId != 0) {
                try {
                    Runtime.getRuntime().exec("kill -9 " + processId);
                } catch (IOException e) {
                    logger.error("Kill attempt failed.", e);
                }
            }
            process.destroy();
        }
    }

    /**
     * Attempt to get the process id for this process
     *
     * @param process The process to get the id from
     * @return The id of the process
     */
    private int processId(final Process process) {
        int processId = 0;
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);

            processId = f.getInt(process);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return processId;
    }

    /**
     * @return true iff the process has been started
     */
    public boolean isStarted() {
        return startupLatch.getCount() == 0L;
    }

    /**
     * @return true iff the process has completed
     */
    public boolean isComplete() {
        return completeLatch.getCount() == 0L;
    }

    /**
     * @return true iff the process is currently running
     */
    public boolean isRunning() {
        return isStarted() && !isComplete();
    }

    public void checkStarted() {
        if (!isStarted()) {
            throw new IllegalStateException("Process has not yet started.");
        }
    }

    @Override
    public String toString() {
        return "Process(cmd = " + Joiner.on(" ").join(cmd) + ", env = " + env + ", cwd = " + workingDir
                + ")";
    }
}
