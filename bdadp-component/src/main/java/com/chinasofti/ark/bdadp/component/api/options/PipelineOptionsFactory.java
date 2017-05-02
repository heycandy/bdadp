package com.chinasofti.ark.bdadp.component.api.options;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by White on 2017/1/4.
 */
public class PipelineOptionsFactory {

    public static PipelineOptions create() {
        return new Builder().as(PipelineOptions.class);
    }

    public static <T extends PipelineOptions> T as(Class<T> klass) {
        return new Builder().as(klass);
    }

    public static Builder fromArgs(String[] args) {
        return new Builder().fromArgs(args);
    }

    public static Builder fromProperties(Properties properties) {
        return new Builder().fromProperties(properties);
    }

    public static Builder fromSettings(Map<String, String> setting) {
        return new Builder().fromSettings(setting);
    }

    public Builder withValidation() {
        return new Builder().withValidation();
    }

    public static class Builder {

        private static final Set<String> PIPELINE_OPTIONS_FACTORY_CLASSES =
                ImmutableSet.of(PipelineOptionsFactory.class.getName(), Builder.class.getName());
        private final Map<String, String> settings;
        private final boolean validation;
        private final boolean strictParsing;

        private Builder() {
            this(null, false, true);
        }

        private Builder(Map<String, String> settings, boolean validation,
                        boolean strictParsing) {
            if (settings == null) {
                this.settings = ImmutableMap.<String, String>builder().build();
            } else {
                this.settings = settings;
            }

            this.validation = validation;
            this.strictParsing = strictParsing;
        }

        public <T extends PipelineOptions> T as(Class<T> klass) {
            try {
                if (this.settings == null) {
                    return klass.newInstance();
                } else {
                    Constructor<T> constructor = klass.getConstructor(Map.class);

                    return constructor.newInstance(new Object[]{this.settings});
                }


            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }

        public Builder fromArgs(String[] args) {
            checkNotNull(args, "Arguments should not be null.");
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (String arg : args) {
                if (arg.contains("=")) {
                    String[] x = arg.trim().split("=");
                    builder.put(x[0], x[1]);
                }
            }
            return fromSettings(builder.build());
        }

        public Builder fromProperties(Properties properties) {
            checkNotNull(properties, "Arguments should not be null.");
            return fromSettings(Maps.fromProperties(properties));
        }

        public Builder fromSettings(Map<String, String> map) {
            checkNotNull(map, "Arguments should not be null.");
            Map<String, String> newSettings = Maps.newConcurrentMap();
            newSettings.putAll(settings);
            newSettings.putAll(map);
            return new Builder(newSettings, validation, strictParsing);
        }

        public Builder withValidation() {
            return new Builder(settings, true, strictParsing);
        }

        public Builder withoutStrictParsing() {
            return new Builder(settings, validation, false);
        }

        public PipelineOptions create() {
            return as(PipelineOptions.class);
        }

    }
}
