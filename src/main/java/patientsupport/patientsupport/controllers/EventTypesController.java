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
import patientsupport.patientsupport.models.events.EventType;
import patientsupport.patientsupport.repository.EventTypeRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * EventTypeController
 */
@Controller
@RequestMapping("eventtypes")
public class EventTypesController {

    private String pathView = "admin/eventtypes";
    private final EventTypeRepository _repository;
    private UserService userService;

    /**
     * Constructor dependencies inyection
     * @param _repository
     * @param userService
     */
    @Autowired
    public EventTypesController (
        EventTypeRepository _repository, 
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
        view.addObject("EventTypes", _repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }


    /**
     * Action Create
     * @param eventType
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(EventType itemToCreate) {

        ModelAndView view = new ModelAndView();
        view.setViewName(pathView + "/create");

        return view;
    }

    /**
     * Post Create Event Type
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView store(@Valid EventType itemToCreate, BindingResult result, Model model ) {

        ModelAndView view = new ModelAndView();
        if(result.hasErrors()){
            view.setViewName(pathView + "/create");
            return view;
        }
        try {
            itemToCreate.setCreatedAt(new Date());
            itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setActive(true);
            _repository.save(itemToCreate);
            view.setViewName("redirect:/eventtypes");
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
     * Request edit, get data by object filter by id.
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id,Model model ) {

        ModelAndView view = new ModelAndView();
        EventType itemToEdit = findById(id);
        view.addObject("eventType", itemToEdit);
        view.setViewName(pathView + "/edit");
        return view;

    }

    /**
     * Method put for update class in database
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid EventType itemToEdit,BindingResult result, Model model) {

        ModelAndView view = new ModelAndView();

        if(result.hasErrors()) {

            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/eventtypes");
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
            EventType itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
    }

    @RequestMapping(value = "/active/{id}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String active(@PathVariable("id") Integer id, Model model) {

        try {
            EventType itemToActive = findById(id);
            itemToActive.setActive(!itemToActive.getActive());
            itemToActive.setDeleteBy(userService.getAuthUser().getEmail());
            itemToActive.setDeleteAt(new Date());
            _repository.save(itemToActive);
            return "{\"Status\":\"200\",\"Message\":\"Registro actualizado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al actualziar el registro\"}";
        }
    }

    /**
     * Method find by id model
     */
    private EventType findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }
    
}