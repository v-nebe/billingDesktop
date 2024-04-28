package com.shavneva.billingdesktop.DAO;

import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;

import java.util.List;

public class UserDao {

    private final CrudRepository<User> userRepository;

    public UserDao() {
        userRepository = CrudFactory.create(User.class);
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public User getUserById(String id) {
        return userRepository.getOne(id);
    }

    public void createUser(User user) {
        userRepository.create(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(id);
    }
}
