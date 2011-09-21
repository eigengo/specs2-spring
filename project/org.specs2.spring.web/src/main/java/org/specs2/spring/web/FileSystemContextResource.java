package org.specs2.spring.web;

import org.springframework.core.io.ContextResource;
import org.springframework.core.io.FileSystemResource;

/**
 * FileSystemResource that explicitly expresses a context-relative path
 * through implementing the ContextResource interface.
 */
class FileSystemContextResource extends FileSystemResource implements ContextResource {

	public FileSystemContextResource(String path) {
		super(path);
	}

	public String getPathWithinContext() {
		return getPath();
	}
}
