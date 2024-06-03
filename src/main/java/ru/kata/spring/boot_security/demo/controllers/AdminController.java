package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Principal principal, Model model) {
        model.addAttribute("users", userService.getAll());

        addAttributesToMainPage(model, principal);

        return "/admin/user-list";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("newUser") @Valid User newUser,
                             BindingResult bindingResult, Model model, Principal principal) {
        User userByEmail = userService.findByEmail(newUser.getEmail());
        if (userByEmail != null) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            addAttributesToMainPage(model, principal);
            return "/admin/user-list";
        }

        this.userService.add(newUser);
        return "redirect:/admin/users/";
    }

    @GetMapping("/user-update")
    public String showFormForUpdate(@RequestParam("id") long id,
                                    Model model) {
        User user = userService.readUser(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("listRoles", roleService.findAll());
            return "/admin/edit-user";
        } else {
            return "redirect:/admin/users";
        }
    }


    @PatchMapping("/users/edit")
    public String editUser(@ModelAttribute("updatingUser") @Valid User updatingUser,
                           BindingResult bindingResult, Model model, Principal principal) {
        User userByEmail = userService.findByEmail(updatingUser.getEmail());
        if (userByEmail != null && (userByEmail.getId() != (updatingUser.getId()))) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            addAttributesToMainPage(model, principal);
            model.addAttribute("editUserError", true);
            return "/admin/user-list";
        }

        userService.update(updatingUser);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/delete")
    public String deleteUser(@ModelAttribute("deletingUser") User user) {
        Long id = user.getId();
        if (userService.readUser(id) != null) {
            userService.delete(id);
        }
        return "redirect:/admin/users";
    }

    private void addAttributesToMainPage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            user = new User();
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();

        model.addAttribute("authUser", user);
        model.addAttribute("userRoles", roles);

        if (!model.containsAttribute("updatingUser")) {
            model.addAttribute("updatingUser", new User());
        }

        if (!model.containsAttribute("newUser")) {
            model.addAttribute("newUser", new User());
        }

        if (!model.containsAttribute("deletingUser")) {
            model.addAttribute("deletingUser", new User());
        }

        model.addAttribute("listRoles", roleService.findAll());
        model.addAttribute("users", userService.getAll());
    }
}