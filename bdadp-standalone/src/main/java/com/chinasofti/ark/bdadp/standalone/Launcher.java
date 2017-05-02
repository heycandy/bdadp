package com.chinasofti.ark.bdadp.standalone;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by White on 2017/4/21.
 */
public class Launcher {
    // TODO: You should configure this appropriately for your environment
    private static final String ARK_WAR = System.getenv("ARK_WAR");
    private static final String ARK_HOME = System.getenv("ARK_HOME");
    private static final String ARK_ENV = System.getenv("ARK_ENV") == null ? "dev" : System.getenv("ARK_ENV");

    private static final String WEB_INF_DIR = Paths.get(ARK_WAR, "WEB-INF").toString();
    private static final String WEB_XML_FILE = Paths.get(WEB_INF_DIR, "web.xml").toString();

    private static final String ARK_LOG_DIR = Paths.get(ARK_HOME, "logs").toString();
    private static final String ARK_LOG_FILE = Paths.get(ARK_LOG_DIR, "ark.yyyy_mm_dd.log").toString();

    private Server server;

    private int port;
    private String host;
    private String contextPath;

    private Launcher() {
        this(8080);
    }

    private Launcher(int aPort) {
        this(aPort, null, "/");
    }

    private Launcher(int aPort, String aHost, String aContextPath) {
        port = aPort;
        host = aHost;
        contextPath = aContextPath;
    }

    private void start() throws Exception {
        server = new Server();

        WebAppContext webAppContext = new WebAppContext();

        webAppContext.setContextPath(contextPath);
        webAppContext.setServer(server);
        if (ARK_ENV.equals("dev")) {
            webAppContext.setWar(getShadedWarUrl());
        } else {
            webAppContext.setWar(ARK_WAR);
        }

        server.setThreadPool(createThreadPool());
        server.addConnector(createConnector());
        server.setHandler(createHandlers(webAppContext));
        server.setStopAtShutdown(true);
        server.start();
    }

    private void join() throws InterruptedException {
        server.join();
    }

    private void stop() throws Exception {
        server.stop();
    }

    private ThreadPool createThreadPool() {
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(10);
        _threadPool.setMaxThreads(100);
        return _threadPool;
    }

    private SelectChannelConnector createConnector() {
        SelectChannelConnector _connector = new SelectChannelConnector();
        _connector.setPort(port);
        _connector.setHost(host);
        return _connector;
    }

    private HandlerCollection createHandlers(WebAppContext webAppContext) {
        ContextHandler _ctx = new ContextHandler();

        _ctx.setContextPath(contextPath);

        List<Handler> _handlers = new ArrayList<>();

        _handlers.add(_ctx);
        _handlers.add(webAppContext);

        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[1]));

        RequestLogHandler _log = new RequestLogHandler();
        _log.setRequestLog(createRequestLog());

        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[]{_contexts, _log});

        return _result;
    }

    private RequestLog createRequestLog() {
        NCSARequestLog _log = new NCSARequestLog();
        File _logPath = new File(ARK_LOG_FILE);
        _logPath.getParentFile().mkdirs();

        _log.setFilename(_logPath.getPath());
        _log.setRetainDays(90);
        _log.setExtended(false);
        _log.setAppend(true);
        _log.setLogTimeZone("GMT");
        _log.setLogLatency(true);
        return _log;
    }

    private URL getResource(String aResource) throws IOException {
        File file = new File(aResource);
        URI fileUri = file.toURI();
        URL fileUrl = fileUri.toURL();
        return fileUrl;
    }

    private String getShadedWarUrl() throws IOException {
        String _urlStr = getResource(WEB_XML_FILE).toString();
        return _urlStr.substring(0, _urlStr.length() - 15);
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String host = "localhost";
        String contextPath = "/";

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        if (args.length > 1) {
            host = args[1];
        }

        if (args.length > 2) {
            contextPath = args[2];
        }

        Launcher launcher = new Launcher(port, host, contextPath);

        launcher.start();
        launcher.join();
        launcher.stop();
    }
}
