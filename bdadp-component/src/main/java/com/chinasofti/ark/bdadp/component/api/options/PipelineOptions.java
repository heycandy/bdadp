package com.chinasofti.ark.bdadp.component.api.options;

public interface PipelineOptions extends StreamingOptions {

    /**
     * Transforms this object into an object of type {@code <T>} saving each property that has been
     * manipulated. {@code <T>} must extend {@link PipelineOptions}.
     * <p>
     * <p>If {@code <T>} is not registered with the {@link PipelineOptionsFactory}, then we attempt to
     * verify that {@code <T>} is composable with every interface that this instance of the {@code
     * PipelineOptions} has seen.
     *
     * @param kls The class of the type to transform to.
     * @return An object of type kls.
     */
    <T extends PipelineOptions> T as(Class<T> kls);

    String getExecutionId();

    PipelineOptions setExecutionId(String executionId);

}
