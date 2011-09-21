package org.specs2.spring.web.webobject;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.FormTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author janm
 */
public class WebObject {
	private final byte[] content;
	private final Map<String, Object> model;
	private List<HtmlForm> forms;

	public WebObject(HttpServletRequest request, HttpServletResponse response,
					 byte[] content, ModelAndView modelAndView) {
		if (modelAndView != null) {
			this.model = modelAndView.getModel();
		} else {
			this.model = null;
		}
		this.content = content;
		parse();
	}

	private void parse() {
		try {
			this.forms = doGetForms();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	private BindingResult getRequiredBindingResultFor(String name) {
		return BindingResultUtils.getRequiredBindingResult(this.model, name);
	}

	private BindingResult getRequiredSingleBindingResult() {
		BindingResult result = null;
		for (Map.Entry<String, Object> e : this.model.entrySet()) {
			if (e.getValue() instanceof BindingResult) {
				if (result != null) throw new RuntimeException("More than one BindingResult found.");
				result = (BindingResult) e.getValue();
			}
		}
		if (result == null) throw new RuntimeException("No BindingResult found.");
		return result;
	}

	public String getHtml() {
		return new String(this.content);
	}

	public Object getModelAttribute(String name) {
		return getModelAttribute(name, Object.class);
	}

	public <T> T getModelAttribute(String name, Class<T> type) {
		final Object o = this.model.get(name);
		if (o == null) return null;
		if (o.getClass().isAssignableFrom(type)) throw new RuntimeException("");
		return (T) o;
	}

	public boolean hasFieldErrorFor(String path) {
		return getRequiredSingleBindingResult().hasFieldErrors(path);
	}

	private List<HtmlForm> doGetForms() throws ParserException {
		final Parser parser = Parser.createParser(getHtml(), "UTF-8");
		final List<HtmlForm> forms = new ArrayList<HtmlForm>();
		NodeFilter filter = new NodeClassFilter(FormTag.class);
		final NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		for (NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {
			final Node node = e.nextNode();
			forms.add(new HtmlForm((FormTag)node));
		}
		return forms;
	}

	/**
	 * Find fields that match the given selector and set their values
	 *
	 * @param selector the jQuery-style selector.
	 * @param values the values to be set to the field
	 */
	public void setValue(String selector, String... values) {
		getSingleForm().setValue(selector, values);
	}

	public HtmlForm getSingleForm() {
		if (this.forms.size() == 1) return this.forms.get(0);
		return null;
	}
}
