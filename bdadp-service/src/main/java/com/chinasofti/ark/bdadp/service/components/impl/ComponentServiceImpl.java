package com.chinasofti.ark.bdadp.service.components.impl;

import com.chinasofti.ark.bdadp.component.ComponentLoader;
import com.chinasofti.ark.bdadp.component.ParamConfig;
import com.chinasofti.ark.bdadp.component.ParamOption;
import com.chinasofti.ark.bdadp.component.support.ComponentManger;
import com.chinasofti.ark.bdadp.dao.components.ComponentDao;
import com.chinasofti.ark.bdadp.entity.components.Component;
import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.service.components.ComponentStatus;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by White on 2016/09/14.
 */

public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentDao dao;

    private ComponentManger manger;

    @Override
    public void setInitProperties(Properties props) {
        System.out.printf("component.service.path=%s\n", props.getProperty("path", "components"));

        String path = props.getProperty("path", "components");
        if (Strings.isNullOrEmpty(path) || Files.notExists(Paths.get(path))) {
            System.out.printf("user.components.dir=%s\n", System.getProperty("user.components.dir"));

            String userDir = System.getProperty("user.components.dir");
            if (!Strings.isNullOrEmpty(userDir) && Files.exists(Paths.get(userDir))) {
                path = Paths.get(userDir).toString();
            } else {
                System.out.printf("user.dir=%s\n", System.getProperty("user.dir"));

                userDir = System.getProperty("user.dir");
                if (!Strings.isNullOrEmpty(userDir) && userDir.endsWith("bin")) {
                    if (Files.exists(Paths.get(userDir.replace("bin", "webapps/ROOT")))) {
                        path = Paths.get(userDir.replace("bin", "webapps/ROOT/components")).toString();
                    }
                } else if (!Strings.isNullOrEmpty(userDir)) {
                    if (Files.exists(Paths.get(userDir, "webapps/ROOT"))) {
                        path = Paths.get(userDir, "webapps/ROOT/components").toString();
                    }
                } else {
                    System.err.println("error component service path not found.");
                }
            }
        }

        manger = new ComponentManger(path);

        List<Component> list = new ArrayList<>();
        for (ComponentLoader loader : manger.getLoaders().values()) {
            Component config = new Component();
            config.setComponentId(loader.getId());
            config.setComponentBase(true);
            config.setComponentDesc(loader.getDesc());
            config.setComponentName(loader.getName());
            config.setComponentPid(loader.getPid());
            config.setComponentType(loader.getClazz());
            config.setComponentStatus(0);
            list.add(config);
        }

        dao.save(list);

    }

    @Override
    public Iterable<Component> getComponentsBase(String language) {
        String languageTag = Splitter.on(",").split(language).iterator().next();
        Locale locale = Locale.forLanguageTag(languageTag);

        List<Component> list = new ArrayList<>();
        for (ComponentLoader loader : manger.getLoaders().values()) {
            ResourceBundle bundle = loader.getResourceBundle(locale);

            Component config = new Component();

            config.setComponentId(loader.getId());

            try {
                config.setComponentDesc(bundle.getString(loader.getDesc()));
            } catch (MissingResourceException e) {
                config.setComponentDesc(loader.getDesc());
            }

            try {
                config.setComponentName(bundle.getString(loader.getName()));
            } catch (MissingResourceException e) {
                config.setComponentName(loader.getName());
            }

            config.setComponentPid(loader.getPid());

            list.add(config);
        }

        return list;
    }

    @Override
    public Iterable<Component> getComponentsBusiness(String language) {
        return dao.findByComponentBaseFalseAndComponentStatusNot(ComponentStatus.INVALID.ordinal());
    }

    @Override
    public Component findComponentById(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<ComponentConfig> findComponentConfigs(String componentId) {
        return null;
    }

    @Override
    public Iterable<ComponentConfig> findComponentConfigs(String componentId, String language) {
        String languageTag = Splitter.on(",").split(language).iterator().next();
        Locale locale = Locale.forLanguageTag(languageTag);

        ComponentLoader loader = manger.getLoader(componentId);
        List<ComponentConfig> list = new ArrayList<>();
        if (loader != null) {
            ResourceBundle bundle = loader.getResourceBundle(locale);

            for (int i = 0; i < loader.getParamConfig().size(); i++) {
                ParamConfig paramConfig = loader.getParamConfig().get(i);

                ComponentConfig componentConfig = new ComponentConfig();

                componentConfig.setComponentId(componentId);

                List<String> defaultOptions = Lists.newArrayList();
                if (paramConfig.getType().equalsIgnoreCase(ParamConfig.ParamType.EXPR.name())) {
                    ObjectMapper mapper = new ObjectMapper();

                    for (ParamOption paramOption : paramConfig.getDefaultOptions()) {
                        ParamOption option = new ParamOption();

                        option.setArgs(paramOption.getArgs());
                        try {
                            option.setCate(bundle.getString(paramOption.getCate()));
                        } catch (MissingResourceException e) {
                            option.setCate(paramOption.getCate());
                        }

                        try {
                            option.setDesc(bundle.getString(paramOption.getDesc()));
                        } catch (MissingResourceException e) {
                            option.setDesc(paramOption.getDesc());
                        }

                        option.setName(paramOption.getName());

                        try {
                            String optionString = mapper.writeValueAsString(option);

                            defaultOptions.add(optionString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    for (ParamOption paramOption : paramConfig.getDefaultOptions()) {
                        StringBuilder builder = new StringBuilder();
                        try {
                            builder.append(bundle.getString(paramOption.getDesc()));
                        } catch (MissingResourceException e) {
                            builder.append(paramOption.getDesc());
                        }
                        builder.append("=");
                        builder.append(paramOption.getVal());

                        defaultOptions.add(builder.toString());

                    }
                }

                componentConfig.setDefaultOptions(Joiner.on(",").join(defaultOptions));
                componentConfig.setDefaultValue(paramConfig.getDefaultVal());
                componentConfig.setNullable(paramConfig.getNullable());
                componentConfig.setOrderId(i);

                try {
                    componentConfig.setParamDesc(bundle.getString(paramConfig.getDesc()));
                } catch (MissingResourceException e) {
                    componentConfig.setParamDesc(paramConfig.getDesc());
                }

                componentConfig.setParamId(paramConfig.getId());
                componentConfig.setParamName(paramConfig.getName());
                componentConfig.setParamType(paramConfig.getType());

                list.add(componentConfig);
            }
        }

        return list;

    }

    @Override
    public InputStream getComponentIcon(String id, String param) {
        if (manger.getLoader(id) != null) {
            return manger.getLoader(id).getIcon(param);
        }
        return null;
    }

    @Override
    public String getComponentIconAsString(String id, String param) {
        if (manger.getLoader(id) != null) {
            try {
                return manger.getLoader(id).getIconAsString(param);
            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("get icon as string, id: %s; param: %s", id, param), e);
            }
        }
        return null;
    }

    @Override
    public Class loadComponentClass(String id) {
        try {
            return manger.getLoader(id).loadClass();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    String.format("load component as class, id: %s", id), e);
        }
    }

    @Override
    public Iterable<String> getJarFiles() {
        return StreamSupport.stream(manger.getLoaders().values())
                .map(ComponentLoader::getJarFile)
                .map(File::toURI)
                .map(URI::toString)
                .collect(Collectors.toList());
    }
}
