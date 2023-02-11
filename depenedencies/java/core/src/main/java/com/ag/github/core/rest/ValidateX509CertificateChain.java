package com.ag.github.core.rest;

import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

@Service
public class ValidateX509CertificateChain {

	public boolean validate(X509Certificate[] x509Certificates, String authType) {
		return true;
	}

}
