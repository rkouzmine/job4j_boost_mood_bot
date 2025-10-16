package ru.job4j.bmb.repository;

import ru.job4j.bmb.model.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    List<User> findAll();

    User findByClientId(Long clientId);

}
