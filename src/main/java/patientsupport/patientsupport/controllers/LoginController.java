package patientsupport.patientsupport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.services.UserService;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        User email = userService.getAuthUser();
        if (email == null) {
            modelAndView.setViewName("security/login");
        } else {
            modelAndView.setViewName("admin/home");
        }
        return modelAndView;
    }
}
