package com.ag.github.core.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ag.github.core.model.DomainModel;
import com.ag.github.core.model.ServiceDef;
import com.ag.github.core.model.StrategyType;
import com.ag.github.core.rule.Rule;

@Service
public class HandlerService {
	@Autowired
	@Qualifier("reactorThreadPool")
	TaskExecutor asyncExecutor;

	public void handle(DomainModel domainModel, List<ServiceDef> serviceDefs) throws Exception {
		for (ServiceDef serviceDef : serviceDefs) {
			List<Rule> rules = serviceDef.getRules();
			StrategyType strategyType = serviceDef.getStrategy();
			if (null == strategyType || StrategyType.SEQUENTIAL == strategyType) {
				for (Rule rule : rules)
					rule.process(domainModel);
			} else if (StrategyType.PARALLEL == strategyType) {
				ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
				forkJoinPool.submit(() -> {
					rules.parallelStream().forEach(rule -> {
						try {
							rule.process(domainModel);
						} catch (Exception e) {
							domainModel.setException(e, serviceDef.getName(), rule.getClass().getName());
						}
					});
				}, asyncExecutor).get();
			} else {
				CompletableFuture.runAsync(() -> {
					rules.parallelStream().forEach(rule -> {
						try {
							rule.process(domainModel);
						} catch (Exception e) {
							domainModel.setException(e, serviceDef.getName(), rule.getClass().getName());
						}
					});
				}, asyncExecutor);
			}
			if (serviceDef.isEndState() && null != domainModel.getResponseEntity())
				break;
			if (!CollectionUtils.isEmpty(domainModel.getExceptionBlocks()))
				break;
		}
	}
}
