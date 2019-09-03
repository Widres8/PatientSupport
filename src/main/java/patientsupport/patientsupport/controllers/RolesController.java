package patientsupport.patientsupport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.repository.RoleRepository;

@Controller
public class RolesController {

    @Autowired
    RoleRepository roleRepository;

    private String pathView = "admin/roles";

    //Index
    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    public ModelAndView list(){

        ModelAndView view = new ModelAndView();
        view.addObject("Roles",roleRepository.findAll());
        view.setViewName(pathView + "/index");

        return view;
        
    }

    
}