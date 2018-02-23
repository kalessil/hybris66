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
package de.hybris.platform.acceleratorfacades.cart.action.exceptions;

import de.hybris.platform.acceleratorfacades.cart.action.CartEntryActionHandler;


/**
 * A general exception used by {@link CartEntryActionHandler#handleAction(long)} when an error occurs.
 */
public class CartEntryActionException extends Exception
{
	public CartEntryActionException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public CartEntryActionException(final String message)
	{
		super(message);
	}
}