package patientsupport.patientsupport.controllers;

import java.util.Date;

import javax.validation.Valid;


import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.accounts.Physician;
import patientsupport.patientsupport.repository.PhysicianRepository;
import patientsupport.patientsupport.repository.SpecialistTypeRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * PhysiciansController
 */
@Controller
@RequestMapping("physicians")
public class PhysiciansController {

    private String pathView = "accounts/physicians";
    private final PhysicianRepository _repository;
    private SpecialistTypeRepository specialistTypeRepository;
    private UserService userService;

    /**
     * Constructor ID
     * @param _repository
     * @param specialistTypeRepository
     * @param userService
     */
    public PhysiciansController(
        PhysicianRepository _repository, 
        SpecialistTypeRepository specialistTypeRepository,
        UserService userService) {
        this._repository = _repository;
        this.specialistTypeRepository = specialistTypeRepository;
        this.userService = userService;
    }

    /**
     * Method list data
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView();
        view.addObject("Physicians", _repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }
    

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(Physician itemTocreate) {

        ModelAndView view = new ModelAndView();
        view.addObject("specialistTypes", specialistTypeRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView store(@Valid Physician itemTocreate, 
                                BindingResult result, Model model ) {

        ModelAndView view = new ModelAndView();
        view.addObject("specialistTypes", specialistTypeRepository.findAll());
        if(result.hasErrors()){
            view.setViewName(pathView + "/create");
            return view;
        }
        try {
            String symbol = ", ";
            String fullName = itemTocreate.getFirstName() + symbol + itemTocreate.getLastName();
            itemTocreate.setCreatedAt(new Date());
            itemTocreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemTocreate.setAccountName(fullName);
            itemTocreate.setActive(true);
            _repository.save(itemTocreate);
            view.setViewName("redirect:/physicians");
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

    /**
     * Return the model for action Edit
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, Model model){

        ModelAndView view = new ModelAndView();
        Physician itemToEdit = findById(id);
        view.addObject("physician", itemToEdit);
        view.addObject("specialistTypes", specialistTypeRepository.findAll());
        view.setViewName(pathView + "/edit");
        return view;
    }

    /**
     * Update physician
     * @param id
     * @param itemToEdit
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id,
            @Valid Physician itemToEdit,
            BindingResult result, Model model){
        
        ModelAndView view = new ModelAndView();
        view.addObject("specialistTypes", specialistTypeRepository.findAll());
        if(result.hasErrors()) {

            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            String symbol = ", ";
            String fullName = itemToEdit.getFirstName() + symbol + itemToEdit.getLastName();
            itemToEdit.setAccountName(fullName);
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/physicians");
            return view;

        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.setViewName(pathView + "/edit");
            return view;
        }

    }

    /**
     * Enable or disable register
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/active/{id}",method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String enable(@PathVariable("id") Integer id, Model model) {
        try {
            Physician itemToActive = findById(id);
            itemToActive.setActive(!itemToActive.getActive());
            itemToActive.setDeleteBy(userService.getAuthUser().getEmail());
            itemToActive.setDeleteAt(new Date());
            _repository.save(itemToActive);
            return "{\"Status\":\"200\",\"Message\":\"Registro actualizado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al actualizar el registro\"}";
        }
        
    }

    /**
     * This Method allow find object by ID
     * @param id
     * @return
     */
    private Physician findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

}