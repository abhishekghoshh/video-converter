package com.ag.github.producer.controller;

import com.ag.github.producer.model.DomainModel;
import com.ag.github.producer.service.HandlerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

@RestController
public class ControllerHandler {

    @Autowired
    HandlerService handlerService;

    public ControllerHandler(@Autowired RequestMappingHandlerMapping requestMappingHandlerMapping) throws NoSuchMethodException {
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());
        requestMappingHandlerMapping.registerMapping(
                RequestMappingInfo.paths("/convert/to-audio")
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.ALL_VALUE)
                        .produces(MediaType.ALL_VALUE)
                        .options(options)
                        .build(),
                this,
                ControllerHandler.class.getMethod("control", HttpServletRequest.class)
        );
    }

    @RequestMapping(path = "/convert/to-audio", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity control(HttpServletRequest httpServletRequest)
            throws Exception {
        DomainModel domainModel = new DomainModel(httpServletRequest);
        try {
            handlerService.handle(domainModel);
            return domainModel.getResponseEntity();
        } catch (Exception exception) {
            if (null != domainModel.getException())
                throw domainModel.getException();
            throw exception;
        }
    }
}
