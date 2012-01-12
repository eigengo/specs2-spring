package org.specs2.spring.web.webobject;

import org.htmlparser.Node;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author janm
 */
public class HtmlForm {
	private final String action;
	private final String method;
	private final List<InputField> inputs;

	HtmlForm(FormTag formTag) throws ParserException {
		this.action = formTag.getAttribute("action");
		String rawMethod = formTag.getAttribute("method");
		if (rawMethod == null) rawMethod = "GET";
		this.method = rawMethod.toUpperCase();

		this.inputs = new ArrayList<InputField>();
		final NodeList inputs = formTag.getFormInputs();
		for (NodeIterator e = inputs.elements(); e.hasMoreNodes();) {
			final Node node = e.nextNode();
			InputTag inputTag = (InputTag) node;
			String id = inputTag.getAttribute("id");
			if (id == null) id = inputTag.getAttribute("name");
			String value = inputTag.getAttribute("value");
			if (id == null) continue;

			InputField inputField = getOrCreateInputField(id);
			inputField.addValue(value);
			this.inputs.add(inputField);
		}
	}

	private InputField getOrCreateInputField(String id) {
		for (InputField inputField : this.inputs) {
			if (id.equals(inputField.getId())) return inputField;
		}
		return new InputField(id);
	}

	public void setValue(String selector, String... values) {
		if (!selector.startsWith("#")) {
			throw new UnsupportedOperationException("Only ID selectors for now");
		}
		String parsedSelector = selector.substring(1);
		getOrCreateInputField(parsedSelector).setValues(values);
	}

	public String getAction() {
		return this.action;
	}

	public String getMethod() {
		return this.method;
	}

	public List<InputField> getInputs() {
		return this.inputs;
	}
}
