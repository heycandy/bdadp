package com.chinasofti.ark.bdadp.entity.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by White on 2016/10/15.
 */

@Embeddable
public class ScenarioExecuteHistoryPK implements Serializable {

    @Column(name = "execution_id")
    @JsonProperty("execution_id")
    private String executionId;

    @Column(name = "task_id")
    @JsonProperty("task_id")
    private String taskId;

    public ScenarioExecuteHistoryPK() {
    }

    public ScenarioExecuteHistoryPK(String executionId, String taskId) {
        this.executionId = executionId;
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((executionId == null) ? 0 : executionId.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ScenarioExecuteHistoryPK other = (ScenarioExecuteHistoryPK) obj;
        if (executionId == null) {
            if (other.executionId != null) {
                return false;
            }
        } else if (!executionId.equals(other.executionId)) {
            return false;
        }
        if (taskId == null) {
            if (other.taskId != null) {
                return false;
            }
        } else if (!taskId.equals(other.taskId)) {
            return false;
        }

        return true;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
