package com.ag.github.core.rest;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

@Configuration
public class SSLContextProvider {

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	Logger log = LoggerFactory.getLogger(SSLContextProvider.class);

	@Bean
	public SSLContext sslContext(@Autowired Environment environment,
			@Autowired ValidateX509CertificateChain validateX509CertificateChain) throws IOException,
			CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		String hasHttpsEnabled = environment.getProperty("rest-template.https.enabled", FALSE);
		log.debug("hasHttpsEnabled flag value {}", hasHttpsEnabled);
		String validateCertificateChain = environment.getProperty("rest-template.validate.certificate-chain", FALSE);
		log.info("validateCertificateChain flag value {}", validateCertificateChain);
		TrustStrategy trustStrategy = (x509Certificates, authType) -> {
			if (TRUE.equalsIgnoreCase(validateCertificateChain))
				return validateX509CertificateChain.validate(x509Certificates, authType);
			return true;
		};
		if (TRUE.equalsIgnoreCase(hasHttpsEnabled)) {
			String location = environment.getProperty("server.ssl.key-store");
			String pass = environment.getProperty("server.ssl.key-store-password");
			log.info("location of the certificate {}", location);
			if (null == location || location.isBlank() || null == pass || pass.isBlank()) {
				throw new RuntimeException("keystore/password should not be empty");
			}
			return SSLContextBuilder.create()
					.loadTrustMaterial(ResourceUtils.getFile(location), pass.toCharArray(), trustStrategy).build();
		} else {
			return SSLContextBuilder.create().loadTrustMaterial(trustStrategy).build();
		}
	}
}
