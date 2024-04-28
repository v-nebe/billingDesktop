module com.shavneva.billingdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires java.ws.rs;
    requires resteasy.client.api;

    opens com.shavneva.billingdesktop to javafx.fxml;
    exports com.shavneva.billingdesktop;
    exports com.shavneva.billingdesktop.entity;
    exports com.shavneva.billingdesktop.service;
    exports com.shavneva.billingdesktop.repository;
    exports com.shavneva.billingdesktop.DAO;
}