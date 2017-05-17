package com.chinasofti.ark.bdadp.service.push.impl;

import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.push.PushService;
import com.chinasofti.ark.bdadp.service.push.bean.EventBody;
import com.chinasofti.ark.bdadp.service.user.UserSecurityService;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by White on 2016/09/13.
 */

public class PushServiceImpl implements PushService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Configuration config;
    private SocketIOServer server;

    @Autowired
    private UserSecurityService userSecurityService;

    @Override
    public void setInitProperties(Properties props) {
        config = new Configuration();

        config.setHostname(props.getProperty("hostname", "localhost"));
        config.setPort(Integer.valueOf(props.getProperty("port", "8081")));

    }

    @Override
    public void destroy() throws Exception {
        server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server = new SocketIOServer(config);

        server.addConnectListener(
                socketIOClient ->
                        logger.info("onConnect {} {}",
                                socketIOClient.getRemoteAddress(), socketIOClient.getSessionId()));

        server.addDisconnectListener(
                socketIOClient ->
                    logger.info("onDisconnect {} {}",
                                socketIOClient.getRemoteAddress(), socketIOClient.getSessionId()));

        server.start();

    }

    @Override
    public void sendEvent(EventBody event, User... users) {
        Collection<User> collection = Arrays.asList(users);
        Iterable<String> iterable = StreamSupport.stream(collection)
                .map(userSecurityService::logged)
                .collect(Collectors.toSet());

        event.setTokens(iterable);

        server.getBroadcastOperations().sendEvent(event.getName(), event);
    }
}
