package org.eclipse.fx.code.editor.ldef.langs.fx.java;

public class Java__java_single_line_comment extends org.eclipse.jface.text.rules.RuleBasedScanner {
	public Java__java_single_line_comment() {
		org.eclipse.jface.text.rules.Token java_doc_defaultToken = new org.eclipse.jface.text.rules.Token(new org.eclipse.jface.text.TextAttribute("java.java_doc_default"));
		setDefaultReturnToken(java_doc_defaultToken);
		org.eclipse.jface.text.rules.IRule[] rules = new org.eclipse.jface.text.rules.IRule[0];

		setRules(rules);
	}
}