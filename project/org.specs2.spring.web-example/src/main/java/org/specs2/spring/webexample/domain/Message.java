package org.specs2.spring.webexample.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author janm
 */
@Entity
public class Message {
	@Id
	@GeneratedValue
	private Long id;
	private String sourceText;
	private String processedText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public String getProcessedText() {
		return processedText;
	}

	public void setProcessedText(String processedText) {
		this.processedText = processedText;
	}
}
