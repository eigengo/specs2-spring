package org.specs2.spring.web;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
* @author janm
*/
class AbsoluteFilesystemResourceLoader extends DefaultResourceLoader {

	@Override
	protected Resource getResourceByPath(String path) {
		return new FileSystemContextResource(path);
	}
}
