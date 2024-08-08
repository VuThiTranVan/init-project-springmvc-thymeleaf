package com.spring.sample.util;

import java.util.Collections;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class ViewSupportDialect extends AbstractDialect implements IExpressionObjectDialect {

	public ViewSupportDialect() {
		super("PrettyTime Dialect");
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IExpressionObjectFactory() {

			@Override
			public Set<String> getAllExpressionObjectNames() {
				return Collections.singleton("viewSupport");
			}

			@Override
			public Object buildObject(IExpressionContext context, String expressionObjectName) {
				return new ViewSupportUtil();
			}

			@Override
			public boolean isCacheable(String expressionObjectName) {
				return true;
			}
		};
	}
}