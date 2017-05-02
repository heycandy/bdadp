package com.chinasofti.ark.bdadp.service.push;

import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.push.bean.EventBody;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * Created by White on 2016/09/13.
 */
public interface PushService extends InitializingBean, DisposableBean {

    void setInitProperties(Properties props);

    void sendEvent(EventBody eventBody, User... users);
}
