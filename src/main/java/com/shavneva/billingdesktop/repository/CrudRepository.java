package com.shavneva.billingdesktop.repository;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import java.util.List;

public class CrudRepository<T> {

    private final IServerApplicationCrud<T> iServerApplicationCrud;
    private final  ResteasyClient client;
    public CrudRepository(ResteasyClient client, IServerApplicationCrud<T> iServerApplicationCrud) {

        this.iServerApplicationCrud = iServerApplicationCrud;
        this.client = client;
    }

    public List<T> getAll() {
        List<T> all = iServerApplicationCrud.getAll();
        client.close();
        return all;
    }

    public T getOne(String id) {
        return iServerApplicationCrud.getById(id);
    }

    public void create(T t) {
        iServerApplicationCrud.create(t);
        client.close();
    }

    public void update(T t) {
        iServerApplicationCrud.update(t);
        client.close();
    }

    public void delete(String id) {
        iServerApplicationCrud.delete(id);
        client.close();
    }
}