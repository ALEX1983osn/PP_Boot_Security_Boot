package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.validator.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleRepository roleRepository;
    private final UserServiceImpl userServiceImpl;
    private final UserValidator userValidator;

    public AdminController(RoleRepository roleRepository, UserServiceImpl userServiceImpl, UserValidator userValidator) {
        this.roleRepository = roleRepository;
        this.userServiceImpl = userServiceImpl;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String getUsers(Model model) {
        List<User> users = userServiceImpl.getAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/add")
    public String createUserForm(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "add";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add";
        }
        userServiceImpl.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/user-update")
    public String showFormForUpdate(@RequestParam("id") long id,
                                    Model model) {
        User user = userServiceImpl.findUser(id);
        model.addAttribute("user", user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "user-update";
    }

    @PostMapping("/user-update")
    public String updateUser(@RequestParam("id") long id,
                             @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "user-update";
        userServiceImpl.update(user, user.getRoles());
        return "redirect:/admin";
    }

    @DeleteMapping("/user-delete")
    public String delete(@RequestParam("id") int id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }
}