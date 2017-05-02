package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.ComponentLoader;
import com.chinasofti.ark.bdadp.component.api.Component;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by White on 2016/09/03.
 */
public class ComponentManger {

    private final String parent;
    private Map<String, Class<? extends Component>> map = Maps.newHashMap();
    private Map<String, ComponentLoader> loaders = Maps.newHashMap();

    public ComponentManger(String parent) {
        this.parent = parent;

        if (!Strings.isNullOrEmpty(parent) && Files.exists(Paths.get(parent))) {
            loadDefault();
            loadUsage();
        } else {
            System.out.printf("path:%s is empty or not exists.\n", parent);
        }

    }

    public static void main(String[] args) {
        new ComponentManger("D:\\ideaProject\\ark-bdadp\\bdadp-misc\\components");
    }

    private void loadDefault() {
        File parentDir = new File(this.parent);
        File versignDir = new File(parentDir, ".versign");

        if (!versignDir.exists() && versignDir.mkdir()) {
            System.out.println(versignDir);
        }

        File[] componentDirs = parentDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && !pathname.getName().equals(".versign");
            }
        });

        for (int i = 0; i < componentDirs.length; i++) {
            File dir = componentDirs[i];

            File[] jars = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".jar");
                }
            });

            for (File jarFile : jars) {
                System.out.printf("load component resource > " + jarFile);

                try {
                    ComponentLoader
                            loader =
                            new ComponentLoader(dir.getName(), jarFile.getName(), versignDir, jarFile);

                    System.out.printf(" > > >");

                    loader.load();

                    System.out.printf(" > > >");

                    Assert.notNull(loader.getId(), "failed");
                    if (this.loaders.containsKey(loader.getId())) {
                        this.loaders.get(loader.getId()).close();
                        this.loaders.remove(loader.getId());
                    }
                    loaders.put(loader.getId(), loader);

                    System.out.printf(" > > >");

                    System.out.println(" OK.");

                } catch (Exception e) {
                    System.err.println(" > " + e.getMessage());
                }

            }
        }

        System.out.println();

    }

    private void loadUsage() {

    }

    public Map<String, Class<? extends Component>> getMap() {
        return map;
    }

    public Map<String, ComponentLoader> getLoaders() {
        return loaders;
    }

    public ComponentLoader getLoader(String componentId) {
        return getLoaders().get(componentId);
    }

    public Class<? extends Component> getClassByName(String name) {
        return getMap().get(name);
    }

}
