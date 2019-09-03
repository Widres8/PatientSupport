package patientsupport.patientsupport.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.models.cases.Case;
import patientsupport.patientsupport.repository.CaseRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * CasesController
 */
@Controller
@RequestMapping("cases")
public class CasesController {

    private String pathView = "cases";
    private CaseRepository _repository;
    private UserService userService;


    /**
     * 
     * @param _repository
     * @param userService
     */
    @Autowired
    public CasesController(
        CaseRepository _repository, 
        UserService userService) {
        this._repository = _repository;
        this.userService = userService;
    }
    
    /**
     * Action list rows for table
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView view = new ModelAndView();
        view.addObject("cases", _repository.findAll());
        view.setViewName(pathView + "/index");

        return view;
    }

    @RequestMapping(value="/create", method = RequestMethod.GET)
    public ModelAndView create(Case itemToCreate){
        ModelAndView view = new ModelAndView();
        view.setViewName(pathView + "/create");
        return view;
    }



    
}