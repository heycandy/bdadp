package com.chinasofti.ark.bdadp.component.api.options;

import com.google.common.collect.Maps;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.channel.MemoryChannel;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.data.DataType;
import com.chinasofti.ark.bdadp.component.api.data.SparkData;
import com.chinasofti.ark.bdadp.component.api.data.StreamData;
import com.chinasofti.ark.bdadp.security.SecurityLogin;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.dstream.DStream;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import java8.util.stream.StreamSupport;

/**
 * Created by White on 2017/1/5.
 */
public class SparkScenarioOptions extends ScenarioOptions {

    private static SparkContext defaultSparkContext = null;
    private static SQLContext defaultSqlContext = null;
    private static HiveContext defaultHiveContext = null;
    private SparkContext sparkContext;
    private SQLContext sqlContext;
    private HiveContext hiveContext;
    private StreamingContext streamingContext;

    public SparkScenarioOptions() throws IOException {
        this(Maps.newConcurrentMap());
    }

    public SparkScenarioOptions(Map<String, String> settings) throws IOException {
        super(settings);
        this.security();
        this.context();

    }

    public SparkScenarioOptions(ScenarioOptions options) throws IOException {
        super(options);
        this.security();
        this.context();
    }

    private void security() throws IOException {
        if (java8.util.Maps.getOrDefault(
                this.getSettings(), "spark.authenticate", "true").equals("true")) {

            String defaultUserKeytabPath;
            String defaultKrb5ConfPath;

            URL urlUserKeytab = this.getClass().getClassLoader().getResource("user.keytab");
            URL urlKrb5Conf = this.getClass().getClassLoader().getResource("krb5.conf");
            if (urlUserKeytab != null) {
                defaultUserKeytabPath = urlUserKeytab.getPath();
            } else {
                defaultUserKeytabPath =
                        Paths.get(System.getProperty("user.hadoop.conf"), "user.keytab").toString();
            }

            if (urlKrb5Conf != null) {
                defaultKrb5ConfPath = urlKrb5Conf.getPath();
            } else {
                defaultKrb5ConfPath =
                        Paths.get(System.getProperty("user.hadoop.conf"), "krb5.conf").toString();
            }

            String userPrincipal = java8.util.Maps.getOrDefault(
                    this.getSettings(), "spark.yarn.principal", System.getProperty("user.principal"));

            String userKeytabPath = java8.util.Maps.getOrDefault(
                    this.getSettings(), "spark.yarn.keytab", defaultUserKeytabPath);

            String krb5ConfPath = System.getProperty("java.security.krb5.conf", defaultKrb5ConfPath);

            this.getSettings().put("spark.yarn.principal", userPrincipal);
            this.getSettings().put("spark.yarn.keytab", userKeytabPath);

            SecurityLogin.login(
                    userPrincipal, userKeytabPath, krb5ConfPath);

        }

    }

    private void context() {
        if (this.isDebug()) {
            this.sparkContext = defaultSparkContext();
            this.sqlContext = defaultSqlContext();
//      this.hiveContext = defaultHiveContext();
        } else {
            SparkConf config = new SparkConf()
                    .set("spark.app.id", this.getExecutionId());

            StreamSupport.stream(this.getSettings().entrySet())
                    .forEach(entry -> config.set(entry.getKey(), entry.getValue()));
            this.sparkContext = new SparkContext(config);
            this.sqlContext = new SQLContext(this.sparkContext);
//      this.hiveContext = new HiveContext(this.sparkContext);
        }
    }

    private SparkContext defaultSparkContext() {
        synchronized (SparkScenarioOptions.class) {
            if (SparkScenarioOptions.defaultSparkContext == null) {
                SparkConf config = new SparkConf();

                StreamSupport.stream(this.getSettings().entrySet())
                        .forEach(entry -> config.set(entry.getKey(), entry.getValue()));

                SparkScenarioOptions.defaultSparkContext = new SparkContext(config);
            }
        }

        return SparkScenarioOptions.defaultSparkContext;
    }

    private SQLContext defaultSqlContext() {
        synchronized (SparkScenarioOptions.class) {
            if (SparkScenarioOptions.defaultSqlContext == null) {
                SparkContext sparkContext = defaultSparkContext();

                SparkScenarioOptions.defaultSqlContext = new SQLContext(sparkContext);
            }
        }

        return SparkScenarioOptions.defaultSqlContext;
    }

    private HiveContext defaultHiveContext() {
        synchronized (SparkScenarioOptions.class) {
            if (SparkScenarioOptions.defaultHiveContext == null) {
                SparkContext sparkContext = defaultSparkContext();

                SparkScenarioOptions.defaultHiveContext = new HiveContext(sparkContext);
            }
        }

        return SparkScenarioOptions.defaultHiveContext;
    }

    @Override
    public Channel union(Collection<Channel> channels) {
      Data data = null;

      if (StreamSupport.stream(channels)
          .allMatch(channel -> channel.output().getType() == DataType.SPARK)) {
        DataFrame rawData = StreamSupport.stream(channels)
            .map(channel -> (DataFrame) channel.output().getRawData())
            .reduce(DataFrame::unionAll)
            .orElse(this.sqlContext().emptyDataFrame());
        data = new SparkData(rawData);
      } else if (StreamSupport.stream(channels)
          .allMatch(channel -> channel.output().getType() == DataType.STREAM)) {
        DStream<String> rawData = StreamSupport.stream(channels)
            .map(channel -> (DStream<String>) channel.output().getRawData())
            .reduce(DStream::union)
            .get();
        data = new StreamData(rawData);
      }

        Channel channel = new MemoryChannel();

        channel.input(data);

        return channel;
    }

    public SparkContext sparkContext() {
        return sparkContext;
    }

    public SQLContext sqlContext() {
        return sqlContext;
    }

    public HiveContext hiveContext() {
        return hiveContext;
    }

    public synchronized StreamingContext streamingContext() {
        if (streamingContext == null) {
            String
                batchInterval =
                java8.util.Maps
                    .getOrDefault(this.settings, "spark.streaming.batchInterval", "5000");
            long milliseconds = Long.valueOf(batchInterval);
            streamingContext =
                new StreamingContext(this.sparkContext, Durations.milliseconds(milliseconds));

            super.setStreaming(true);
        }

        return streamingContext;
    }

    public void startAndAwaitTermination() {
      if (this.streamingContext != null) {
        this.streamingContext.start();
        this.streamingContext.awaitTermination();
      }

    }

    public void stop() {
      if (this.streamingContext != null) {
        this.streamingContext.stop(false);
      }
    }
}
