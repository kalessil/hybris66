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
package de.hybris.platform.solrfacetsearch.integration;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import org.junit.Test;


public class SearchQueryFilterQueriesTest extends AbstractSearchQueryTest
{
	@Override
	protected void loadData() throws Exception
	{
		importConfig("/test/integration/SearchQueryFilterQueriesTest.csv");
	}

	@Test
	public void addFilterQuery1() throws Exception
	{
		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_CODE_FIELD, PRODUCT1_CODE);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT1_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT1_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQuery2() throws Exception
	{
		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_CODE_FIELD, PRODUCT2_CODE);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT2_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT2_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQueryWithEscaping1() throws Exception
	{
		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_NAME_WITH_RESERVED_CHARS_FIELD, PRODUCT1_NAME);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT1_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT1_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQueryWithEscaping2() throws Exception
	{
		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_NAME_WITH_RESERVED_CHARS_FIELD, PRODUCT2_NAME);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT2_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT2_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQuery1ForLegacyMode() throws Exception
	{
		// given
		enabledSearchLegacyMode();

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_CODE_FIELD, PRODUCT1_CODE);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT1_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT1_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQuery2ForLegacyMode() throws Exception
	{
		// given
		enabledSearchLegacyMode();

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_CODE_FIELD, PRODUCT2_CODE);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT2_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT2_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQueryWithEscaping1ForLegacyMode() throws Exception
	{
		// given
		enabledSearchLegacyMode();

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_NAME_WITH_RESERVED_CHARS_FIELD, PRODUCT1_NAME);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT1_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT1_NAME, document, PRODUCT_NAME_FIELD);
	}

	@Test
	public void addFilterQueryWithEscaping2ForLegacyMode() throws Exception
	{
		// given
		enabledSearchLegacyMode();

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addFilterQuery(PRODUCT_NAME_WITH_RESERVED_CHARS_FIELD, PRODUCT2_NAME);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document = searchResult.getDocuments().get(0);
		assertDocumentField(PRODUCT2_CODE, document, PRODUCT_CODE_FIELD);
		assertDocumentField(PRODUCT2_NAME, document, PRODUCT_NAME_FIELD);
	}
}