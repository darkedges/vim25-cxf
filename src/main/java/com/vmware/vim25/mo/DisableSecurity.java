package com.vmware.vim25.mo;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

import com.vmware.vim25.VimPortType;

public class DisableSecurity {

	public static void trustEveryone(VimPortType port) {
		Client client = ClientProxy.getClient(port);
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		TLSClientParameters params = conduit.getTlsClientParameters();
		if (params == null) {
			params = new TLSClientParameters();
			conduit.setTlsClientParameters(params);
		}

		params.setTrustManagers(new TrustManager[] { new NoOpX509TrustManager() });
		params.setDisableCNCheck(true);
	}

	public static class NoOpX509TrustManager implements X509TrustManager {

		public NoOpX509TrustManager() {

		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}
}