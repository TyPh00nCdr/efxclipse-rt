/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.fx.xtext.sample.editor.java.text;

import java.util.List;

import org.eclipse.jface.text.rules.IRule;

public class SingleTokenJavaScanner extends AbstractJavaScanner{
	private String[] fProperty;

	public SingleTokenJavaScanner(String property) {
		super();
		fProperty= new String[] { property };
		initialize();
	}
	
	@Override
	protected List<IRule> createRules() {
		setDefaultReturnToken(getToken(fProperty[0]));
		return null;
	}

}
