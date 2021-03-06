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
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 *
 */
public class BaseOptionDataPopulator implements Populator<VariantProductModel, BaseOptionData>
{
	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	protected Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter()
	{
		return variantOptionDataConverter;
	}

	@Required
	public void setVariantOptionDataConverter(final Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter)
	{
		this.variantOptionDataConverter = variantOptionDataConverter;
	}

	@Override
	public void populate(final VariantProductModel source, final BaseOptionData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final ProductModel baseProduct = source.getBaseProduct();
		target.setVariantType(baseProduct.getVariantType().getCode());
		target.setSelected(getVariantOptionDataConverter().convert(source));
	}
}
