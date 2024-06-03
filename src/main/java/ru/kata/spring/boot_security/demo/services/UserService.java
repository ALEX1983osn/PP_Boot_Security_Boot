package ru.kata.spring.boot_security.demo.services;



import ru.kata.spring.boot_security.demo.entities.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    List<User> getAll();

    boolean add(@Valid User user);

    void update(User updatedUser);

    User readUser(long id);

    void delete(long id);

    User findByEmail(String email);

    void createRolesIfNotExist();
}