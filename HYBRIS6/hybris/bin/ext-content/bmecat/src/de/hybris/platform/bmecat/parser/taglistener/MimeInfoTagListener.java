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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.Collection;
import java.util.Collections;


/**
 * Parses the &lt;MimeInfo&gt; tag
 * 
 * 
 * 
 */
public class MimeInfoTagListener extends DefaultBMECatTagListener
{
	public MimeInfoTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Collections.singleton(new MimeTagListener(this));
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		return getSubTagValueCollection(BMECatConstants.XML.TAG.MIME);
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.MIME_INFO;
	}
}