package com.chinasofti.ark.bdadp.expression;

import com.chinasofti.ark.bdadp.expression.support.ArkEvaluationContext;
import com.chinasofti.ark.bdadp.expression.variable.*;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Created by White on 2016/09/11.
 */
public class ArkExpressionParser {


    private ExpressionParser expressionParser;
    private EvaluationContext evaluationContext;
    private ParserContext parserContext;

    public ArkExpressionParser() {
        this.expressionParser = new SpelExpressionParser();
        this.evaluationContext = new ArkEvaluationContext();
        this.parserContext = new TemplateParserContext();

        setDefaultVariable();
    }


    public void setDefaultVariable() {
        evaluationContext.setVariable("day", new SomeDayVariable());
        evaluationContext.setVariable("someMonthEnd", new SomeMonthEndVariable());
        evaluationContext.setVariable("someMonthStart", new SomeMonthStartVariable());
        evaluationContext.setVariable("someWeekMonday", new SomeWeekMonDayVariable());
        evaluationContext.setVariable("someWeekSunDay", new SomeWeekSunDayVariable());
        evaluationContext.setVariable("someWeekSunDay", new SomeWeekSunDayVariable());

    }

    public void setVariable(ArkVariable variable) {
        evaluationContext.setVariable(variable.getName(), variable);
    }

    public void setVariable(Iterable<ArkVariable> variables) {
        for (ArkVariable variable : variables) {
            evaluationContext.setVariable(variable.getName(), variable);
        }
    }

    public String getValue(String expressionString) {
        return getValue(expressionString, String.class);
    }

    public <T> T getValue(String expressionString, Class<T> clazz) {
        Expression expression = expressionParser.parseExpression(expressionString, parserContext);
        return expression.getValue(evaluationContext, clazz);
    }

    public Class<?> getValueType(String expressionString) {
        Expression expression = expressionParser.parseExpression(expressionString, parserContext);

        return expression.getValueType();
    }
}
