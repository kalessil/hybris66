/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.sapordermgmtservices.converters.populator;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.sap.core.common.TechKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.data.CartEntryConfigurationAttributes;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf.Schedline;
import de.hybris.platform.sap.sapordermgmtbol.transaction.item.businessobject.interf.Item;
import de.hybris.platform.sap.sapordermgmtservices.schedline.data.ScheduleLineData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 *
 */
public class DefaultAbstractOrderEntryPopulator implements Populator<Item, OrderEntryData>
{
	private static final Logger LOG = Logger.getLogger(DefaultAbstractOrderEntryPopulator.class);
	private PriceDataFactory priceFactory;
	private Converter<Schedline, ScheduleLineData> scheduleLinesConverter;
	private ProductConfigurationService productConfigurationService;
	private SessionAccessService sessionAccessService;
	private Converter<ConfigModel, List<ConfigurationInfoData>> orderEntryConfigurationInfoConverter;

	/**
	 * @return the productConfigurationService
	 */
	public ProductConfigurationService getProductConfigurationService()
	{
		return productConfigurationService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final Item item, final OrderEntryData target) throws ConversionException
	{
		target.setBasePrice(priceFactory.create(PriceDataType.BUY, item.getNetValueWOFreight(), item.getCurrency()));
		target.setTotalPrice(priceFactory.create(PriceDataType.BUY, item.getGrossValue(), item.getCurrency()));

		target.setQuantity(Long.valueOf(item.getQuantity().longValue()));
		target.setEntryNumber(Integer.valueOf(item.getNumberInt()));
		final ProductData productData = createProductFromItem(item);
		productData.setPurchasable(Boolean.TRUE);
		target.setProduct(productData);
		target.setUpdateable(true);
		target.setItemPK(item.getHandle());

		final String itemHandle = item.getHandle();
		//In case the item handle is a guid, we got it from LO-API from a persisted ERP order -> we don't deal with product configuration in that case
		//Product configuration relevant attributes are only taken care of in case of cart entries
		if (itemHandle.length() != 32)
		{
			LOG.debug("item has not been persisted yet, so configuration has to be assigned to the item " + itemHandle);
			handleConfiguration(item, target, productData);
		}
		else
		{
			LOG.debug("we deal with an existing ECC item:  " + itemHandle);
			handleConfigurationBackendLeads(item, target, productData);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("creating schedule lines for: " + item.getNumberInt());
		}

		target.setScheduleLines(convertScheduleLines(item.getScheduleLines()));
	}

	/**
	 * @param item
	 * @param target
	 * @param productData
	 */
	protected void handleConfiguration(final Item item, final OrderEntryData target, final ProductData productData)
	{
		target.setConfigurationAttached(item.isConfigurable());
		if (item.isConfigurable())
		{
			productData.setConfigurable(Boolean.TRUE);
			final CartEntryConfigurationAttributes configAttributes = productConfigurationService
					.calculateCartEntryConfigurationAttributes(item.getHandle(), productData.getCode(), null);
			addNumberOfIssuesForCartDisplay(configAttributes, target);

			final ConfigModel configModel = getProductConfigurationService()
					.retrieveConfigurationModel(getSessionAccessService().getConfigIdForCartEntry(item.getHandle()));
			final List<ConfigurationInfoData> configInfoData = getOrderEntryConfigurationInfoConverter().convert(configModel);
			target.setConfigurationInfos(configInfoData);
		}
	}

	protected void addNumberOfIssuesForCartDisplay(final CartEntryConfigurationAttributes configurationAttributes,
			final OrderEntryData targetEntry)
	{
		if (!configurationAttributes.getConfigurationConsistent().booleanValue())
		{
			final Map<ProductInfoStatus, Integer> statusSummaryMap = new HashMap<>();
			statusSummaryMap.put(ProductInfoStatus.ERROR, configurationAttributes.getNumberOfErrors());
			targetEntry.setStatusSummaryMap(statusSummaryMap);
		}
	}

	ProductData createProductFromItem(final Item item)
	{
		final ProductData productData = new ProductData();
		final TechKey productGuid = item.getProductGuid();
		if (productGuid != null && (!TechKey.isEmpty(productGuid)))
		{
			//item exists in ERP
			productData.setCode(formatProductIdForHybris(item.getProductGuid().getIdAsString()));
		}
		else
		{
			productData.setCode(formatProductIdForHybris(item.getProductId()));
		}
		productData.setName(item.getProductId());
		return productData;
	}

	/**
	 * @param SapScheduleLines
	 * @return List<ScheduleLineData>
	 */
	private List<ScheduleLineData> convertScheduleLines(final List<Schedline> SapScheduleLines)
	{
		final List<ScheduleLineData> scheduleLines = new ArrayList<ScheduleLineData>();

		for (final Schedline sapScheduleLine : SapScheduleLines)
		{
			final ScheduleLineData hybrisScheduleLine = scheduleLinesConverter.convert(sapScheduleLine);
			scheduleLines.add(hybrisScheduleLine);
		}
		return scheduleLines;
	}

	/**
	 * @return the priceFactory
	 */
	public PriceDataFactory getPriceFactory()
	{
		return priceFactory;
	}

	/**
	 * @param priceFactory
	 *           the priceFactory to set
	 */
	public void setPriceFactory(final PriceDataFactory priceFactory)
	{
		this.priceFactory = priceFactory;
	}

	/**
	 * @return the scheduleLinesConverter
	 */
	public Converter<Schedline, ScheduleLineData> getScheduleLinesConverter()
	{
		return scheduleLinesConverter;
	}

	/**
	 * @param scheduleLinesConverter
	 *           the scheduleLinesConverter to set
	 */
	public void setScheduleLinesConverter(final Converter<Schedline, ScheduleLineData> scheduleLinesConverter)
	{
		this.scheduleLinesConverter = scheduleLinesConverter;
	}

	/**
	 * Formats product ID taken from backend catalog and prepares it for Hybris. Removes leading zeros.l
	 *
	 * @param input
	 * @return Formatted product ID
	 */
	protected String formatProductIdForHybris(final String input)
	{

		return input;
	}

	/**
	 * @param productConfigurationService
	 */
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;

	}

	protected boolean isKbPresent(final Item item)
	{
		final Date kbDate = item.getKbDate();
		if (kbDate != null)
		{
			return productConfigurationService.hasKbForDate(item.getProductId(), kbDate);
		}
		else
		{
			return false;
		}
	}

	protected void handleConfigurationBackendLeads(final Item item, final OrderEntryData target, final ProductData product)
	{
		final boolean isConfigurableAndValid = item.isConfigurable() && isKbPresent(item);
		if (isConfigurableAndValid)
		{
			target.setConfigurationAttached(true);
			product.setConfigurable(Boolean.TRUE);
			final List<ConfigurationInfoData> configInfoData = new ArrayList<>();
			final ConfigurationInfoData configInfo = new ConfigurationInfoData();
			configInfo.setConfiguratorType(ConfiguratorType.CPQCONFIGURATOR);
			configInfoData.add(configInfo);
			target.setConfigurationInfos(configInfoData);
		}

	}

	public Converter<ConfigModel, List<ConfigurationInfoData>> getOrderEntryConfigurationInfoConverter()
	{
		return orderEntryConfigurationInfoConverter;
	}

	public void setOrderEntryConfigurationInfoConverter(
			final Converter<ConfigModel, List<ConfigurationInfoData>> orderEntryConfigurationInfoConverter)
	{
		this.orderEntryConfigurationInfoConverter = orderEntryConfigurationInfoConverter;
	}

	/**
	 * @return the sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

}