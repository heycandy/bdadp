package com.chinasofti.ark.bdadp.service.scenario.bean;

/**
 * Created by White on 2016/10/13.
 */
public class ScenarioServiceAssert {

    public static void nonExistsScenario(boolean expression, Object... args) {
        if (expression) {
            String format = "Scenario (#%s) does not exists.";
            String message = args.length > 0 ? String.format(format, args) : "Scenario does not exists.";

            throw new ScenarioServiceException(22002, message, args);
        }
    }

    public static void nonExistsGraph(boolean expression, Object... args) {
        if (expression) {
            String format = "Scenario graph (#%s) does not exists.";
            String
                    message =
                    args.length > 0 ? String.format(format, args) : "Scenario graph does not exists.";

            throw new ScenarioServiceException(22003, message, args);
        }
    }

    public static void nonExistsTask(boolean expression, Object... args) {
        if (expression) {
            String format = "Scenario task (#%s) does not exists.";
            String
                    message =
                    args.length > 0 ? String.format(format, args) : "Scenario task does not exists.";

            throw new ScenarioServiceException(22004, message, args);
        }
    }

    public static void isCycleDependency(boolean expression, Object... args) {
        if (expression) {
            String format = "The cycle dependency found in this scenario graph, %s";
            String
                    message =
                    args.length > 0 ? String.format(format, args)
                            : "The cycle dependency found in this scenario graph.";

            throw new ScenarioServiceException(22009, message, args);
        }
    }

}
