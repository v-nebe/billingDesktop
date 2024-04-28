package com.shavneva.billingdesktop.repository;

import java.util.List;

public class CrudRepository<T> {

    private final IServerApplicationCrud<T> iServerApplicationCrud;
    private final Class<T> type;

    public CrudRepository(Class<T> type, IServerApplicationCrud<T> iServerApplicationCrud) {
        this.iServerApplicationCrud = iServerApplicationCrud;
        this.type = type;
    }

    public List<T> getAll() {
        return iServerApplicationCrud.getAll();
    }

    public T getOne(String id) {
        return iServerApplicationCrud.getById(id);
    }

    public void create(T t) {
        iServerApplicationCrud.create(t);
    }

    public void update(T t) {
        iServerApplicationCrud.update(t);
    }

    public void delete(String id) {
        iServerApplicationCrud.delete(id);
    }
}