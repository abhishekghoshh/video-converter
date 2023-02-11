package com.github.ag.core.rule;

import com.github.ag.core.model.DomainModel;

public interface Rule {
	public void process(DomainModel domainModel) throws Exception;
}
