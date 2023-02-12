package io.github.abhishekghoshh.core.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.github.abhishekghoshh.core.model.ApiDef;

@Configuration
@ConfigurationProperties("reactor.properties")
public class ReactorDefination {
	private Map<String, ApiDef> apidef = new HashMap<>();
	private Map<String, String> apiEntry = null;

	public Map<String, ApiDef> getApidef() {
		return apidef;
	}

	public Map<String, String> getApiEntryMap() {
		return apiEntry;
	}

	public void setApiEntryMap(Map<String, String> apiEntry) {
		if (null == this.apiEntry)
			this.apiEntry = Collections.unmodifiableMap(apiEntry);
	}

	public String getApiEntryKey(String apiKey) {
		if (null == apiEntry)
			return null;
		return apiEntry.get(apiKey);
	}
}
