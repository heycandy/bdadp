package com.chinasofti.ark.bdadp.util.dto;

import java.util.List;

/**
 * @Author : water
 * @Date : 2016年10月26日
 * @Desc : TODO
 * @version: V1.0
 */
public class PageDTO {

    private Long total;

    private String graphRaw;

    private List<MonitorDTO> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<MonitorDTO> getRows() {
        return rows;
    }

    public void setRows(List<MonitorDTO> rows) {
        this.rows = rows;
    }

    public String getGraphRaw() {
        return graphRaw;
    }

    public void setGraphRaw(String graphRaw) {
        this.graphRaw = graphRaw;
    }

}
