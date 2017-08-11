package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.dao.user.RoleDao;
import com.chinasofti.ark.bdadp.dao.user.UserDao;
import com.chinasofti.ark.bdadp.entity.user.Role;
import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.PropsService;
import com.chinasofti.ark.bdadp.service.schedule.impl.MailService;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.flow.bean.SimpleCallableFlow;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioExecutorService;
import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by White on 2016/10/14.
 */
public class ScenarioScheduleMSGListener implements Listener {

  private UserDao userDao;
  private RoleDao roleDao;
  private ScenarioExecutorService service;
  private MailService mailService;

  public ScenarioScheduleMSGListener() {
    this.service = ServiceContext.getService(ScenarioExecutorService.class);
    this.userDao = ServiceContext.getService(UserDao.class);
    this.roleDao = ServiceContext.getService(RoleDao.class);
    this.mailService = ServiceContext.getService(MailService.class);
  }

  @Override
  public void listen(Component component) {

    if (component instanceof SimpleCallableFlow) {
      SimpleCallableFlow flow = ((SimpleCallableFlow) component);

      if (flow.getState() == 3) {
        // send mail
        String subject = "Schedule failed scenario name : " + flow.getName();
        String content = "See : < " + System.getProperty("loginUrl")
            + " > \r\n  ------------------------------------------  \r\n \r\n \r\n ";
        try {
          content += service.execute(flow.getId(), flow.getExecutionId()).toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
        mailService.sendEmail(subject, content, getReceivers(), getLoginUserMail());

        //send msg
        /*
        ...
        */
      }
    }
  }

  public String[] getReceivers(String roleName) {
    List<User> users = userDao.findAllByRoles(roleDao.findAllByRoleName(roleName));
    String[] mails = new String[users.size()];
    for (int i = 0; i < users.size(); i++) {
      mails[i] = users.get(i).getUserDesc();
    }
    return mails;
  }

  public String[] getReceivers() {
    return getReceivers(PropsService.getConfigProps().getProperty("mail.receivergroup.rolename"));
  }

  public String getLoginUserMail() {
    String loginUser = System.getProperty("loginUser");
    return StringUtils.isNotEmpty(loginUser) ? null
        : userDao.findAllByUserName(loginUser).getUserDesc();
  }

}
