package com.chinasofti.ark.bdadp.service.flow.impl;

import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;
import com.chinasofti.ark.bdadp.service.flow.bean.CacheMode;
import com.chinasofti.ark.bdadp.service.flow.bean.CacheableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlowVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.TaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * Created by White on 2016/09/27.
 */
public class CacheableFlowExecutorService implements FlowExecutorService {

    protected ListeningExecutorService executor;

    protected CacheMode cacheMode;
    protected String cachePath;
    private boolean cacheClean;

    @Override
    public void submit(CallableFlow flow, Vertex vertex) {
        Futures.addCallback(executor.submit(() -> {
            if (vertex instanceof TaskVertex) {
                TaskVertex taskVertex = ((TaskVertex) vertex);
                taskVertex.get().run();

            } else if (vertex instanceof CallableFlowVertex) {
                CallableFlowVertex flowVertex = ((CallableFlowVertex) vertex);
                flowVertex.get().onSubmit().call(getExecutor());
            }

            return vertex;
        }), new FutureCallback<Vertex>() {
            @Override
            public void onSuccess(Vertex vertex) {
                flow.onSuccess(vertex).call(getExecutor());

                CacheableFlowExecutorService.this.completing(flow);
            }

            @Override
            public void onFailure(Throwable throwable) {
                flow.onFailure(vertex, throwable).call(getExecutor());

                CacheableFlowExecutorService.this.completing(flow);
            }
        }, executor);
    }

    protected void completing(CallableFlow flow) {
        try {
            if (this.cacheClean && flow.isDone()) {
                switch (this.cacheMode) {
                    case LOCAL:
                        if (flow instanceof CacheableFlow) {
                            Files.walkFileTree(Paths.get(this.cachePath, ((CacheableFlow) flow).getExecutionId()),
                                    new DeleteDirectoryFileVisitor());
                        } else {
                            Files.walkFileTree(Paths.get(this.cachePath, flow.getId()),
                                    new DeleteDirectoryFileVisitor());
                        }

                        break;
                    case HDFS:
                        FileSystem
                                fs =
                                FileSystem.newInstance(ConfigurationClient.getInstance().getConfiguration());

                        if (flow instanceof CacheableFlow) {
                            fs.delete(new org.apache.hadoop.fs.Path(this.cachePath,
                                            ((CacheableFlow) flow).getExecutionId()),
                                    true);
                        } else {
                            fs.delete(new org.apache.hadoop.fs.Path(this.cachePath, flow.getId()), true);
                        }

                        fs.close();

                        break;
                }
            }
        } catch (IOException e) {
            System.err.println(this + " -> " + flow);
        }
    }

    @Override
    public FlowExecutorService getExecutor() {
        return this;
    }

    @Override
    public void submit(CallableFlow flow) {
        if (flow instanceof CacheableFlow) {
            Path executionPath = Paths.get(this.cachePath, ((CacheableFlow) flow).getExecutionId());

            ((CacheableFlow) flow).setCacheMode(this.cacheMode);
            ((CacheableFlow) flow).setExecutionPath(executionPath.toString());

        }
        executor.submit(() -> flow.onSubmit().call(getExecutor()));
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    public void setInitProperties(Properties properties) {
        String mode = properties.getProperty("cache.mode", "local");
        String path = properties.getProperty("cache.path", System.getProperty("java.io.tmpdir"));
        String clean = properties.getProperty("cache.clean", "true");

        if (Strings.isNullOrEmpty(mode)) {
            mode = "local";
        }

        if (Strings.isNullOrEmpty(path)) {
            path = System.getProperty("java.io.tmpdir");
        }

        if (Strings.isNullOrEmpty(clean)) {
            clean = "true";
        }

        cacheMode = CacheMode.valueOf(mode.toUpperCase());
        cachePath = Paths.get(path, ".cache").toString();
        cacheClean = Boolean.parseBoolean(clean);

    }

    class DeleteDirectoryFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return super.postVisitDirectory(dir, exc);
        }
    }
}
