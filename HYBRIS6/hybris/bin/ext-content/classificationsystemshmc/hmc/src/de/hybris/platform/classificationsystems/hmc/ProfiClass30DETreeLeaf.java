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
package de.hybris.platform.classificationsystems.hmc;

import de.hybris.platform.classificationsystems.constants.ClassificationsystemsConstants;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;



public class ProfiClass30DETreeLeaf extends ClassificationImportTreeLeaf
{
	public ProfiClass30DETreeLeaf(final DisplayState displayState, final Chip parent)
	{
		super(displayState, parent, ClassificationsystemsConstants.PROFICLASS_3_0_DE, "startproficlass30DE");
	}
}