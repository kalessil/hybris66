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
package de.hybris.platform.adaptivesearch.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContextFactory;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileCalculationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class CategoryRemovalTest extends ServicelayerTransactionalTest
{
	private final static String CATALOG_ID = "hwcatalog";
	private final static String VERSION_STAGED = "Staged";

	private static final String INDEX_CONFIGURATION = "indexConfiguration";
	private static final String INDEX_TYPE = "index1";

	private static final String CAT_AWARE_SEARCH_PROFILE_CODE = "categoryAwareProfile";

	private static final String CAT10_CODE = "crCat10";
	private static final String CAT20_CODE = "crCat20";

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";
	private static final String PRODUCT3_CODE = "product3";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private AsSearchProfileService asSearchProfileService;

	@Resource
	private AsSearchConfigurationService asSearchConfigurationService;

	@Resource
	private AsSearchProfileCalculationService asSearchProfileCalculationService;

	@Resource
	private AsSearchProfileContextFactory asSearchProfileContextFactory;

	private CatalogVersionModel catalogVersion;

	private CategoryModel category10;
	private CategoryModel category20;

	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/categoryRemovalTest.impex", "utf-8");

		catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);

		category10 = categoryService.getCategoryForCode(catalogVersion, CAT10_CODE);
		category20 = categoryService.getCategoryForCode(catalogVersion, CAT20_CODE);

		product1 = productService.getProductForCode(catalogVersion, PRODUCT1_CODE);
		product2 = productService.getProductForCode(catalogVersion, PRODUCT2_CODE);
		product3 = productService.getProductForCode(catalogVersion, PRODUCT3_CODE);
	}

	@Test
	public void calculateSearchProfile() throws Exception
	{
		// given
		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10, category20));

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(searchProfileContext,
				Collections.singletonList(searchProfile));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedItems());
		assertEquals(3, result.getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = result.getPromotedItems().values().iterator();

		final AsPromotedItem promotedItem1 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem1.getItemPk());

		final AsPromotedItem promotedItem2 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product2.getPk(), promotedItem2.getItemPk());

		final AsPromotedItem promotedItem3 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product3.getPk(), promotedItem3.getItemPk());
	}

	@Test
	public void calculateSearchProfileAfterRemovingCat10() throws Exception
	{
		// given
		removeCategory(category10);

		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10, category20));

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(searchProfileContext,
				Collections.singletonList(searchProfile));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedItems());
		assertEquals(2, result.getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = result.getPromotedItems().values().iterator();

		final AsPromotedItem promotedItem1 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem1.getItemPk());

		final AsPromotedItem promotedItem2 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product3.getPk(), promotedItem2.getItemPk());
	}

	@Test
	public void calculateSearchProfileAfterRemovingCat20() throws Exception
	{
		// given
		removeCategory(category20);

		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10, category20));

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(searchProfileContext,
				Collections.singletonList(searchProfile));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedItems());
		assertEquals(2, result.getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = result.getPromotedItems().values().iterator();

		final AsPromotedItem promotedItem1 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem1.getItemPk());

		final AsPromotedItem promotedItem2 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product2.getPk(), promotedItem2.getItemPk());
	}

	@Test
	public void calculateSearchProfileAfterRemovingCat10AndCat20() throws Exception
	{
		// given
		removeCategory(category10);
		removeCategory(category20);

		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10, category20));

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(searchProfileContext,
				Collections.singletonList(searchProfile));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedItems());
		assertEquals(1, result.getPromotedItems().size());
		final Iterator<AsConfigurationHolder<AsPromotedItem>> promotedItemsIterator = result.getPromotedItems().values().iterator();

		final AsPromotedItem promotedItem1 = promotedItemsIterator.next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem1.getItemPk());
	}

	@Test
	public void getSearchConfigurationForGlobalCat() throws Exception
	{
		// given
		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Collections.emptyList());

		// when
		final Optional<AbstractAsSearchConfigurationModel> searchConfigurationResult = asSearchConfigurationService
				.getSearchConfigurationForContext(searchProfileContext, searchProfile);

		// then
		assertNotNull(searchConfigurationResult);
		assertTrue(searchConfigurationResult.isPresent());
	}

	@Test
	public void getSearchConfigurationForGlobalCatAfterRemovingCat10AndCat20() throws Exception
	{
		// given
		removeCategory(category10);
		removeCategory(category20);

		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Collections.emptyList());

		// when
		final Optional<AbstractAsSearchConfigurationModel> searchConfigurationResult = asSearchConfigurationService
				.getSearchConfigurationForContext(searchProfileContext, searchProfile);

		// then
		assertNotNull(searchConfigurationResult);
		assertTrue(searchConfigurationResult.isPresent());
	}

	@Test
	public void getSearchConfigurationForCat10() throws Exception
	{
		// given
		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10));

		// when
		final Optional<AbstractAsSearchConfigurationModel> searchConfigurationResult = asSearchConfigurationService
				.getSearchConfigurationForContext(searchProfileContext, searchProfile);

		// then
		assertNotNull(searchConfigurationResult);
		assertTrue(searchConfigurationResult.isPresent());
	}

	@Test
	public void getSearchConfigurationForCat10AfterRemovingCat10() throws Exception
	{
		// given
		removeCategory(category10);

		final AbstractAsSearchProfileModel searchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CAT_AWARE_SEARCH_PROFILE_CODE).get();
		final AsSearchProfileContext searchProfileContext = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION,
				INDEX_TYPE, Collections.singletonList(catalogVersion), Arrays.asList(category10));

		// when
		final Optional<AbstractAsSearchConfigurationModel> searchConfigurationResult = asSearchConfigurationService
				.getSearchConfigurationForContext(searchProfileContext, searchProfile);

		// then
		assertNotNull(searchConfigurationResult);
		assertFalse(searchConfigurationResult.isPresent());
	}

	protected void removeCategory(final CategoryModel category)
	{
		modelService.remove(category);
	}
}