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
package com.hybris.ymkt.sapprodrecowebservices.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.ymkt.sapprodrecowebservices.facades.RecommendationPopulatorFacade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import de.hybris.platform.cmsfacades.data.OptionData;


@Controller
@Api(tags = "RecommendationScenarios")
public class RecommendationScenariosController {

	@Resource(name = "recommendationPopulatorFacade")
	protected RecommendationPopulatorFacade recommendationPopulatorFacade;

	
	/**
	 * Retrieve dropdown values via oData and return content
	 *  
	 * @param sourceType: Dropdown to fill
	 * @return JSON containing option data list
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @throws IOException 
	 */
	
	@RequestMapping(value = "/data/product/{sourceField}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Method with sourceField parameter returning a JSON string", notes = "Returns values to populate given dropdown field", produces = "application/json")
	public String populateDropdown(
			@ApiParam(value = "Dropdown field to fill. Determines the data to be returned", required = true)
			@PathVariable String sourceField) throws IOException
	{
		Map<String, List<OptionData>> map = new HashMap<String, List<OptionData>>();
		List<OptionData> optionDataList = recommendationPopulatorFacade.populateDropDown(sourceField);
		
		//construct JSON for dropdowns
		map.put("options", optionDataList);
		return new ObjectMapper().writeValueAsString(map);
	}
	
	public void setRecommendationPopulatorFacade(RecommendationPopulatorFacade recommendationPopulatorFacade) {
		this.recommendationPopulatorFacade = recommendationPopulatorFacade;
	}

}