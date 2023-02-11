package com.github.ag.core.model;

import java.util.ArrayList;
import java.util.List;

import com.github.ag.core.rule.Rule;

public class ServiceDef {
	private String name;
	private List<String> classes;
	private List<Rule> rules;
	private StrategyType strategy = StrategyType.SEQUENTIAL;
	private boolean endState = false;

	public ServiceDef() {
		super();
		this.rules = new ArrayList<>();
	}

	public ServiceDef(String name, List<String> classes, StrategyType strategy, boolean endState) {
		super();
		this.name = name;
		this.classes = classes;
		this.strategy = strategy;
		this.endState = endState;
		this.rules = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public StrategyType getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyType strategy) {
		this.strategy = strategy;
	}

	public boolean isEndState() {
		return endState;
	}

	public void setEndState(boolean endState) {
		this.endState = endState;
	}

	public ServiceDef addRule(Rule rule) {
		if (null != rule)
			this.rules.add(rule);
		return this;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "ServiceDef [name=" + name + ", classes=" + classes + ", strategy=" + strategy + ", endState=" + endState
				+ ", rules=" + rules + "]";
	}

}
