/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.aop;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * Basic time measurement for batch processing.
 */
public class TimeMeasurementAspect
{
	private static final Logger LOG = Logger.getLogger(TimeMeasurementAspect.class);

	/**
	 * Invokes a method and measures the execution time.
	 * 
	 * @param pjp
	 * @return result of the invocation
	 * @throws Throwable
	 */
	public Object measure(final ProceedingJoinPoint pjp) throws Throwable // NOSONAR
	{
		final String methodName = pjp.getTarget().getClass().getSimpleName() + "." + pjp.getSignature().getName();
		final Object[] args = pjp.getArgs();
		final long start = System.currentTimeMillis();
		final Object result = pjp.proceed();
		if (LOG.isInfoEnabled())
		{
			final BatchHeader header = AspectUtils.getHeader(args);
			LOG.info("Processed " + methodName + (header == null ? "" : " [header=" + header + "]") + " in "
					+ (System.currentTimeMillis() - start) + "ms");
		}
		return result;
	}
}