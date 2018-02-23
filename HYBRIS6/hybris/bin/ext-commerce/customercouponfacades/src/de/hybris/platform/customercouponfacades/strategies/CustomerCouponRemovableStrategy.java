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
package de.hybris.platform.customercouponfacades.strategies;



/**
 * Strategy to check a customer coupon can be removed.
 */
public interface CustomerCouponRemovableStrategy
{

	/**
	 * check the given coupon can be removed from current customer.
	 *
	 * @param couponCode
	 *           code of coupon that should be checked
	 * @return boolean if true can be removed false otherwise
	 */
	default boolean checkRemovable(final String couponCode)
	{
		return true;
	}

}