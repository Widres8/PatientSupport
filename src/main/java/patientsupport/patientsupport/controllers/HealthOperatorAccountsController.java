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
import patientsupport.patientsupport.models.accounts.HealthOperatorAccount;
import patientsupport.patientsupport.repository.HealtOperatorAccountRepository;
import patientsupport.patientsupport.repository.HealthOperatorTypeRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * HealthOperatorAccountsController
 */
@Controller
@RequestMapping("healthoperatoraccounts")
public class HealthOperatorAccountsController {

    private String pathView = "accounts/healthoperatoraccounts";
    private final HealtOperatorAccountRepository _repository;
    private HealthOperatorTypeRepository healthOperatorTypeRepository;
    private UserService userService;

    /**
     * Constructor DI
     * @param _repository
     * @param healthOperatorType
     * @param userService
     */
    public HealthOperatorAccountsController(
        HealtOperatorAccountRepository _repository,
        HealthOperatorTypeRepository healthOperatorTypeRepository, 
        UserService userService) {
        this._repository = _repository;
        this.healthOperatorTypeRepository = healthOperatorTypeRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView list(){

        ModelAndView view = new ModelAndView();
        view.addObject("HealthOperatorAccounts", _repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }

    /**
     * Action Create
     * @param itemTocreate
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(HealthOperatorAccount itemTocreate) {

        ModelAndView view = new ModelAndView();
        view.addObject("healthOperatorTypes", healthOperatorTypeRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView store(@Valid HealthOperatorAccount itemTocreate, BindingResult result, Model model ) {

        ModelAndView view = new ModelAndView();
        view.addObject("healthOperatorTypes", healthOperatorTypeRepository.findAll());
        if(result.hasErrors()){
            view.setViewName(pathView + "/create");
            return view;
        }
        try {
            itemTocreate.setCreatedAt(new Date());
            itemTocreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemTocreate.setActive(true);
            _repository.save(itemTocreate);
            view.setViewName("redirect:/healthoperatoraccounts");
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
     * Consult Data before to edit
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id,Model model ) {

        ModelAndView view = new ModelAndView();
        HealthOperatorAccount itemToEdit = findById(id);
        view.addObject("healthOperatorAccount", itemToEdit);
        view.addObject("healthOperatorTypes", healthOperatorTypeRepository.findAll());
        view.setViewName(pathView + "/edit");
        return view;

    }
    
    
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, 
                @Valid HealthOperatorAccount itemToEdit,
                BindingResult result, Model model) {

        ModelAndView view = new ModelAndView();
        view.addObject("healthOperatorTypes", healthOperatorTypeRepository.findAll());
        if(result.hasErrors()) {

            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/healthoperatoraccounts");
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

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String delete(@PathVariable("id") Integer id,Model model) {

        try {
            HealthOperatorAccount itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
    }

    @RequestMapping(value = "/active/{id}",method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String enable(@PathVariable("id") Integer id, Model model) {
        try {
            HealthOperatorAccount itemToActive = findById(id);
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
     * Method find by id model
     */
    private HealthOperatorAccount findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    
}