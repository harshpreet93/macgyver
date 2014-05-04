package io.macgyver.web.rythm;

import java.io.IOException;
import java.net.URL;

import org.rythmengine.extension.ITemplateResourceLoader;
import org.rythmengine.resource.ClasspathTemplateResource;
import org.rythmengine.resource.ITemplateResource;
import org.rythmengine.resource.TemplateResourceBase;
import org.rythmengine.utils.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class CustomClasspathTemplateResource extends TemplateResourceBase
		implements ITemplateResource {

	private static final long serialVersionUID = -6349332717233870055L;

	private URL url;
	private String key;
	Logger log = LoggerFactory.getLogger(CustomClasspathTemplateResource.class);

	public CustomClasspathTemplateResource(String path,
			ITemplateResourceLoader loader) {
		super(loader);

		path = MacGyverRythmResourceLoader.stripLeadingSlash(path);
		path = MacGyverRythmResourceLoader.convertDotsToSlash(path);
		try {
			if (log.isDebugEnabled()) {
				log.debug("searching classpath for: {}",path);
			}
			ClassPathResource ur = new ClassPathResource(path,loader.getEngine().classLoader());
			url = ur.getURL();
			if (log.isDebugEnabled()) {
				log.debug("resolved {} to {}", path, url);
			}

		} catch (IOException e) {
			// this isn't really a "problem"
			log.debug("problem resolving template: "+e.toString());
		}
		
		key = path;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String reload() {
		return IO.readContentAsString(url);
	}

	@Override
	protected long lastModified() {
		if (getEngine().isProdMode())
			return 0;

		String fileName;
		if ("file".equals(url.getProtocol())) {
			fileName = url.getFile();
		} else if ("jar".equals(url.getProtocol())) {
			try {
				java.net.JarURLConnection jarUrl = (java.net.JarURLConnection) url
						.openConnection();
				fileName = jarUrl.getJarFile().getName();
			} catch (Exception e) {
				return System.currentTimeMillis() + 1;
			}
		} else {
			return System.currentTimeMillis() + 1;
		}

		java.io.File file = new java.io.File(fileName);
		return file.lastModified();
	}

	@Override
	public boolean isValid() {
		return null != url;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof ClasspathTemplateResource) {
			ClasspathTemplateResource that = (ClasspathTemplateResource) obj;
			return that.getKey().equals(this.getKey());
		}
		return false;
	}

	@Override
	protected long defCheckInterval() {
		return -1;
	}

	@Override
	protected Long userCheckInterval() {
		return Long.valueOf(1000 * 5);
	}

	@Override
	public String getSuggestedClassName() {
		return path2CN(key);
	}

	public boolean exists() {
		return url != null;
	}

}