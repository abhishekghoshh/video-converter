package com.ag.github.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import com.ag.github.core.configuration.ReactorDefination;
import com.ag.github.core.model.ApiDef;
import com.ag.github.core.model.DomainModel;
import com.ag.github.core.model.ExceptionBlock;
import com.ag.github.core.model.ServiceDef;
import com.ag.github.core.rule.Rule;
import com.ag.github.core.service.ApplicationContextManager;
import com.ag.github.core.service.HandlerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ControllerHandler {

	@Autowired
	HandlerService handlerService;
	@Autowired
	ReactorDefination reactorDefination;

	private static final Logger log = LoggerFactory.getLogger(ControllerHandler.class);

	public ControllerHandler(@Autowired RequestMappingHandlerMapping requestMappingHandlerMapping,
			@Autowired ReactorDefination reactorDefination,
			@Autowired ApplicationContextManager applicationContextManager)
			throws NoSuchMethodException, ClassNotFoundException {

		RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
		options.setPatternParser(new PathPatternParser());

		Map<String, String> apiEntryMap = new HashMap<>();
		Map<String, ApiDef> apidefs = reactorDefination.getApidef();
		for (Map.Entry<String, ApiDef> apidefEntry : apidefs.entrySet()) {
			ApiDef apidef = apidefEntry.getValue();
			if (null == apidef.getPath() && null == apidef.getMethod())
				throw new RuntimeException();

			String apidefKey = apidefEntry.getKey();
			apidef.setKey(buildApiKey(apidef.getPath(), apidef.getMethod().toString()));
			apiEntryMap.put(apidef.getKey(), apidefKey);

			log.info("api-key is {}, api-def-key is {}, api {}, method {} ", apidef.getKey(), apidefKey,
					apidef.getPath(), apidef.getMethod());

			requestMappingHandlerMapping.registerMapping(
					RequestMappingInfo.paths(apidef.getPath()).methods(apidef.getMethod()).consumes(MediaType.ALL_VALUE)
							.produces(MediaType.ALL_VALUE).options(options).build(),
					this, ControllerHandler.class.getMethod("control", HttpServletRequest.class));

			if (CollectionUtils.isEmpty(apidef.getServicedef()))
				throw new RuntimeException("Service definition can not be empty");
			for (ServiceDef servicedef : apidef.getServicedef()) {
				if (CollectionUtils.isEmpty(servicedef.getClasses()))
					throw new RuntimeException("Class list is empty for " + servicedef.getName());
				if (CollectionUtils.isEmpty(servicedef.getRules()))
					servicedef.setRules(new ArrayList<>());
				for (String className : servicedef.getClasses())
					servicedef.addRule((Rule) applicationContextManager.get(Class.forName(className)));
			}
		}
		reactorDefination.setApiEntryMap(apiEntryMap);
	}

	private String buildApiKey(String path, String method) {
		return method + path.replace('/', '_').replace('-', '_').toUpperCase();
	}

	public ResponseEntity<?> control(HttpServletRequest httpServletRequest) throws Exception {
		String apidefKey = reactorDefination.getApiEntryMap()
				.get(buildApiKey(httpServletRequest.getRequestURI(), httpServletRequest.getMethod()));
		ApiDef apiDef = reactorDefination.getApidef().get(apidefKey);
		DomainModel domainModel = null;
		if (null != apiDef.getRequestBodyClass())
			domainModel = new DomainModel(httpServletRequest, apiDef.getRequestBodyClass());
		else
			domainModel = new DomainModel(httpServletRequest);
		handlerService.handle(domainModel, apiDef.getServicedef());
		if (!CollectionUtils.isEmpty(domainModel.getExceptionBlocks()))
			throw buildException(domainModel.getExceptionBlocks());
		return domainModel.getResponseEntity();
	}

	private Exception buildException(List<ExceptionBlock> exceptionBlocks) {
		return null;
	}
}
