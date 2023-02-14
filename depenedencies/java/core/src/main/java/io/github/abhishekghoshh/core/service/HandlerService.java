package io.github.abhishekghoshh.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import io.github.abhishekghoshh.core.model.DomainModel;
import io.github.abhishekghoshh.core.model.ServiceDef;
import io.github.abhishekghoshh.core.model.StrategyType;
import io.github.abhishekghoshh.core.rule.Rule;
import io.github.abhishekghoshh.core.rule.RuleRuntimeException;

@Service
public class HandlerService {
	@Autowired
	@Qualifier("reactorThreadPool")
	TaskExecutor asyncExecutor;

	private static final Logger log = LoggerFactory.getLogger(HandlerService.class);

	public void handle(DomainModel domainModel, List<ServiceDef> serviceDefs) throws Exception {
		for (ServiceDef serviceDef : serviceDefs) {
			List<Rule> rules = serviceDef.getRules();
			StrategyType strategyType = serviceDef.getStrategy();
			if (null == strategyType || StrategyType.SEQUENTIAL == strategyType) {
				for (Rule rule : rules) {
					process(domainModel, serviceDef, rule);
				}
			} else if (StrategyType.PARALLEL == strategyType) {
				ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
				Map<String, String> contextMap = getContextMap();
				try {
					forkJoinPool.submit(() -> {
						try {
							rules.parallelStream().forEach(rule -> {
								MDC.setContextMap(contextMap);
								process(domainModel, serviceDef, rule);
							});
						} catch (Exception exception) {
							throw exception;
						}
					}, asyncExecutor).get();
				} catch (ExecutionException exception) {
					if (null != exception.getCause() && exception.getCause() instanceof RuleRuntimeException)
						throw (RuleRuntimeException) exception.getCause();
					throw exception;
				} catch (Exception exception) {
					throw exception;
				}
			} else if (StrategyType.FIRE_AND_FORGET == strategyType) {
				Map<String, String> contextMap = getContextMap();
				CompletableFuture.runAsync(() -> {
					try {
						rules.parallelStream().forEach(rule -> {
							MDC.setContextMap(contextMap);
							process(domainModel, serviceDef, rule);
						});
					} catch (Exception exception) {
						throw exception;
					}
				}, asyncExecutor);
			}
			if (serviceDef.isEndState() && null != domainModel.getResponseEntity())
				break;
		}
	}

	private void process(DomainModel domainModel, ServiceDef serviceDef, Rule rule) {
		try {
			rule.process(domainModel);
		} catch (Exception exception) {
			log.debug("exeption {}, service def name {}, rule class name {}", exception.getMessage(),
					serviceDef.getName(), rule.getClass().getName());
			throw new RuleRuntimeException(exception, serviceDef.getName(), rule.getClass().getName());
		}
	}

	private Map<String, String> getContextMap() {
		if (null != MDC.getCopyOfContextMap())
			return MDC.getCopyOfContextMap();
		if (null != ThreadContext.getContext())
			return ThreadContext.getContext();
		return new HashMap<>();
	}

}
