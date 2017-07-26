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
import java.util.List;
import java.util.Properties;

/**
 * Created by White on 2016/10/14.
 */
public class ScenarioScheduleMailListener implements Listener {

  //  private Properties currentProps = PropsService.getConfigProps();
//  private String roleName = currentProps.getProperty("mail.receivergroup.rolename");
  private String roleName = "oper";
  private UserDao userDao;
  private RoleDao roleDao;
  private ScenarioExecutorService service;
  private MailService mailService;

  public ScenarioScheduleMailListener() {
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

        String subject = "Schedule failed Scenario name : " + flow.getName();
        String content = "See : < " + System.getProperty("loginUrl")
            + " > \r\n ------------------------------------------ \r\n \r\n \r\n";
        try {
          content += service.execute(flow.getId(), flow.getExecutionId()).toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
//        MailUtils.sendMails(subject, content, getReceivers(ROLE_NAME));
        mailService.sendEmail(subject, content, getReceivers(roleName));
      }
    }
  }

  public String[] getReceivers(String roleName) {
    Role role = roleDao.findAllByRoleName(roleName);
    List<User> users = userDao.findAllByRoles(role);
    String[] mails = new String[users.size()];
    for (int i = 0; i < users.size(); i++) {
      mails[i] = users.get(i).getUserDesc();
    }
    return mails;
  }

}
