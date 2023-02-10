package com.ag.github.producer.service;

import com.ag.github.producer.model.DomainModel;

public interface Rule {
    public DomainModel process(DomainModel domainModel);
}
