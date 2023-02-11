package com.github.ag.core.model;

public class ExceptionBlock {
	private Exception exception;
	private String timestamp;
	private String stepKey;
	private String ruleClassName;

	public ExceptionBlock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExceptionBlock(Exception exception, String timestamp, String stepKey, String ruleClassName) {
		super();
		this.exception = exception;
		this.timestamp = timestamp;
		this.stepKey = stepKey;
		this.ruleClassName = ruleClassName;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getStepKey() {
		return stepKey;
	}

	public void setStepKey(String stepKey) {
		this.stepKey = stepKey;
	}

	public String getRuleClassName() {
		return ruleClassName;
	}

	public void setRuleClassName(String ruleClassName) {
		this.ruleClassName = ruleClassName;
	}

}
