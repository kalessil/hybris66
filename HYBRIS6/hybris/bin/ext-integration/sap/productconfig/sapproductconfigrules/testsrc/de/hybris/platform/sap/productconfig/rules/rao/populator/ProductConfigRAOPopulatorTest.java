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
package de.hybris.platform.sap.productconfig.rules.rao.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.rules.ConfigurationRulesTestData;
import de.hybris.platform.sap.productconfig.rules.rao.CsticRAO;
import de.hybris.platform.sap.productconfig.rules.rao.ProductConfigRAO;
import de.hybris.platform.sap.productconfig.rules.service.impl.ProductConfigRuleFormatTranslatorImpl;
import de.hybris.platform.sap.productconfig.rules.service.impl.ProductConfigRuleUtilImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ProductConfigRAOPopulatorTest
{

	private ProductConfigRAOPopulator classUnderTest;

	@Before
	public void setUp()
	{
		classUnderTest = new ProductConfigRAOPopulator();
		classUnderTest.setRulesFormator(new ProductConfigRuleFormatTranslatorImpl());
		classUnderTest.setRuleUtil(new ProductConfigRuleUtilImpl());
	}

	@Test
	public void testPopulator_csticWithAssignableValues()
	{
		final ConfigModel configModel = ConfigurationRulesTestData.createConfigModelWith2GroupAndAssignedValues();
		final ProductConfigRAO target = new ProductConfigRAO();
		classUnderTest.populate(configModel, target);
		final List<CsticRAO> csticRAOs = target.getCstics();
		assertEquals("Number of cstic RAOs is incorrect", 9, csticRAOs.size());
		// number of value RAOs
		assertEquals("Number of assigned values is incorrect", 1, csticRAOs.get(0).getAssignedValues().size());
		assertEquals("Number of assignable values is incorrect", 3, csticRAOs.get(0).getAssignableValues().size());
		assertEquals("Number of assigned values is incorrect", 1, csticRAOs.get(1).getAssignedValues().size());
		assertEquals("Number of assignable values is incorrect", 3, csticRAOs.get(1).getAssignableValues().size());
		assertEquals("Number of assigned values is incorrect", 1, csticRAOs.get(4).getAssignedValues().size());
		assertEquals("Number of assignable values is incorrect", 4, csticRAOs.get(4).getAssignableValues().size());
		assertEquals("Number of assigned values is incorrect", 2, csticRAOs.get(5).getAssignedValues().size());
		assertEquals("Number of assignable values is incorrect", 4, csticRAOs.get(5).getAssignableValues().size());
		// Check Cstic RAOs in detail
		final CsticRAO csticRao = csticRAOs.get(3);
		assertEquals("Cstic Name is incorrect", "CSTIC_1.4", csticRao.getCsticName());
		assertEquals("Cstic Value Name is incorrect", "VAL1", csticRao.getAssignableValues().get(0).getCsticValueName());
		assertEquals("Cstic Value Name is incorrect", "VAL3", csticRao.getAssignedValues().get(1).getCsticValueName());
		assertEquals("Assigned and Assignable cstic value rao have to be the same object", csticRao.getAssignableValues().get(2),
				csticRao.getAssignedValues().get(1));

	}

	@Test
	public void testPopulator_csticWithOutAssignableValues()
	{
		final ConfigModel configModel = ConfigurationRulesTestData.createConfigModelWithCstic();
		final ProductConfigRAO target = new ProductConfigRAO();
		classUnderTest.populate(configModel, target);
		final List<CsticRAO> csticRAOs = target.getCstics();
		assertEquals("Number of cstic RAOs is incorrect", 1, csticRAOs.size());
		// number of value RAOs
		assertEquals("Number of assigned values is incorrect", 1, csticRAOs.get(0).getAssignedValues().size());
		assertEquals("Number of assignable values is incorrect", 0, csticRAOs.get(0).getAssignableValues().size());
		// Check Cstic RAOs in detail
		final CsticRAO csticRao = csticRAOs.get(0);
		assertEquals("Cstic Name is incorrect", ConfigurationRulesTestData.STRING_CSTIC, csticRao.getCsticName());
		assertEquals("Cstic Value Name is incorrect", ConfigurationRulesTestData.STRING_CSTIC_VALUE,
				csticRao.getAssignedValues().get(0).getCsticValueName());
	}

	@Test
	public void testPopulator_numericCstic()
	{
		final ConfigModel configModel = ConfigurationRulesTestData.createConfigModelWithNumericCstic();
		final ProductConfigRAO target = new ProductConfigRAO();
		classUnderTest.populate(configModel, target);
		final List<CsticRAO> csticRAOs = target.getCstics();
		final CsticRAO csticRao = csticRAOs.get(0);
		assertEquals("1500.0 in engine format is 1,500 in rules format (english locale!)", "1,500",
				csticRao.getAssignedValues().get(0).getCsticValueName());
	}

	@Test
	public void testPopulator_assignableValueNumeric()
	{
		final ConfigModel configModel = ConfigurationRulesTestData.createConfigModelWithNumericCsticAssignable();
		final ProductConfigRAO target = new ProductConfigRAO();
		classUnderTest.populate(configModel, target);
		final List<CsticRAO> csticRAOs = target.getCstics();
		final CsticRAO csticRao = csticRAOs.get(0);
		assertEquals("1500.0 final in engine format is 1,500 final in rules format (english locale!)", "1,500",
				csticRao.getAssignableValues().get(0).getCsticValueName());
	}
}