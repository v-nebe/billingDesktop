package com.shavneva.billingdesktop.repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface IServerApplicationCrud<T>{
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    List<T> getAll();
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    T getById(@PathParam("id") String id);

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    T create(T t);

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    T update(T t);

    @DELETE
    @Path("/delete/{id}")
    void delete(@PathParam("id") String id);
}
