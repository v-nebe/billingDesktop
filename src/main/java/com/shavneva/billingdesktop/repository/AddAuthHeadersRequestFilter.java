package com.shavneva.billingdesktop.repository;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.util.Base64;

public class AddAuthHeadersRequestFilter implements ClientRequestFilter {
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String credentials = SecurityContext.getUsername() + ":" + SecurityContext.getPassword();
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        requestContext.getHeaders().add("Authorization", "Basic " + base64Credentials);
    }
}
