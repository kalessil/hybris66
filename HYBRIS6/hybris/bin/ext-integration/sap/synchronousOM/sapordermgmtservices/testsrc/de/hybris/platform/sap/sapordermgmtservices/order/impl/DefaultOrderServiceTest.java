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
package de.hybris.platform.sap.sapordermgmtservices.order.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.sap.core.common.TechKey;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Configuration;
import de.hybris.platform.sap.productconfig.runtime.interf.external.impl.ConfigurationImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf.Order;
import de.hybris.platform.sap.sapordermgmtbol.transaction.item.businessobject.impl.ItemListImpl;
import de.hybris.platform.sap.sapordermgmtbol.transaction.item.businessobject.impl.ItemSalesDoc;
import de.hybris.platform.sap.sapordermgmtbol.transaction.item.businessobject.interf.Item;
import de.hybris.platform.sap.sapordermgmtbol.transaction.order.businessobject.impl.OrderImpl;
import de.hybris.platform.sap.sapordermgmtservices.bolfacade.BolOrderFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings(
{ "javadoc", "unchecked" })
public class DefaultOrderServiceTest
{
	BolOrderFacade bolOrderFacade = null;
	Converter<Order, OrderData> orderConverter;
	DefaultOrderService classUnderTest = new DefaultOrderService();
	String one = "1";
	String oneWithLeadingZeros = "0000000001";
	String allZeros = "0000000000";
	private final String code = "083736";
	private final static String itemNumber = "10";
	private Order order;
	OrderData orderData;
	private Item item = new ItemSalesDoc();
	ProductConfigurationService productConfigurationService;
	private ConfigModel configModel = new ConfigModelImpl();
	private Configuration externalConfiguration = new ConfigurationImpl();
	private final static String configId = "1";

	@Before
	public void init()
	{
		order = new OrderImpl();
		order.setItemList(new ItemListImpl());
		order.addItem(item );
		item.setNumberInt(Integer.valueOf(itemNumber));
		item.setProductConfigurationExternal(externalConfiguration );
		orderData = new OrderData();
		bolOrderFacade = EasyMock.createMock(BolOrderFacade.class);
		orderConverter = EasyMock.createMock(Converter.class);
		EasyMock.expect(bolOrderFacade.getSavedOrder(code)).andReturn(order);
		EasyMock.expect(orderConverter.convert(order)).andReturn(orderData);
		EasyMock.replay(bolOrderFacade, orderConverter);
		classUnderTest.setBolOrderFacade(bolOrderFacade);
		classUnderTest.setOrderConverter(orderConverter);
		
		configModel.setId(configId);
		productConfigurationService =  EasyMock.createMock(ProductConfigurationService.class);
		EasyMock.expect(productConfigurationService.createConfigurationFromExternalSource(externalConfiguration)).andReturn(configModel );
		productConfigurationService.releaseSession(configId, true);
		EasyMock.expectLastCall();
		EasyMock.replay(productConfigurationService);
		classUnderTest.setProductConfigurationService(productConfigurationService);

	}


	@Test
	public void testCalculateNumberOfPages()
	{
		int totalNumberOfResults = 1;
		int pageSize = 3;
		int numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(1, numberOfPages);

		totalNumberOfResults = 2;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(1, numberOfPages);

		totalNumberOfResults = 3;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(1, numberOfPages);

		totalNumberOfResults = 4;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(2, numberOfPages);

		totalNumberOfResults = 5;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(2, numberOfPages);

		totalNumberOfResults = 6;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(2, numberOfPages);

		totalNumberOfResults = 7;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(3, numberOfPages);

		totalNumberOfResults = 8;
		pageSize = 3;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(3, numberOfPages);

		totalNumberOfResults = 18;
		pageSize = 5;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(4, numberOfPages);

		totalNumberOfResults = 24;
		pageSize = 5;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(5, numberOfPages);

		totalNumberOfResults = 25;
		pageSize = 5;
		numberOfPages = classUnderTest.calculateNumberOfPages(totalNumberOfResults, pageSize);
		assertEquals(5, numberOfPages);
	}

	@Test
	public void testAddLeadingZeros()
	{
		assertEquals(oneWithLeadingZeros, classUnderTest.addLeadingZeros(one, this.allZeros));
		final String alphaNum = "0A";
		assertEquals(alphaNum, classUnderTest.addLeadingZeros(alphaNum, this.allZeros));
	}

	@Test
	public void testAddLeadingZerosOrderID()
	{
		assertEquals(oneWithLeadingZeros, classUnderTest.addLeadingZerosOrderID(one));
	}

	@Test
	public void testBolOrderFacade()
	{
		assertEquals(bolOrderFacade, classUnderTest.getBolOrderFacade());
	}

	@Test
	public void testOrderConverter()
	{
		assertEquals(orderConverter, classUnderTest.getOrderConverter());
	}

	@Test
	public void testGetOrderForCode()
	{
		assertEquals(orderData, classUnderTest.getOrderForCode(code));
	}
	
	@Test
	public void testGetConfigurationId()
	{		
		ConfigModel configuration = classUnderTest.getConfiguration(code, itemNumber);
		assertEquals(configId, configuration.getId());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationIdNotExistingItemNo()
	{		
		classUnderTest.getConfiguration(code, "50");
	}	
	
	@Test
	public void testProductConfigurationService()
	{		
		assertEquals(productConfigurationService, classUnderTest.getProductConfigurationService());
	}	

}