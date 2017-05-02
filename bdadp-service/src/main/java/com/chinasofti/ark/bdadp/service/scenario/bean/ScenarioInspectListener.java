package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.push.PushService;
import com.chinasofti.ark.bdadp.service.push.bean.EventBody;

/**
 * Created by White on 2016/11/24.
 */
public class ScenarioInspectListener implements Listener {

    private PushService pushService = ServiceContext.getService(PushService.class);

    @Override
    public void listen(Component component) {
        pushService.sendEvent(new EventBody(0, "test", component));
    }
}
