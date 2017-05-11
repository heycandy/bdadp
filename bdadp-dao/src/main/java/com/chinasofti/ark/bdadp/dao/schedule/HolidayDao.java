package com.chinasofti.ark.bdadp.dao.schedule;

import com.chinasofti.ark.bdadp.entity.schedule.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by water on 2017.5.9.
 */
public interface HolidayDao extends JpaRepository<Holiday, Integer> {

    public Holiday findByHolidayValue(String holidayValue);

}

