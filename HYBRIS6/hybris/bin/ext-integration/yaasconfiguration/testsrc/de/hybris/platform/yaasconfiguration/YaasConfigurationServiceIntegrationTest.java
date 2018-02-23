/*
* [y] hybris Platform
*
* Copyright (c) 2017 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/
package de.hybris.platform.yaasconfiguration;

import static de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants.YAAS_CLIENT_SCOPE;
import static de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants.YAAS_CLIENT_URL;
import static de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants.YAAS_OAUTH_CLIENTID;
import static de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants.YAAS_OAUTH_CLIENTSECRET;
import static de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants.YAAS_OAUTH_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.yaasconfiguration.client.ProductClient;
import de.hybris.platform.yaasconfiguration.model.YaasClientCredentialModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;
import de.hybris.platform.yaasconfiguration.model.YaasServiceModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class YaasConfigurationServiceIntegrationTest extends ServicelayerTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private YaasConfigurationService yaasConfigurationService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();


	@Test
	public void testGetYaasClientForId() throws Exception
	{
		createYaasService();

		final YaasServiceModel clientModel = yaasConfigurationService.getYaasServiceForId("ProductClient");

		assertEquals(clientModel.getIdentifier(), "ProductClient");
		assertEquals(clientModel.getServiceURL(), "https://api.yaas.io/hybris/product/v2");
		assertEquals(clientModel.getServiceScope(), "hybris.product_read_unpublished");
	}

	@Test
	public void testGetClientCredentialForId() throws Exception
	{
		createYaasClientCredential("devapplication");


		final YaasClientCredentialModel yaasClientCredential = yaasConfigurationService
				.getYaasClientCredentialForId("devapplication");

		assertEquals(yaasClientCredential.getIdentifier(), "devapplication");
		assertEquals(yaasClientCredential.getClientId(), "5jfdsAxDYBJJc5DEtoN9rtZdxfrF7h5R");
		assertEquals(yaasClientCredential.getClientSecret(), "4MAKkTecaOFewdlH");
		assertEquals(yaasClientCredential.getOauthURL(), "https://api.yaas.io/hybris/oauth2/v1");
	}

	@Test
	public void testBuildYaasConfig_invalidIds() throws Exception
	{
		final YaasClientCredentialModel clientCredential = createYaasClientCredential("ClientCredential2");
		createYaasService();

		errorMustBeReported(
				"No result for the given example [YaasServiceModel (<unsaved>)] was found. Searched with these attributes: {identifier=String}");

		yaasConfigurationService.buildYaasConfig(clientCredential, "invalid".getClass());

	}


	@Test
	public void testBuildYaasConfig_validIds() throws Exception
	{
		final YaasClientCredentialModel clientCredential = createYaasClientCredential("ClientCredential1");
		createYaasService();

		final Map<String, String> map = yaasConfigurationService.buildYaasConfig(clientCredential, ProductClient.class);

		assertEquals(map.get(YAAS_OAUTH_CLIENTID), "5jfdsAxDYBJJc5DEtoN9rtZdxfrF7h5R");
		assertEquals(map.get(YAAS_OAUTH_CLIENTSECRET), "4MAKkTecaOFewdlH");
		assertEquals(map.get(YAAS_OAUTH_URL), "https://api.yaas.io/hybris/oauth2/v1");


		assertEquals(map.get(YAAS_CLIENT_URL), "https://api.yaas.io/hybris/product/v2");
		assertEquals(map.get(YAAS_CLIENT_SCOPE), "hybris.product_read_unpublished");

	}



	private YaasClientCredentialModel createYaasClientCredential(final String id)
	{
		final YaasProjectModel yaasProject = modelService.create(YaasProjectModel.class);
		yaasProject.setIdentifier("devproject");

		final YaasClientCredentialModel yaasClientCredential = modelService.create(YaasClientCredentialModel.class);

		yaasClientCredential.setIdentifier(id);
		yaasClientCredential.setClientId("5jfdsAxDYBJJc5DEtoN9rtZdxfrF7h5R");
		yaasClientCredential.setClientSecret("4MAKkTecaOFewdlH");
		yaasClientCredential.setOauthURL("https://api.yaas.io/hybris/oauth2/v1");
		yaasClientCredential.setYaasProject(yaasProject);

		modelService.save(yaasClientCredential);

		return yaasClientCredential;
	}

	private void createYaasService()
	{

		final YaasServiceModel yaasService = (YaasServiceModel) modelService.create(YaasServiceModel.class);

		yaasService.setIdentifier("ProductClient");
		yaasService.setServiceURL("https://api.yaas.io/hybris/product/v2");
		yaasService.setServiceScope("hybris.product_read_unpublished");

		modelService.save(yaasService);
	}

	private void errorMustBeReported(final String msg)
	{
		expectedException.expectMessage(containsString(msg));
	}

}