package com.chinasofti.ark.bdadp.entity.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by water on 2017.5.9.
 */
@Entity
public class Holiday implements Serializable {


    @Id
    @Column(name = "holiday_id")
    @JsonProperty("holiday_id")
    private Integer holidayId;

    @Column(name = "holiday_value")
    @JsonProperty("holiday_value")
    private String holidayValue;

    public Integer getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Integer holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayValue() {
        return holidayValue;
    }

    public void setHolidayValue(String holidayValue) {
        this.holidayValue = holidayValue;
    }
}
