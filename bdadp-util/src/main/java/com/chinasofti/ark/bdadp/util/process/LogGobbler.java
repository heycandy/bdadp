package com.chinasofti.ark.bdadp.util.process;

import com.google.common.base.Joiner;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class LogGobbler extends Thread {

    private final BufferedReader inputReader;
    private final Logger logger;
    private final LogBuffer<String> buffer;

    public LogGobbler(final Reader inputReader, final Logger logger,
                      final int bufferLines) {
        this.inputReader = new BufferedReader(inputReader);
        this.logger = logger;
        buffer = new LogBuffer<String>(bufferLines);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && buffer.getSize() < buffer.getMaxSize()) {
                String line = inputReader.readLine();
                if (line == null) {
                    return;
                }

                buffer.append(line);
                log(line);
            }
            if (buffer.getSize() == buffer.getMaxSize()) {
                buffer.append("...", false);
            }
        } catch (IOException e) {
            error("Error reading from logging stream:", e);
        }
    }

    private void log(String message) {
        if (logger != null) {
            logger.info(message);
        }
    }

    private void error(String message, Exception e) {
        if (logger != null) {
            logger.error(message, e);
        }
    }

    private void info(String message, Exception e) {
        if (logger != null) {
            logger.info(message, e);
        }
    }

    public void awaitCompletion(final long waitMs) {
        try {
            join(waitMs);
        } catch (InterruptedException e) {
            info("I/O thread interrupted.", e);
        }
    }

    public String getRecentLog() {
        return Joiner.on(System.getProperty("line.separator")).join(buffer);
    }

    public LogBuffer<String> getBuffer() {
        return this.buffer;
    }

}
