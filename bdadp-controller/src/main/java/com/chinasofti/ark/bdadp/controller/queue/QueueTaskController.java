package com.chinasofti.ark.bdadp.controller.queue;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.controller.queue.bean.QueueAction;
import com.chinasofti.ark.bdadp.service.queue.QueueTaskService;
import com.chinasofti.ark.bdadp.service.queue.bean.QueueTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by White on 2016/11/21.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class QueueTaskController {

    @Autowired
    private QueueTaskService queueTaskService;

    @RequestMapping(value = "/queue/task", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody findAll() {
        ResultBody<Iterable<QueueTask>> body = new ResultBody<>();
        try {
            body.setResult(queueTaskService.findAll());
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @RequestMapping(value = "/queue/task/action", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResultBody action(@RequestBody QueueAction action) {
        ResultBody<Iterable<String>> body = new ResultBody<>();
        try {
            switch (action.getAction()) {
                case "cancel":
                    body.setResult(queueTaskService.cancel(action.getIterable()));
                    break;

                case "remove":
                    body.setResult(queueTaskService.remove(action.getIterable()));
                    break;
            }

        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

}
