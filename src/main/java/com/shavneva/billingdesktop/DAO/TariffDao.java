package com.shavneva.billingdesktop.DAO;

import com.shavneva.billingdesktop.entity.Tariff;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;

import java.util.List;

public class TariffDao {
    private final CrudRepository<Tariff> tariffRepository;

    public TariffDao() {
        tariffRepository = CrudFactory.create(Tariff.class);
    }

    public List<Tariff> getAllTariffs() {
        return tariffRepository.getAll();
    }

    public Tariff getTariffById(String id) {
        return tariffRepository.getOne(id);
    }

    public void createTariff(Tariff tariff) {
        tariffRepository.create(tariff);
    }

    public void updateTariff(Tariff tariff) {
        tariffRepository.update(tariff);
    }

    public void deleteTariff(String id) {
        tariffRepository.delete(id);
    }
}
