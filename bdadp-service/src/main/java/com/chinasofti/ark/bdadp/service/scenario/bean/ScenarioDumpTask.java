package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.service.queue.bean.AbstractQueueTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Created by White on 2016/11/17.
 */
public abstract class ScenarioDumpTask extends AbstractQueueTask {

    protected final Collection<String> collection;
    protected final Path path;
    private final String createUser;
    protected double progress;

    public ScenarioDumpTask(
            String id, String name, Collection<String> collection, Path path, String createUser) {
        super(id, name);

        this.collection = collection;
        this.path = path;

        this.createUser = createUser;
    }

    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Override
    public double getProgress() {
        return this.progress;
    }

    protected void setProgress(double progress) {
        this.progress += progress;

        super.reportAll();
    }

    @Override
    public boolean remove() {
        try {
            return Files.deleteIfExists(path) &&
                    Files.deleteIfExists(path.getParent()) &&
                    Files.deleteIfExists(path.getParent().getParent());
        } catch (IOException e) {
            return false;
        }
    }
}
