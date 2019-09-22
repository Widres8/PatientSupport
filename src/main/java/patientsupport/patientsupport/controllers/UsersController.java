package patientsupport.patientsupport.controllers;

import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.services.UserService;

/**
 * EventTypeController
 */
@Controller
@RequestMapping("users")
public class UsersController {

    private String pathView = "admin/users";
    private UserService userService;

    /**
     * Constructor dependencies inyection
     * @param userService
     */
    @Autowired
    public UsersController (UserService userService){
        this.userService = userService;
    }

    /**
     * List view. Index
     * @return all items in model
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView list(){

        ModelAndView view = new ModelAndView();
        view.addObject("Users", userService.All());
        view.setViewName(pathView + "/index");
        return view;
    }


    /**
     * Action Create
     * @param itemTocreate
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(User itemTocreate) {
        ModelAndView view = new ModelAndView();
        view.addObject("roles", userService.getRoles());
        int year = Calendar.getInstance().get(Calendar.YEAR);
        itemTocreate.setPassword("SoportePacientes" + year);
        view.setViewName(pathView + "/create");
        return view;
    }

    /**
     * Post Create Event Type
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView store(@Valid User itemTocreate, BindingResult result, Model model, @RequestParam("role") String role ) {

        ModelAndView view = new ModelAndView();
        view.addObject("roles", userService.getRoles());
        if(result.hasErrors()){
            view.setViewName(pathView + "/create");
            return view;
        }
        try {
            userService.saveUser(itemTocreate, role);
            view.setViewName("redirect:/users");
            return view;

        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.setViewName(pathView + "/create");
            return view;
        } catch (Exception ex) {
            model.addAttribute("error",ex.getMessage());
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
        User itemToEdit = userService.findById(id);
        view.addObject("user", itemToEdit);
        view.addObject("roles", userService.getRoles());
        view.setViewName(pathView + "/edit");
        return view;
    }

    /**
     * Method put for update class in database
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid User itemToEdit,BindingResult result, Model model, @RequestParam("role") String role) {

        ModelAndView view = new ModelAndView();
        view.addObject("roles", userService.getRoles());
        if(result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }

        try {    
            userService.updatUser(itemToEdit, role);
            view.setViewName("redirect:/users");
            return view;

        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.setViewName(pathView + "/edit");
            return view;
        } catch (Exception ex) {
            model.addAttribute("error",ex.getMessage());
            view.setViewName(pathView + "/create");
            return view;
        }
    }

    /**
     * Method delete, validate id in class
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String delete(@PathVariable("id") Integer id,Model model) {

        boolean result = userService.deleteUser(id);
        if (result) {
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } else {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
    }

    @RequestMapping(value = "/active/{id}",method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String enable(@PathVariable("id") Integer id, Model model) {
        boolean result = userService.activeUser(id);
        if (result) {
            return "{\"Status\":\"200\",\"Message\":\"Registro actualizado correctamente\"}";
        } else {
            return "{\"Status\":\"400\",\"Error\":\"Error al actualizar el registro\"}";
        }        
    }
}