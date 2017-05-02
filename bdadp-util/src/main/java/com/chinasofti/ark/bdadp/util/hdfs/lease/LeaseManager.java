package com.chinasofti.ark.bdadp.util.hdfs.lease;

import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manager to auto renew cluster lease.
 */
public class LeaseManager {

    private static final Logger LOG = LoggerFactory.getLogger(LeaseManager.class);
    private static final int PERIOD = 82800; // refresh connection every 23 hours
    private static ScheduledThreadPoolExecutor service;
    private static boolean started = false;

    static {
        try {
            service = new ScheduledThreadPoolExecutor(1);
        } catch (Throwable e) {
            LOG.error("Init hadoop configuration failed: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Start service.
     */
    public synchronized static void start() {
        if (!started) {
            service.scheduleAtFixedRate(new LeaseDaemon(), 0, PERIOD, TimeUnit.SECONDS);
            started = true;
            try {
                Thread.sleep(1000);// hold 1 sec to wait daemon thread run.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("LeaseDaemon start successful!");
            return;
        }

        LOG.warn("LeaseDaemon already started.");
    }

    private static class LeaseDaemon implements Runnable {

        @Override
        public void run() {
            try {
                LOG.info("Renewing cluster connection...");
                Configuration conf = ConfigurationClient.getInstance().getConfiguration();
                LeaseUtil.connect(conf);
                LOG.info("Renewing cluster connection successful!");
            } catch (IOException e) {
                LOG.error("SecurityDeamon ran into exception: ", e);
                return;
            }
        }
    }
}
