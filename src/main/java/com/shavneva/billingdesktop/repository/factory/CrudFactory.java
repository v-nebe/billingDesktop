package com.shavneva.billingdesktop.repository.factory;

import com.shavneva.billingdesktop.entity.*;
import com.shavneva.billingdesktop.repository.*;
import com.shavneva.billingdesktop.repository.interfaces.*;
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

    public static CrudRepository<Tariff> createTariffRepository() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<Tariff> proxy = client.target(BASE_URL + "/tariffs" )
                .proxy(ITariffCrud.class);
        return new CrudRepository<>(client, proxy);
    }

    public static CrudRepository<Services> createServicesRepository() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<Services> proxy = client.target(BASE_URL + "/services" )
                .proxy(IServicesCrud.class);
        return new CrudRepository<>(client, proxy);
    }

    public static CrudRepository<Role> createRoleRepository() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<Role> proxy = client.target(BASE_URL + "/roles" )
                .proxy(IRoleCrud.class);
        return new CrudRepository<>(client, proxy);
    }

    public static CrudRepository<Account> createAccountRepository() {
        ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
        client.register(AddAuthHeadersRequestFilter.class);
        IServerApplicationCrud<Account> proxy = client.target(BASE_URL + "/accounts" )
                .proxy(IAccountCrud.class);
        return new CrudRepository<>(client, proxy);
    }
}
