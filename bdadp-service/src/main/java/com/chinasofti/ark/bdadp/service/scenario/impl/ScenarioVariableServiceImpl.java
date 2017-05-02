package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioUserVariableDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioVariableDao;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioUserVariable;
import com.chinasofti.ark.bdadp.expression.support.ArkConversionUtil;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioVariableService;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by White on 2016/09/12.
 */
@Service
public class ScenarioVariableServiceImpl implements ScenarioVariableService {

    @Autowired
    private ScenarioVariableDao variableDao;

    @Autowired
    private ScenarioUserVariableDao userVariableDao;


    @Override
    public ScenarioUserVariable create(ScenarioUserVariable s) {
//		s.setVariableId(UUID.randomUUID().toString());

        return userVariableDao.save(s);
    }

    @Override
    public Iterable<Object> findByScenarioId(String scenarioId) {
        List<Object> iterable = new ArrayList<>();

        iterable.addAll(variableDao.findAll());
        iterable.addAll(userVariableDao.findByScenarioId(scenarioId));

        return iterable;
    }

    @Override
    public ScenarioUserVariable update(ScenarioUserVariable s) {
        s.setModifiedTime(new Date());

        ScenarioUserVariable target = userVariableDao.findOne(s.getVariableId());

        BeanHelper.mergeProperties(s, target);

        return target;
    }

    @Override
    public void delete(String id) {
        userVariableDao.delete(id);
    }

    @Override
    public String parseVariable(String inputVariable, String scenarioId) {

        String result;
        List<ScenarioUserVariable> userVarables = userVariableDao.findByScenarioId(scenarioId);
        for (int i = 0, len = userVarables.size(); i < len; i++) {
            ScenarioUserVariable bean = userVarables.get(i);
            String variableName = bean.getVariableName();
            if (inputVariable.contains("#" + variableName)) {
                inputVariable = inputVariable.replace("#" + variableName, bean.getVariableExpr());
//				result = conversionService.parseVariableByDefined(inputVariable);
            }

        }
        ArkConversionUtil conversionUtil = new ArkConversionUtil();
        result = conversionUtil.parseVariableByDefined(inputVariable);
        return result;
    }

//	public static void main(String[] args) {
//		ScenarioVariableServiceImpl s = new ScenarioVariableServiceImpl();
//		String ss = s.parseVariable("今天是, #{#someday }", "w");
//		System.out.println(ss);
//	}
}
