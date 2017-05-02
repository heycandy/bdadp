package com.chinasofti.ark.bdadp.service.visual.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioCategoryDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;
import com.chinasofti.ark.bdadp.service.scenario.bean.CateStauts;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioStatus;
import com.chinasofti.ark.bdadp.service.visual.ScenarioVisualService;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioColumnVisual;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioCycleVisual;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioExecuteStatus;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioRadarVisual;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java8.util.Comparators;
import java8.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by White on 2016/10/20.
 */
@Service
public class ScenarioVisualServiceImpl implements ScenarioVisualService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ScenarioDao scenarioDao;

    @Autowired
    ScenarioCategoryDao scenarioCategoryDao;

    @Override
    public ScenarioCycleVisual calcCycleVisual(String startTime, String endTime) {
        Map<String, Double> usageResult = queryScenarioTotalUsageResult(startTime, endTime);
        Map<String, Double> successResult =
                queryTotalExecuteResult(ScenarioExecuteStatus.SUCCESS.ordinal(), startTime, endTime);
//    Map<String, Double> failureResult =
//        queryTotalExecuteResult(ScenarioExecuteStatus.FAILURE.ordinal(), startTime, endTime);
        int usage = (int) (java8.util.Maps.getOrDefault(usageResult, "total", 0.0) * 100.0);
        int success = (int) (java8.util.Maps.getOrDefault(successResult, "total", 0.0) * 100.0);
        int failure = success != 0 ? 100 - success : 0;

        return new ScenarioCycleVisual(usage, success, failure);
    }

    @Override
    public List<ScenarioRadarVisual> calcRadarVisual(String startTime, String endTime) {
        Map<String, Double> executeSuccess =
                queryExecuteResult(ScenarioExecuteStatus.SUCCESS.ordinal(), startTime, endTime);
        Map<String, Double> executeFailure =
                queryExecuteResult(ScenarioExecuteStatus.FAILURE.ordinal(), startTime, endTime);
        Map<String, Double> scheduleSuccess = queryScheduleSuccessResult(startTime, endTime);
        Map<String, Double> scenarioUsage = queryScenarioUsageResult(startTime, endTime);
        Map<String, Double> resourceUsage = queryResourceUsageResult(startTime, endTime);

        Iterable<Scenario>
                iterable =
                scenarioDao.findByScenarioStatusNotOrderByCreateTimeDesc(ScenarioStatus.DELETE.ordinal());

        // calc value & weight
        List<ScenarioRadarVisual> visuals = Lists.newArrayList();
        for (Scenario scenario : iterable) {
            String scenarioId = scenario.getScenarioId();

            int es = (int) (java8.util.Maps.getOrDefault(executeSuccess, scenarioId, 0.0) * 100.0);
            //int ef = (int) (executeFailure.getOrDefault(scenarioId, 0.0) * 100.0);
            int
                    ef =
                    (es != 0 ? (100 - es) : (int) (java8.util.Maps.getOrDefault(executeFailure, scenarioId, 0.0) * 100.0));
            int ss = (int) (java8.util.Maps.getOrDefault(scheduleSuccess, scenarioId, 0.0) * 100.0);
            int su = (int) (java8.util.Maps.getOrDefault(scenarioUsage, scenarioId, 0.0) * 100.0);
            int ru = (int) (java8.util.Maps.getOrDefault(resourceUsage, scenarioId, 0.0) * 100.0);

            List<Integer> value = Lists.newArrayList(ru, ef, su, ss, es);
            Double weight = es * 0.2 + ef * 0.1 + ss * 0.2 + su * 0.3 + ru * 0.2;

            visuals.add(new ScenarioRadarVisual(scenario, value, weight));
        }

        //sort by weight
        StreamSupport.stream(visuals)
                .sorted(Comparators.comparing(ScenarioRadarVisual::getWeight));

        // top5
        if (visuals.isEmpty()) {
            return visuals;
        } else {
            int toIndex = visuals.size() >= 5 ? 5 : visuals.size();
            List<ScenarioRadarVisual> tops = visuals.subList(0, toIndex);

            // reset weight
            double total = 0.0;
            for (ScenarioRadarVisual visual : tops) {
                total += visual.getWeight();
            }

            for (ScenarioRadarVisual visual : tops) {
                double weight = visual.getWeight();
                visual.setWeight(weight / total);
            }

            return tops;
        }

    }

    @Override
    public Integer calcTotalCountVisual(String startTime, String endTime) {
        Map<String, Double> map = queryTotalCount(startTime, endTime);
        return java8.util.Maps.getOrDefault(map, "total", 0.0).intValue();
    }

    @Override
    public Integer calcTotalExecuteCountVisual(String startTime, String endTime) {
        Map<String, Double> map = queryTotalExecuteCount(startTime, endTime);
        return java8.util.Maps.getOrDefault(map, "total", 0.0).intValue();
    }

    @Override
    public List calcLinearVisual(String startTime, String endTime) throws Exception {
        if (Strings.isNullOrEmpty(startTime) || Strings.isNullOrEmpty(endTime)) {
            return Lists.newArrayList();
        }

        Map<String, Double> successResult =
                queryTotalExecuteCount(ScenarioExecuteStatus.SUCCESS.ordinal(), startTime, endTime);
        Map<String, Double> failureResult =
                queryTotalExecuteCount(ScenarioExecuteStatus.FAILURE.ordinal(), startTime, endTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startTime);
        Date endDate = dateFormat.parse(endTime);

        Calendar startCal = Calendar.getInstance();
        Calendar endCalen = Calendar.getInstance();

        startCal.setTime(startDate);
        endCalen.setTime(endDate);

        SimpleDateFormat axisFormat = new SimpleDateFormat("yyyy.MM.dd");
        List<String> axis = Lists.newArrayList();
        while (!startCal.after(endCalen)) {
            Date date = startCal.getTime();
            axis.add(axisFormat.format(date));
            startCal.add(Calendar.DAY_OF_YEAR, 1);
        }

        List<List<Object>> success = Lists.newArrayList();
        List<List<Object>> failure = Lists.newArrayList();
        for (String a : axis) {
            List<Object> list0 = Lists.newArrayList(a, java8.util.Maps.getOrDefault(
                    successResult, a, 0.0).intValue());

            List<Object> list1 = Lists.newArrayList(a, java8.util.Maps.getOrDefault(
                    failureResult, a, 0.0).intValue());

            success.add(list0);
            failure.add(list1);
        }

        return Lists.newArrayList(success, failure);
    }

    @Override
    public List<ScenarioColumnVisual> calcColumnVisual(String startTime, String endTime) {
        Map<String, Double> executeSuccess =
                queryExecuteResultForColumn(ScenarioExecuteStatus.SUCCESS.ordinal(), startTime, endTime);
        Map<String, Double> executeFailure =
                queryExecuteResultForColumn(ScenarioExecuteStatus.FAILURE.ordinal(), startTime, endTime);
        Map<String, Double> scheduleSuccess = queryScheduleSuccessResultForColumn(startTime, endTime);
        Map<String, Double> scenarioUsage = queryScenarioUsageResultForColumn(startTime, endTime);
        Map<String, Double> resourceUsage = queryResourceUsageResultForColumn(startTime, endTime);

        Iterable<ScenarioCategory>
                iterable =
                scenarioCategoryDao.findByCateStatusNotOrderByCreateTimeDesc(CateStauts.DELETE.ordinal());

        // calc value & weight
        List<ScenarioColumnVisual> visuals = Lists.newArrayList();
        for (ScenarioCategory scenarioCategory : iterable) {
            String scenarioCategoryId = scenarioCategory.getCateId();

            int es = (int) (java8.util.Maps.getOrDefault(executeSuccess, scenarioCategoryId, 0.0) * 100.0);
            int
                    ef =
                    (es != 0 ? (100 - es)
                            : (int) (java8.util.Maps.getOrDefault(executeFailure, scenarioCategoryId, 0.0) * 100.0));
//            int ef = (int) (executeFailure.getOrDefault(scenarioCategoryId, 0.0) * 100.0);
            int ss = (int) (java8.util.Maps.getOrDefault(scheduleSuccess, scenarioCategoryId, 0.0) * 100.0);
            int su = (int) (java8.util.Maps.getOrDefault(scenarioUsage, scenarioCategoryId, 0.0) * 100.0);
            int ru = (int) (java8.util.Maps.getOrDefault(resourceUsage, scenarioCategoryId, 0.0) * 100.0);

            List<Integer> value = Lists.newArrayList(es, ef, ss, su, ru);
            Double weight = es * 0.2 + ef * 0.1 + ss * 0.2 + su * 0.3 + ru * 0.2;

            visuals.add(new ScenarioColumnVisual(scenarioCategory, value, weight));
        }

        //sort by weight
        StreamSupport.stream(visuals)
                .sorted(Comparators.comparing(ScenarioColumnVisual::getWeight));

        // top5
        if (visuals.isEmpty()) {
            return visuals;
        } else {
            int toIndex = visuals.size() >= 5 ? 5 : visuals.size();
            List<ScenarioColumnVisual> tops = visuals.subList(0, toIndex);

//            // reset weight
//            double total = 0.0;
//            for (ScenarioColumnVisual visual : tops) {
//                total += visual.getWeight();
//            }
//
//            for (ScenarioColumnVisual visual : tops) {
//                double weight = visual.getWeight();
//                visual.setWeight(weight / total);
//            }

            return tops;
        }

    }

    private Map<String, Double> queryScenarioTotalUsageResult(String startTime, String endTime) {
        String s = "SELECT 'total' total, s0.cnt / s1.cnt percent FROM (\n"
                + " SELECT\n"
                + "   count(*) cnt\n"
                + " FROM scenario s\n"
                + " WHERE s.scenario_status = 3 AND s.online_time >= ? AND s.online_time <= ?\n"
                + ") s0, (\n"
                + " SELECT\n"
                + "   count(*) cnt\n"
                + " FROM scenario s\n"
                + " WHERE s.scenario_status != 5\n"
                + ") s1;";

        return new QueryTransformer<String, Double>(startTime, endTime).nativeQuery(s);
    }

    private Map<String, Double> queryTotalExecuteResult(Integer executeStatus, String startTime,
                                                        String endTime) {
        String s = "SELECT 'total' total, s0.cnt / s1.cnt percent FROM (\n"
                + " SELECT\n"
                + "   count(*) cnt\n"
                + " FROM scenarioexecutehistory s\n"
                + " WHERE s.task_id = s.scenario_id and s.execute_status = ? AND s.start_time >= ? AND s.end_time <= ?\n"
                + ") s0, (\n"
                + " SELECT\n"
                + "   count(*) cnt\n"
                + " FROM scenarioexecutehistory s\n"
                + " WHERE s.task_id = s.scenario_id and s.execute_status > 1 AND s.start_time >= ? AND s.end_time <= ?\n"
                + ") s1;";

        return new QueryTransformer<String, Double>(executeStatus, startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryExecuteResult(Integer executeStatus, String startTime,
                                                   String endTime) {
        String s = "SELECT s0.scenario_id, s0.cnt / s1.total percent FROM (\n"
                + "  SELECT\n"
                + "    s.scenario_id,\n"
                + "    count(*) cnt\n"
                + "  FROM scenarioexecutehistory s\n"
                + "  WHERE s.task_id = s.scenario_id AND s.execute_status = ? AND s.start_time >= ? AND s.end_time <= ?\n"
                + "  GROUP BY s.scenario_id\n"
                + ") s0, (\n"
                + "  SELECT\n"
                + "    s.scenario_id,\n"
                + "    count(*) total\n"
                + "  FROM scenarioexecutehistory s\n"
                + "  WHERE s.task_id = s.scenario_id AND s.execute_status > 1 AND s.start_time >= ? AND s.end_time <= ?\n"
                + "  GROUP BY s.scenario_id\n"
                + ") s1 WHERE s0.scenario_id = s1.scenario_id;";

        return new QueryTransformer<String, Double>(executeStatus, startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryScheduleSuccessResult(String startTime, String endTime) {
        String s = "SELECT s0.scenario_id,s0.cnt/s1.total percent FROM\n" +
                "    (SELECT sh.scenario_id,count(sh.scenario_id)cnt FROM schedulehistory sh\n" +
                "    WHERE sh.schedule_status = 'success' AND sh.start_time >= ? AND sh.end_time <= ?\n"
                +
                "    GROUP BY sh.scenario_id)s0,\n" +
                "    (SELECT sh.scenario_id,count(sh.scenario_id)total FROM schedulehistory sh\n" +
                "    WHERE sh.start_time >= ? AND sh.end_time <= ?\n" +
                "    GROUP BY sh.scenario_id)s1\n" +
                "  WHERE s0.scenario_id = s1.scenario_id;";

        return new QueryTransformer<String, Double>(startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryScenarioUsageResult(String startTime, String endTime) {
        String s = "SELECT t0.relation_id scenario_id, t0.cnt / t1.total percent FROM (\n"
                + "  SELECT\n"
                + "    t.relation_id,\n"
                + "    count(*) cnt\n"
                + "  FROM task t\n"
                + "  WHERE t.task_type = 'scenario'\n"
                + "  GROUP BY t.relation_id\n"
                + ") t0, (\n"
                + "  SELECT\n"
                + "    count(*) total\n"
                + "  FROM task t\n"
                + "  WHERE t.task_type = 'scenario'\n"
                + ") t1;";

        return new QueryTransformer<String, Double>().nativeQuery(s);
    }

    private Map<String, Double> queryResourceUsageResult(String startTime, String endTime) {
        String s = "SELECT s0.scenario_id, s0.duration / s1.total percent FROM (\n"
                + "  SELECT\n"
                + "    s.scenario_id,\n"
                + "    sum(s.end_time - s.start_time) duration\n"
                + "  FROM scenarioexecutehistory s\n"
                + "  WHERE s.task_id = s.scenario_id AND s.execute_status > 1 AND s.start_time >= ? AND s.end_time <= ?\n"
                + "  GROUP BY s.scenario_id\n"
                + ") s0, (\n"
                + "  SELECT\n"
                + "    sum(s.end_time - s.start_time) total\n"
                + "  FROM scenarioexecutehistory s\n"
                + "  WHERE s.task_id = s.scenario_id AND s.execute_status > 1 AND s.start_time >= ? AND s.end_time <= ?\n"
                + ") s1;";

        return new QueryTransformer<String, Double>(startTime, endTime, startTime, endTime)
                .nativeQuery(s);
    }

    private Map<String, Double> queryTotalCount(String startTime, String endTime) {
        String s = "SELECT 'total' total, count(*) / 1.0 cnt \n"
                + "FROM scenario s \n"
                + "WHERE s.scenario_status != 5;";

        return new QueryTransformer<String, Double>().nativeQuery(s);
    }

    private Map<String, Double> queryTotalExecuteCount(String startTime, String endTime) {
        String s = "SELECT 'total' total, count(*) / 1.0 cnt\n"
                + "FROM scenarioexecutehistory s\n"
                + "WHERE s.task_id = s.scenario_id;";

        return new QueryTransformer<String, Double>().nativeQuery(s);
    }

    private Map<String, Double> queryTotalExecuteCount(Integer executeStatus, String startTime,
                                                       String endTime) {
        String s = "SELECT DATE_FORMAT(s.end_time, '%Y.%m.%d') axis, count(*) / 1.0 cnt\n"
                + "FROM scenarioexecutehistory s\n"
                + "WHERE s.task_id = s.scenario_id AND s.execute_status = ? AND s.start_time >= ? AND s.end_time <= ?\n"
                + "GROUP BY DATE_FORMAT(s.end_time, '%Y%m%d');";

        return new QueryTransformer<String, Double>(executeStatus, startTime, endTime).nativeQuery(s);
    }

    private Map<String, Double> queryExecuteResultForColumn(Integer executeStatus, String startTime,
                                                            String endTime) {
        String s = "SELECT sr.cate_id,avg(percent) successRate FROM\n" +
                "  (SELECT s0.cate_id cate_id,s0.scenario_id,s0.cnt / s1.total percent FROM\n" +
                "    ((SELECT scd.cate_id cate_id,scd.scenario_id scenario_id,count(scd.scenario_id) cnt FROM scenariocategorydetail scd  LEFT JOIN scenarioexecutehistory seh ON scd.scenario_id=seh.scenario_id\n"
                +
                "    WHERE seh.task_id = seh.scenario_id AND seh.execute_status = ? AND seh.start_time >= ?\n"
                +
                "          AND seh.end_time <= ?\n" +
                "    GROUP BY scd.scenario_id,scd.cate_id)s0,\n" +
                "      (SELECT scd.cate_id cate_id,scd.scenario_id scenario_id,count(scd.scenario_id) total FROM scenariocategorydetail scd  LEFT JOIN scenarioexecutehistory seh ON scd.scenario_id=seh.scenario_id\n"
                +
                "      WHERE seh.task_id = seh.scenario_id AND seh.execute_status >1 AND seh.start_time >= ? AND seh.end_time <= ?\n"
                +
                "      GROUP BY scd.scenario_id,scd.cate_id)s1)\n" +
                "  WHERE s0.scenario_id = s1.scenario_id AND s0.cate_id = s1.cate_id\n" +
                "  )sr\n" +
                "GROUP BY sr.cate_id; ";
        return new QueryTransformer<String, Double>(executeStatus, startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryScheduleSuccessResultForColumn(String startTime,
                                                                    String endTime) {
        String s = "SELECT er.cate_id,avg(percent) exeRate FROM\n" +
                "  (SELECT s0.cate_id,s0.scenario_id,s0.cnt/s1.total percent FROM\n" +
                "    (SELECT scd.scenario_id,scd.cate_id,count(scd.scenario_id) cnt FROM scenariocategorydetail scd LEFT JOIN schedulehistory sh ON sh.scenario_id = scd.scenario_id\n"
                +
                "    WHERE sh.schedule_status = 'success' AND sh.start_time >= ? AND sh.end_time <= ?\n"
                +
                "    GROUP BY scd.scenario_id,scd.cate_id)s0,\n" +
                "    (SELECT scd.cate_id,scd.scenario_id,count(scd.scenario_id) total FROM scenariocategorydetail scd LEFT JOIN schedulehistory sh ON sh.scenario_id = scd.scenario_id\n"
                +
                "    WHERE sh.start_time >= ? AND sh.end_time <= ?\n" +
                "    GROUP BY scd.scenario_id,scd.cate_id)s1\n" +
                "  WHERE s0.scenario_id = s1.scenario_id AND s0.cate_id = s1.cate_id)er\n" +
                "GROUP BY er.cate_id;";

        return new QueryTransformer<String, Double>(startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryScenarioUsageResultForColumn(String startTime, String endTime) {
        String s = "SELECT su.cate_id,avg(su.percent) usageRate FROM\n" +
                "  (SELECT s0.cate_id cate_id,s0.scenario_id,s0.cnt/s1.total percent FROM\n" +
                "    (SELECT scd.cate_id cate_id,scd.scenario_id scenario_id,count(scd.scenario_id) cnt FROM (scenariocategorydetail scd LEFT JOIN task t ON scd.scenario_id = t.relation_id)\n"
                +
                "      LEFT JOIN scenarioexecutehistory seh ON seh.scenario_id = scd.scenario_id WHERE seh.start_time >= ? AND seh.end_time <= ? AND t.task_type = 'scenario'\n"
                +
                "    GROUP BY scd.scenario_id,scd.cate_id)s0,\n" +
                "    (SELECT scd.cate_id cate_id,scd.scenario_id scenario_id,count(scd.scenario_id) total FROM(scenariocategorydetail scd LEFT JOIN task t ON scd.scenario_id = t.relation_id)\n"
                +
                "      LEFT JOIN scenarioexecutehistory seh ON seh.scenario_id = scd.scenario_id WHERE seh.start_time >= ? AND seh.end_time <= ? AND t.task_type = 'scenario'\n"
                +
                "    GROUP BY scd.cate_id)s1\n" +
                "  WHERE s0.cate_id = s1.cate_id)su\n" +
                "GROUP BY su.cate_id;";

        return new QueryTransformer<String, Double>(startTime, endTime, startTime,
                endTime).nativeQuery(s);
    }

    private Map<String, Double> queryResourceUsageResultForColumn(String startTime, String endTime) {
        String s = "SELECT ru.cate_id,avg(ru.percent) resourceRate FROM\n" +
                "  (SELECT s0.cate_id cate_id,s0.scenario_id,s0.cnt/s1.total percent FROM\n" +
                "    (SELECT scd.cate_id,scd.scenario_id,sum(seh.end_time - seh.start_time) cnt\n" +
                "     FROM scenariocategorydetail scd LEFT JOIN scenarioexecutehistory seh ON scd.scenario_id = seh.scenario_id\n"
                +
                "     WHERE seh.task_id = seh.scenario_id AND seh.execute_status > 1 AND seh.start_time >= ? AND seh.end_time <= ?\n"
                +
                "     GROUP BY scd.scenario_id,scd.cate_id)s0,\n" +
                "    (SELECT scd.cate_id,sum(seh.end_time - seh.start_time) total\n" +
                "     FROM scenariocategorydetail scd LEFT JOIN scenarioexecutehistory seh ON scd.scenario_id = seh.scenario_id\n"
                +
                "     WHERE seh.task_id = seh.scenario_id AND seh.execute_status > 1 AND seh.start_time >= ? AND seh.end_time <= ?\n"
                +
                "     GROUP BY scd.cate_id)s1\n" +
                "  WHERE s0.cate_id = s1.cate_id)ru\n" +
                "GROUP BY ru.cate_id;";

        return new QueryTransformer<String, Double>(startTime, endTime, startTime, endTime)
                .nativeQuery(s);
    }

    class QueryTransformer<K, V> {

        private final Object[] parameters;

        QueryTransformer(Object... parameters) {
            this.parameters = parameters;
        }

        private Map<String, Double> nativeQuery(String s) {
            Map<String, Double> map = Maps.newHashMap();

            Query query = entityManager.createNativeQuery(s);

            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i + 1, parameters[i]);
            }

            List results = query.getResultList();
            for (Object result : results) {
                Object[] columns = (Object[]) result;

                Object key = columns[0];
                Object value = columns[1];

                if (value instanceof BigDecimal) {
                    map.put((String) key, ((BigDecimal) value).doubleValue());
                } else if (value instanceof Double) {
                    map.put((String) key, (Double) value);
                }

            }

            return map;
        }

    }
}
