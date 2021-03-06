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
package com.hybris.ymkt.segmentation.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.servicelayer.session.SessionService;
import com.hybris.ymkt.common.consent.YmktConsentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hybris.ymkt.common.consent.YmktConsentService;
import com.hybris.ymkt.segmentation.dto.SAPInitiative;
import com.hybris.ymkt.segmentation.services.InitiativeService;
import com.hybris.ymkt.segmentation.model.CMSYmktCampaignRestrictionModel;


/**
 *
 */
@UnitTest
public class CampaignRestrictionEvaluatorTest
{
	@Mock
	private SessionService sessionService;
	
	@Mock
	private RestrictionData context;
	
	@Mock
	private InitiativeService initiativeService;
	
	@Mock
	private YmktConsentService ymktConsentService;
	
	private CampaignRestrictionEvaluator campaignRestrictionEvaluator = new CampaignRestrictionEvaluator();
	
	private static final String USER_ID = "6de4ae57e795a737";
	
	List<String> sessionCampaigns = new ArrayList<>();
	List<String> backendCampaigns = new ArrayList<>();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		campaignRestrictionEvaluator.setInitiativeService(initiativeService);
		campaignRestrictionEvaluator.setSessionService(sessionService);
		campaignRestrictionEvaluator.setYmktConsentService(ymktConsentService);
		
		sessionCampaigns.add("campaign1");
		backendCampaigns.add("campaign2");
		
		given(initiativeService.getInteractionContactId()).willReturn(USER_ID);
		given(ymktConsentService.getUserConsent(any())).willReturn(true);
	}

	@After
	public void teardown()
	{
		sessionCampaigns.clear();
		backendCampaigns.clear();
	}

	@Test
	public void testUserBelongsToCampaignFromSession()
	{
		//setup restriction
		final CMSYmktCampaignRestrictionModel campaignRestriction = new CMSYmktCampaignRestrictionModel();
		campaignRestriction.setCampaign("campaign1");
		campaignRestriction.setMemberOfCampaign(true);
		
		//return "campaign1" when getting from session
		given(sessionService.getAttribute(any())).willReturn(sessionCampaigns);
		
		//expect true since there we return a session campaign
		assertTrue(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
		
		//test excluded
		campaignRestriction.setMemberOfCampaign(false);
		assertFalse(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
	}
	
	@Test
	public void testUserBelongsToCampaignFromBackend() throws IOException
	{
		//return initiatives list when getting from back end
		List<SAPInitiative> initiatives = new ArrayList<>();
		final SAPInitiative initiative1 = new SAPInitiative();
		initiative1.setId("campaign2");
		initiative1.setMemberCount("10");
		initiative1.setName("campaign2");
		initiatives.add(initiative1);
		given(initiativeService.getInitiatives(any())).willReturn(initiatives);
		given(ymktConsentService.getUserConsent(any())).willReturn(true);
		
		//setup restriction
		final CMSYmktCampaignRestrictionModel campaignRestriction = new CMSYmktCampaignRestrictionModel();
		campaignRestriction.setCampaign("campaign2");
		campaignRestriction.setMemberOfCampaign(true);
		
		//return null when getting from session (simulates no session key found)
		given(sessionService.getAttribute(any())).willReturn(null);
		
		//return "campaign2" when getting from back end
		given(sessionService.getOrLoadAttribute(any(), any())).willReturn(backendCampaigns);
		
		//expect true since we return a back end campaign
		assertTrue(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
		
		//test with excluded
		campaignRestriction.setMemberOfCampaign(false);
		assertFalse(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
	}

	@Test
	public void testUserBelongsToZeroCampaigns() throws IOException
	{		
		//return empty list when getting from back end
		given(initiativeService.getInitiatives(any())).willReturn(Collections.emptyList());

		//setup restriction
		final CMSYmktCampaignRestrictionModel campaignRestriction = new CMSYmktCampaignRestrictionModel();
		campaignRestriction.setCampaign("campaign0");
		campaignRestriction.setMemberOfCampaign(true);

		//return nothing when getting from session
		given(sessionService.getAttribute(any())).willReturn(Collections.emptyList());
		
		//return nothing getting from back end
		given(sessionService.getOrLoadAttribute(any(), any())).willReturn(Collections.emptyList());
		
		//expect false since both session and back end lists are empty
		assertFalse(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
		
		//test with excluded
		campaignRestriction.setMemberOfCampaign(false);
		assertTrue(campaignRestrictionEvaluator.evaluate(campaignRestriction, context));
	}
}
