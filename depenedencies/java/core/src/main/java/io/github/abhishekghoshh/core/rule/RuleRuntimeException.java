package io.github.abhishekghoshh.core.rule;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RuleRuntimeException extends RuntimeException {
	private Exception exception;
	private String timestamp;
	private String stepKey;
	private String ruleClassName;
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	private static final long serialVersionUID = 1L;

	public RuleRuntimeException(Exception exception, String stepKey, String ruleClassName) {
		super(exception);
		this.exception = exception;
		this.timestamp = "" + System.currentTimeMillis();
		this.stepKey = stepKey;
		this.ruleClassName = ruleClassName;
	}

	public RuleRuntimeException(Exception exception, String stepKey, String ruleClassName, HttpStatus status) {
		super(exception);
		this.exception = exception;
		this.timestamp = "" + System.currentTimeMillis();
		this.stepKey = stepKey;
		this.ruleClassName = ruleClassName;
		this.status = status;
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

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
