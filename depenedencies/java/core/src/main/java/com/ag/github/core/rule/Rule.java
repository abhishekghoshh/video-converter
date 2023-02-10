package com.ag.github.core.rule;

import com.ag.github.core.model.DomainModel;

public interface Rule {
	public void process(DomainModel domainModel) throws Exception;
}
