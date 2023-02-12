package io.github.abhishekghoshh.core.rule;

import io.github.abhishekghoshh.core.model.DomainModel;

public interface Rule {
	public void process(DomainModel domainModel) throws Exception;
}
