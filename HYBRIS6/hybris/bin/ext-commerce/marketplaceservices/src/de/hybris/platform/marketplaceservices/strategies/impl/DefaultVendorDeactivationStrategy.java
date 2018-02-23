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
package de.hybris.platform.marketplaceservices.strategies.impl;

import de.hybris.platform.marketplaceservices.model.VendorUserModel;
import de.hybris.platform.marketplaceservices.strategies.VendorDeactivationStrategy;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * An implementation for {@link VendorDeactivationStrategy}
 */
public class DefaultVendorDeactivationStrategy implements VendorDeactivationStrategy
{

	private ModelService modelService;

	@Override
	public void deactivateVendor(VendorModel vendor)
	{
		final Collection<VendorUserModel> users = vendor.getVendorUsers();
		if (CollectionUtils.isNotEmpty(users))
		{
			users.forEach(user -> user.setLoginDisabled(true));
			getModelService().saveAll(users);
		}
		vendor.setActive(false);
		getModelService().save(vendor);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}