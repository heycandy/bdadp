package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;

/**
 * Created by White on 2016/11/17.
 */
public class ScenarioExportTask extends ScenarioDumpTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Collection<Scenario> scenarios = Sets.newHashSet();
    private final Collection<ScenarioGraphDAG> graphs = Sets.newHashSet();

    private ScenarioService scenarioService = ServiceContext.getService(ScenarioService.class);

    private ScenarioGraphDagService
            scenarioGraphDagService = ServiceContext.getService(ScenarioGraphDagService.class);

    public ScenarioExportTask(String id, String name, Collection<String> collection, Path path,
                              String createUser) {
        super(id, name, collection, path, createUser);
    }

    @Override
    public String getAction() {
        return "scenario.export";
    }

    @Override
    public void run() {
        double progress = 1.0 / (1 + 9 + 1);

        OutputStream out = null;
        ArchiveOutputStream aos = null;
        ObjectOutputStream oos = null;

        this.setProgress(progress);
        try {
            Collection<ScenarioDepsRow> previewRows = Lists.newArrayList();
            for (String scenarioId : collection) {
                logger.debug("find scenario -> " + scenarioId);
                Scenario scenario = scenarioService.findScenarioById(scenarioId);
                ScenarioServiceAssert.nonExistsScenario(scenario == null, scenarioId);
                scenarios.add(scenario);
                ScenarioGraphDAG graph =
                        scenarioGraphDagService.findScenarioByScenarioId(scenarioId);

                logger.debug("analysis scenario dependency -> " + scenarioId);
                Collection<String> visited = Lists.newArrayList();

                if (graph != null) {
                    graphs.add(graph);
                    this.resolveEmbeddedFlows(graph, visited);
                }

                logger.debug("collect scenario dependency -> " + scenarioId);
                Collection<Scenario> depScenarios = StreamSupport.stream(visited)
                        .map(scenarioService::findScenarioById)
                        .collect(Collectors.toSet());

                scenarios.addAll(depScenarios);

                logger.debug("generate scenario dependency -> " + scenarioId);
                Collection<ScenarioDepsRow> scenarioDeps = StreamSupport.stream(depScenarios)
                        .map(s -> new ScenarioDepsRow()
                                .setCreateTime(s.getCreateTime())
                                .setCreateUser(s.getCreateUser())
                                .setScenarioDesc(s.getScenarioDesc())
                                .setScenarioId(s.getScenarioId())
                                .setScenarioName(s.getScenarioName()))
                        .collect(Collectors.toSet());

                logger.debug("generate dependency row -> " + scenarioId);
                ScenarioDepsRow previewRow = new ScenarioDepsRow()
                        .setCreateTime(scenario.getCreateTime())
                        .setCreateUser(scenario.getCreateUser())
                        .setScenarioDeps(scenarioDeps)
                        .setScenarioDesc(scenario.getScenarioDesc())
                        .setScenarioId(scenarioId)
                        .setScenarioName(scenario.getScenarioName());

                previewRows.add(previewRow);

            }

            ArchiveStreamFactory factory = new ArchiveStreamFactory();

            this.setProgress(progress);
            logger.debug("create compressed file.");
            out = Files.newOutputStream(path);
            aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

            this.setProgress(progress);
            logger.debug("create deps tbl.");
            File f0 = new File("deps.tbl");
            ArchiveEntry e0 = aos.createArchiveEntry(f0, f0.getName());

            aos.putArchiveEntry(e0);

            this.setProgress(progress);
            logger.debug("create output stream.");
            oos = new ObjectOutputStream(aos);

            this.setProgress(progress);
            logger.debug("write deps tbl.");
            ScenarioDepsTable tbl = new ScenarioDepsTable()
                    .setArchiverVersion("Candy liu")
                    .setBuildJdk(System.getProperty("java.version"))
                    .setBuiltBy(getAction())
                    .setCreateBy(getCreateUser())
                    .setCreateTime(new Date())
//          .setEntryEncoding(factory.getEntryEncoding())
                    .setPreviewRows(previewRows)
                    .setPreviewVersion("1,090");

            oos.writeObject(tbl);
            oos.flush();

            logger.debug("finish deps tbl.");
            aos.closeArchiveEntry();

            this.setProgress(progress);
            logger.debug("create package dat.");
            File f1 = new File("package.dat");
            ArchiveEntry e1 = aos.createArchiveEntry(f1, f1.getName());

            aos.putArchiveEntry(e1);

            this.setProgress(progress);
            logger.debug("write scenario data.");
            oos.writeObject(scenarios);
            oos.flush();

            this.setProgress(progress);
            logger.debug("write graph data.");
            oos.writeObject(graphs);
            oos.flush();

            logger.debug("finish package data.");
            aos.closeArchiveEntry();

            this.setProgress(progress);
            logger.debug("create readme md.");
            File f2 = new File("README.md");
            ArchiveEntry e2 = aos.createArchiveEntry(f2, f2.getName());

            aos.putArchiveEntry(e2);

            this.setProgress(progress);
            logger.debug("write readme md.");
            OutputStreamWriter osw = new OutputStreamWriter(aos, Charsets.UTF_8);
            osw.write("# Big Data Scenarios Development Platform");
            osw.write("\n");
            osw.write("\n");
            osw.write("*This package contains a list of scenarios built by export task.*");
            osw.write("\n");
            osw.write("\n");
            osw.write("**please don't attempt to modify any.**");
            osw.write("\n");
            osw.write("\n");
            osw.write("---");
            osw.write("\n");
            osw.write("\n");
            osw.write("| Scenario Name | Scenario Desc. | Create User | Create Time |");
            osw.write("\n");
            osw.write("| --- | --- | --- | --- |");
            osw.write("\n");
            for (ScenarioDepsRow row : previewRows) {
                osw.write(String
                        .format("| %s | %s | %s | %s |", row.getScenarioName(), row.getScenarioDesc(),
                                row.getCreateUser(), row.getCreateTime()));
                osw.write("\n");
            }
            osw.write("\n");
            osw.write("---");
            osw.write("\n");
            osw.write("\n");
            osw.write("Copyright 2016 &copy; Chinasoft International Co., Ltd");
            osw.write("\n");
            osw.write("All rights reserved.");
            osw.write("\n");
            osw.flush();

            aos.closeArchiveEntry();

            aos.finish();

            if (logger.isDebugEnabled()) {
                logger.warn(
                        "If u see this statement but not in dev mode, please set logger level to [INFO].");
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            throw new ScenarioServiceException(22999, this + " -> " + e.getMessage(), e);
        } finally {
            logger.debug("finish compressed file.");
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(aos);
            IOUtils.closeQuietly(out);
        }

        this.progress = 1.0;

    }

    private void resolveEmbeddedFlows(ScenarioGraphDAG graph, Collection<String> visited) {
        for (ScenarioGraphVertex vertex : graph.getGraphVertexs()) {
            Task task = vertex.getTask();
            if (task.getTaskType().equals("scenario")) {
                String relationId = task.getRelationId();
                ScenarioGraphDAG
                        embeddedGraph =
                        scenarioGraphDagService.findScenarioByScenarioId(relationId);

                visited.add(relationId);
                graphs.add(embeddedGraph);

                resolveEmbeddedFlows(embeddedGraph, visited);
            }

        }

    }
}
