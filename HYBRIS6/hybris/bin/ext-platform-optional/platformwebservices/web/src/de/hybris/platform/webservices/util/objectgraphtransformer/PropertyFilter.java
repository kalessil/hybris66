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
package de.hybris.platform.webservices.util.objectgraphtransformer;



/**
 * General filter for a property
 */
public interface PropertyFilter
{
	/**
	 * Returns true when property value shall be filtered.
	 * 
	 * @param ctx
	 *           {@link GraphContext}
	 * @param value
	 *           property value
	 * @return true when value shall be filtered
	 */
	boolean isFiltered(PropertyContext ctx, Object value);
}