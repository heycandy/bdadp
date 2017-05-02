package com.chinasofti.ark.bdadp.component.api.options;

import com.google.common.collect.ImmutableList;

/**
 * A registrar containing the default SDK options.
 */
public class DefaultPipelineOptionsRegistrar implements PipelineOptionsRegistrar {

    @Override
    public Iterable<Class<? extends PipelineOptions>> getPipelineOptions() {
        return ImmutableList.<Class<? extends PipelineOptions>>builder()
                .add(PipelineOptions.class)
                .add(ScenarioOptions.class)
                .add(StreamingOptions.class)
                .build();
    }
}
