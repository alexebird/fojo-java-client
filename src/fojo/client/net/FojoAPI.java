package fojo.client.net;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.DeadObjectException;

public class FojoAPI {
	public static FojoAPI singletonInstance;
	
	private static final String API_BASE_URL = "http://10.0.2.2:3000/api";
	private static final String API_FORMAT_EXTENSION = "json";
	private static final String USERNAME = "fojo_user";
	private static final String PASSWORD = "blender";
	
	private DefaultHttpClient httpClient;
	private BasicHttpContext localContext;
	
	private FojoAPI() {
		this.httpClient = new DefaultHttpClient();
		this.httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));
		this.localContext = new BasicHttpContext();
		
        // Generate BASIC scheme object and stick it to the local 
        // execution context
        BasicScheme basicAuth = new BasicScheme();
        this.localContext.setAttribute("preemptive-auth", basicAuth);
        
        // Add as the first request interceptor
        this.httpClient.addRequestInterceptor(new PreemptiveAuth(), 0);
	}
	
	public FojoAPI getInstance() {
		if (FojoAPI.singletonInstance ==null) {
			FojoAPI.singletonInstance = new FojoAPI();
		}
		
		return FojoAPI.singletonInstance;
	}
	
	public HttpResponse sendPostRequest(FojoRESTEntity fojoObject) throws Exception {
		HttpPost httppost = new HttpPost(String.format("%s/%s.%s", API_BASE_URL, fojoObject.getRESTName(), API_FORMAT_EXTENSION));
		StringEntity se = new StringEntity(fojoObject.toJSON(), HTTP.UTF_8);
		httppost.setHeader("Content-Type","application/json;charset=UTF-8");
		httppost.setEntity(se);
		
		HttpResponse response = this.httpClient.execute(httppost, this.localContext);
		return response;
	}
	
	static class PreemptiveAuth implements HttpRequestInterceptor {

        public void process(
                final HttpRequest request, 
                final HttpContext context) throws HttpException, IOException {
            
            AuthState authState = (AuthState) context.getAttribute(
                    ClientContext.TARGET_AUTH_STATE);
            
            // If no auth scheme avaialble yet, try to initialize it preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute(
                        "preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                        ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(
                        ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(
                            new AuthScope(
                                    targetHost.getHostName(), 
                                    targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }
            
        }
        
    }
}
