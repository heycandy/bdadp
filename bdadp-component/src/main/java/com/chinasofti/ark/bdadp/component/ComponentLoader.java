package com.chinasofti.ark.bdadp.component;

import com.chinasofti.ark.bdadp.util.common.UUID;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by White on 2016/10/31.
 */
public class ComponentLoader extends URLClassLoader {

    final String baseName;
    final String fullName;

    final File versign;
    final File jarFile;

    final ComponentConfig conf;

    public ComponentLoader(String baseName, String fullName, File versign, File jarFile) {
        super(new URL[]{}, Thread.currentThread().getContextClassLoader());

        this.baseName = baseName;
        this.fullName = fullName;
        this.versign = versign;
        this.jarFile = jarFile;

        this.conf = new ComponentConfig();

    }

    public File getJarFile() {
        return this.jarFile;
    }

    public String getVersion() {
        return this.fullName
                .substring(this.baseName.length() + 1, this.fullName.length() - ".jar".length());
    }

    public String getId() {
        return this.conf.getId();
    }

    public String getName() {
        return this.conf.getName();
    }

    public String getDesc() {
        return this.conf.getDesc();
    }

    public String getClazz() {
        return this.conf.getClazz();
    }

    public ComponentIcon getIcon() {
        return this.conf.getIcon();
    }

    public InputStream getIcon(String param) {
        switch (param) {
            case ComponentIcon.ICON_XL:
                return super.getResourceAsStream("icon/" + this.getIcon().getXl());
            case ComponentIcon.ICON_XS:
                return super.getResourceAsStream("icon/" + this.getIcon().getXs());
            default:
                return super.getResourceAsStream("icon/" + this.getIcon().getXl());
        }

    }

    public String getIconAsString(String param) throws IOException {
        return "data:image/png;base64" +
                Base64.getEncoder().encodeToString(ByteStreams.toByteArray(this.getIcon(param)));
    }

    public String getPid() {
        return this.conf.getPid();
    }

    public List<ParamConfig> getParamConfig() {
        return this.conf.getParams();
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("i18n", locale, this);
    }

    public Class loadClass() throws ClassNotFoundException {
        return this.loadClass(this.getClazz());
    }

    public void load() throws Exception {
        super.addURL(this.jarFile.toURI().toURL());

        File ver0 = new File(this.versign, this.baseName + ".versigin");
        List<String> vers;
        if (ver0.exists()) {
            vers = Files.readLines(ver0, Charset.defaultCharset());
        } else {
            vers = Lists.newArrayList();
        }

        if (vers.size() == 0) {
            vers.add(UUID.getId());
        }

        InputStream in = super.getResourceAsStream("config.xml");

        Element element = new SAXReader().read(in).getRootElement();

        in.close();

        conf.setId(vers.get(0));
        conf.setName(element.elementTextTrim(ComponentConfig.COMPONENT_NAME));
        conf.setDesc(element.elementTextTrim(ComponentConfig.COMPONENT_DESC));
        conf.setClazz(element.elementTextTrim(ComponentConfig.COMPONENT_CLASS));
        conf.setPid(element.elementTextTrim(ComponentConfig.COMPONENT_PID));

        ComponentIcon icon = new ComponentIcon();
        String[] array = element.elementTextTrim(ComponentConfig.COMPONENT_ICON).split(",|;");
        if (array.length > 1) {
            icon.setXl(array[0]);
            icon.setXs(array[1]);
        } else {
            icon.setXl(array[0]);
            icon.setXs(array[0]);
        }

        conf.setIcon(icon);

        List<ParamConfig> params = Lists.newArrayList();
        List elements = element.element(ComponentConfig.COMPONENT_PARAMS).elements(ParamConfig.PARAM);
        for (int i = 0; i < elements.size(); i++) {
            Element e0 = (Element) elements.get(i);

            ParamConfig param = new ParamConfig();

            if (vers.size() <= i + 1) {
                vers.add(UUID.getId());
            }

            param.setId(vers.get(i + 1));
            param.setName(e0.elementTextTrim(ParamConfig.NAME));
            param.setDesc(e0.elementTextTrim(ParamConfig.DESC));
            param.setType(ParamConfig.ParamType.newValueOf(e0.elementTextTrim(ParamConfig.TYPE)));
            param.setNullable(e0.elementTextTrim(ParamConfig.NULLABLE).equals("true"));

            List<ParamOption> defaultOptions = Lists.newArrayList();
            if (e0.element(ParamConfig.DEFAULT_OPTIONS) != null) {
                for (Object o1 : e0.element(ParamConfig.DEFAULT_OPTIONS).elements(ParamOption.OPTION)) {
                    if (o1 instanceof Element) {
                        Element e1 = (Element) o1;

                        ParamOption option = new ParamOption();

                        option.setName(e1.attributeValue(ParamOption.NAME));
                        option.setDesc(e1.attributeValue(ParamOption.DESC));
                        option.setVal(e1.attributeValue(ParamOption.VALUE));
                        option.setCate(e1.attributeValue(ParamOption.CATE));

                        List<ParamArgument> args = Lists.newArrayList();
                        if (e1.element(ParamOption.ARGS) != null) {
                            for (Object o2 : e1.element(ParamOption.ARGS).elements(ParamArgument.ARG)) {
                                Element e2 = (Element) o2;

                                ParamArgument argument = new ParamArgument();

                                argument.setName(e2.attributeValue(ParamArgument.NAME));
                                argument.setDesc(e2.attributeValue(ParamArgument.DESC));
                                argument.setVal(e2.attributeValue(ParamArgument.VALUE));
                                argument.setDataType(e2.attributeValue(ParamArgument.DATA_TYPE));

                                args.add(argument);
                            }
                        }

                        option.setArgs(args);

                        defaultOptions.add(option);
                    }
                }
            }

            param.setDefaultOptions(defaultOptions);
            param.setDefaultVal(e0.elementTextTrim(ParamConfig.DEFAULT_VAL));

            params.add(param);
        }

        StringBuilder builder = new StringBuilder();
        for (String s : vers) {
            builder.append(s);
            builder.append("\n");
        }
        Files.write(builder.toString(), ver0, Charset.defaultCharset());

        conf.setParams(params);

    }

}
