package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.queue.bean.Previewable;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * Created by White on 2016/11/17.
 */
public class ScenarioImportTask extends ScenarioDumpTask implements Previewable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScenarioDao scenarioDao = ServiceContext.getService(ScenarioDao.class);

    private ScenarioGraphDagService scenarioGraphDagService
            = ServiceContext.getService(ScenarioGraphDagService.class);

    public ScenarioImportTask(String id, String name, Collection<String> collection, Path path,
                              String createUser) {
        super(id, name, collection, path, createUser);
    }

    @Override
    public String getAction() {
        return "scenario.import";
    }

    @Override
    public void run() {
        double progress = 1.0 / (5 + 1);

        InputStream in = null;
        ArchiveInputStream ais = null;
        ObjectInputStream ois = null;
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            this.setProgress(progress);
            logger.debug("open compressed file.");
            in = Files.newInputStream(path);
            ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);

            this.setProgress(progress);
            logger.debug("decompress dependency tbl.");
            ais.getNextEntry();

            ois = new ObjectInputStream(ais);

            Object obj0 = ois.readObject();

            this.setProgress(progress);
            logger.debug("decompress package dat.");
            ais.getNextEntry();

            Object obj1 = ois.readObject();
            Object obj2 = ois.readObject();

            ScenarioServiceAssert.nonExistsScenario(obj1 == null);
            ScenarioServiceAssert.nonExistsGraph(obj2 == null);

            if (logger.isDebugEnabled()) {
                logger.warn(
                        "If u see this statement but not in dev mode, please set logger level to [INFO].");
                Thread.sleep(10000);
            }

            logger.debug("transform dependency tbl.");
            ScenarioDepsTable tbl = (ScenarioDepsTable) obj0;

            logger.debug("transform package data.");
            Collection<Scenario> scenarios = (Collection<Scenario>) obj1;
            Collection<ScenarioGraphDAG> graphs = (Collection<ScenarioGraphDAG>) obj2;

            this.setProgress(progress);
            logger.debug("analysis scenario dependency.");
            Map<String, Collection<String>> scenarioDeps = Maps.newHashMap();
            for (ScenarioDepsRow row : tbl.getPreviewRows()) {
                String key = row.getScenarioId();
                Collection<String> value = StreamSupport.stream(row.getScenarioDeps())
                        .map(ScenarioDepsRow::getScenarioId)
                        .collect(Collectors.toSet());
                scenarioDeps.put(key, value);
            }

            Collection<Scenario> impScenarios = Sets.newHashSet();
            Collection<ScenarioGraphDAG> impGraphs = Sets.newHashSet();

            for (Map.Entry<String, Collection<String>> entry : scenarioDeps.entrySet()) {
                String id = entry.getKey();
                Collection<String> deps = entry.getValue();
                if (collection.contains(id)) {
                    StreamSupport.stream(scenarios)
                            .filter(s -> s.getScenarioId().equals(id) || deps.contains(s.getScenarioId()))
                            .forEach(impScenarios::add);
                    StreamSupport.stream(graphs)
                            .filter(s -> s.getScenarioId().equals(id) || deps.contains(s.getScenarioId()))
                            .forEach(impGraphs::add);
                }
            }

            this.setProgress(progress);
            logger.debug("start import data.");

            StreamSupport.stream(impGraphs).forEach(graph -> {
                graph.setCreateTime(super.getCreateTime());
                graph.setCreateUser(super.getCreateUser());
                graph.setModifiedTime(null);
                graph.setModifiedUser(null);

                scenarioGraphDagService.updateScenarioGraph(graph, false);
            });

            StreamSupport.stream(impScenarios).forEach(s -> {
                s.setCreateTime(super.getCreateTime());
                s.setCreateUser(super.getCreateUser());
                s.setInspectTime(null);
                s.setInspectUser(null);
                s.setModifiedTime(null);
                s.setModifiedUser(null);
                s.setOfflineTime(null);
                s.setOfflineUser(null);
                s.setOnlineTime(null);
                s.setOfflineUser(null);
                s.setScenarioStatus(ScenarioStatus.READY.ordinal());

                scenarioDao.save(s);
            });

            logger.debug("end import data.");

        } catch (Exception e) {
            throw new ScenarioServiceException(22999, this + " -> " + e.getMessage(), e);
        } finally {
            logger.debug("finish decompressed file.");
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(ais);
            IOUtils.closeQuietly(in);

        }

        this.progress = 1.0;
    }

    public Object preview() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        logger.debug("open compressed file.");
        InputStream in = Files.newInputStream(path);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);

        logger.debug("extra preview tbl.");
        ais.getNextEntry();

        ObjectInputStream ois = new ObjectInputStream(ais);

        Object obj = ois.readObject();

        ois.close();
        ais.close();

        return obj;
    }
}
