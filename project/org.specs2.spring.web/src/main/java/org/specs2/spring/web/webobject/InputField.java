package org.specs2.spring.web.webobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author janm
 */
public class InputField {
	private String id;
	private List<String> values;

	InputField(String id) {
		this.id = id;
		this.values = new ArrayList<String>();
	}

	void addValue(String value) {
		this.values.add(value);
	}

	public String[] getValues() {
		return this.values.toArray(new String[this.values.size()]);
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		InputField that = (InputField) o;

		if (!this.id.equals(that.id)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public void setValues(String[] values) {
		this.values.clear();
		this.values.addAll(Arrays.asList(values));
	}
}
