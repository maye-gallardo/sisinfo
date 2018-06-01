package com.ucbcba.proyecto.controllers;

import com.ucbcba.proyecto.entities.*;
import com.ucbcba.proyecto.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        return "principal";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationInit(Model model) {
        java.util.Date date = new java.util.Date();
        java.sql.Date today = new java.sql.Date(date.getTime());
        Users user = new Users();
        user.setDate(today);
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/save/edit",method = RequestMethod.POST)
    public String saveUserEdit(@Valid Users user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "Profile/profileForm";
        }
        userService.save(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@Valid Users user, BindingResult bindingResult, Model model) {
        Iterable<Users> users = userService.listAll();
        for (Users u: users) {
            if (Objects.equals(user.getEmail(), u.getEmail())){
                ObjectError mail = new ObjectError("mail","La cuenta ya se encuentra registrada.");
                bindingResult.addError(mail);
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(user);
            /*return "reg";*/
            return "registration";
        }
        user.setPasswordConfirm(user.getPassword());
        userService.save(user);
        securityService.autologin(user.getEmail(), user.getPasswordConfirm());
        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (roles.contains("CLIENTE")) {
            return "redirect:/user/home";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (roles.contains("CLIENTE")) {
            return "redirect:/user/home";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/user/home", method = RequestMethod.GET)
    public String homeU(Model model){
        return "redirect:/histories";
    }


    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
    public String list(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "Profile/profile";
    }

    @RequestMapping(value = "/edit/user/profile", method = RequestMethod.GET)
    public String editProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "Profile/profileForm";
    }

    @RequestMapping(value = "/user/save/edit", method = RequestMethod.POST)
    public String saveEditUser(@Valid Users user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(user);
            return "Profile/profileForm";
        }
        userService.saveUserEdited(user);
        return "redirect:/user/profile";
    }

}
