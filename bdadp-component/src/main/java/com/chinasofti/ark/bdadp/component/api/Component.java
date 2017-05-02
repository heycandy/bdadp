package com.chinasofti.ark.bdadp.component.api;

/**
 * This interface defines a Raw Task interface. Each task defines <ul> <li>Task Type : {HADOOP,
 * UNIX, JAVA, SUCCESS_TEST, CONTROLLER}</li> <li>Task ID/Name : {String}</li> <li>Arguments:
 * Key/Value Map for Strings</li> </ul>
 * <p>
 * A task is required to have a constructor Task(String taskId)
 * <p>
 * Created by White on 2016/09/04.
 */
public interface Component {

    /**
     * Returns a unique string id for the Task.
     */
    String getId();

    /**
     * Returns a string name for the Task.
     */
    String getName();

    /**
     * Returns a progress report between [0 - 1.0] to indicate the percentage complete
     *
     * @throws Exception If getting progress fails
     */
    double getProgress() throws Exception;

}
