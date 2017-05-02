//package com.chinasofti.ark.bdadp.expression;
//
//import org.springframework.expression.ParserContext;
//
//public class TemplateParserContext implements ParserContext {
//
//	private final String expressionPrefix;
//
//	private final String expressionSuffix;
//
//
//	/**
//	 * Create a new TemplateParserContext with the default "#{" prefix and "}" suffix.
//	 */
//	public TemplateParserContext() {
//		this("\"", "\"");
//	}
//
//	/**
//	 * Create a new TemplateParserContext for the given prefix and suffix.
//	 * @param expressionPrefix the expression prefix to use
//	 * @param expressionSuffix the expression suffix to use
//	 */
//	public TemplateParserContext(String expressionPrefix, String expressionSuffix) {
//		this.expressionPrefix = expressionPrefix;
//		this.expressionSuffix = expressionSuffix;
//	}
//
//
//	@Override
//	public final boolean isTemplate() {
//		return true;
//	}
//
//	@Override
//	public final String getExpressionPrefix() {
//		return this.expressionPrefix;
//	}
//
//	@Override
//	public final String getExpressionSuffix() {
//		return this.expressionSuffix;
//	}
//
//}