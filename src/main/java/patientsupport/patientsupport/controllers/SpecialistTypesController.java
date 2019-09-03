package patientsupport.patientsupport.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import patientsupport.patientsupport.models.parameters.SpecialistType;
import patientsupport.patientsupport.repository.SpecialistTypeRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * EventTypeController
 */
@Controller
@RequestMapping("specialisttypes")
public class SpecialistTypesController {

    private String pathView = "admin/specialisttypes";
    private final SpecialistTypeRepository _repository;
    private UserService userService;

    /**
     * Constructor dependencies inyection
     * @param _repository
     * @param userService
     */
    @Autowired
    public SpecialistTypesController (
        SpecialistTypeRepository _repository, 
        UserService userService){
        this._repository = _repository;
        this.userService = userService;
    }

    /**
     * List view. Index
     * @return all items in model
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView list(){

        ModelAndView view = new ModelAndView();
        view.addObject("SpecialistTypes", _repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }


    /**
     * Action Create
     * @param itemTocreate
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(SpecialistType itemTocreate) {

        ModelAndView view = new ModelAndView();
        view.setViewName(pathView + "/create");

        return view;
    }

    /**
     * Post Create Event Type
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView store(@Valid SpecialistType itemTocreate, BindingResult result, Model model ) {

        ModelAndView view = new ModelAndView();
        if(result.hasErrors()){
            view.setViewName(pathView + "/create");
            return view;
        }
        try {
            itemTocreate.setCreatedAt(new Date());
            itemTocreate.setCreatedBy(userService.getAuthUser().getEmail());
            _repository.save(itemTocreate);
            view.setViewName("redirect:/specialisttypes");
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
     * Request edit Event Type, get data by object filter by id.
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id,Model model ) {

        ModelAndView view = new ModelAndView();
        SpecialistType itemToEdit = findById(id);
        view.addObject("specialistType", itemToEdit);
        view.setViewName(pathView + "/edit");
        return view;

    }

    /**
     * Method put for update class in database
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid SpecialistType itemToEdit,BindingResult result, Model model) {

        ModelAndView view = new ModelAndView();

        if(result.hasErrors()) {

            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/specialisttypes");
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
     * Method delete, validate id in class
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String delete(@PathVariable("id") Integer id,Model model) {

        try {
            SpecialistType itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
    }

    /**
     * Method find by id model
     */
    private SpecialistType findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }
    
}