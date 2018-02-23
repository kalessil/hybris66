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

package de.hybris.platform.customercouponfacades.hooks.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.hooks.NotificationChannelHook;


/**
 * 
 */
public class SmsChannelHook implements NotificationChannelHook
{

	@Override
	public Boolean isChannelOn(final CustomerModel customer)
	{
		return customer.getSmsPreference() == null ? Boolean.FALSE : customer.getSmsPreference();
	}

}