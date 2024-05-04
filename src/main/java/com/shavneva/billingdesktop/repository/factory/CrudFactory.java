package com.shavneva.billingdesktop.repository.factory;

import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.AddAuthHeadersRequestFilter;
import com.shavneva.billingdesktop.repository.IServerApplicationCrud;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.IUserCrud;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class CrudFactory {
    private static final String BASE_URL = "http://localhost:8080/api";

    public static CrudRepository<User> createUserRepository() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<User> proxy = client.target(BASE_URL + "/user" )
                .proxy(IUserCrud.class);
        return new CrudRepository<>(client, proxy);
    }

}
