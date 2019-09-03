package patientsupport.patientsupport.controllers;


import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.approvals.Approval;
import patientsupport.patientsupport.repository.ApprovalRepository;
import patientsupport.patientsupport.services.UserService;



/**
 * ApprovalsController
 */
@Controller
@RequestMapping("approvals")
public class ApprovalsController {

    private String pathView = "approvals";
    private ApprovalRepository _repository;
    private UserService userService;

    /**
     * Constructor ID
     * @param _repository
     * @param userService
     */
    @Autowired
    public ApprovalsController(ApprovalRepository _repository, UserService userService) {
        this._repository = _repository;
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView list(){

        ModelAndView view = new ModelAndView();
        view.addObject("approvals", _repository.findAll());
        view.setViewName(pathView + "/index");
        
        return view;

    }
      
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create (Approval itemToCreate){
        ModelAndView view = new ModelAndView();
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store (@Valid Approval itemToCreate, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();

        if (result.hasErrors()) {
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
            itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setCreatedAt(new Date());
            _repository.save(itemToCreate);
            view.setViewName("redirect:/approvals");
            return view;
        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.setViewName(pathView + "/create");
            return view;
        }
    }

    
}