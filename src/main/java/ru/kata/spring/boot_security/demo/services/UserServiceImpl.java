package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean add(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());

        if (userFromDB != null) {
            return false;
        }
        createRolesIfNotExist();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void createRolesIfNotExist() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(1L, "ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(2L, "ROLE_ADMIN"));
        }
    }

    @Override
    public void update(User updatedUser) {
        User user = readUser(updatedUser.getId());
        String newPassword = updatedUser.getPassword();

        if (!newPassword.equals(user.getPassword()) && !newPassword.isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(newPassword));
        } else {
            updatedUser.setPassword(user.getPassword());
        }

        userRepository.save(updatedUser);
    }

    @Override
    public User readUser(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " not exist"));
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Such user not exists"));
        userRepository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}