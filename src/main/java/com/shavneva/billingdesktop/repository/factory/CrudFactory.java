package com.shavneva.billingdesktop.repository.factory;

import com.shavneva.billingdesktop.repository.AddAuthHeadersRequestFilter;
import com.shavneva.billingdesktop.repository.IServerApplicationCrud;
import com.shavneva.billingdesktop.repository.CrudRepository;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class CrudFactory {
    private static final String BASE_URL = "http://localhost:8080/api";

    public static <T> CrudRepository<T> create(Class<T> type) {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<T> proxy = client.target(BASE_URL + "/" + type.getSimpleName().toLowerCase())
                .proxy(IServerApplicationCrud.class);
        return new CrudRepository<>(client, proxy);
    }
}
