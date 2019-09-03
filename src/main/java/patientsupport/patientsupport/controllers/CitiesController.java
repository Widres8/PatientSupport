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
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.repository.CityRepository;
import patientsupport.patientsupport.repository.DepartmentRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
@RequestMapping("cities")
public class CitiesController {

    private String pathView = "admin/cities";
    private DepartmentRepository _repository;
    private final CityRepository cityRepository;
    private UserService userService;

    @Autowired
    public CitiesController(
        DepartmentRepository _repository, 
        CityRepository cityRepository, 
        UserService userService) {
        this._repository = _repository;
        this.cityRepository = cityRepository;
        this.userService = userService;
    }

    // Index
    @RequestMapping(value = "/{departmentId}",method = RequestMethod.GET)
    public ModelAndView list(@PathVariable("departmentId") Integer departmentId) {
        ModelAndView view = new ModelAndView();
        view.addObject("Department", _repository.findById(departmentId).get());
        view.addObject("Cities", cityRepository.findByDepartmentId(departmentId));
        view.setViewName(pathView + "/index");

        return view;
    }

    // Create
    @RequestMapping(value = "/create/{departmentId}",method = RequestMethod.GET)
    public ModelAndView create(@PathVariable("departmentId") Integer departmentId, City itemToCreate, Model model) {
        ModelAndView view = new ModelAndView();
        itemToCreate.setDepartmentId(departmentId);
        view.addObject("city", itemToCreate);
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create/{departmentId}",method = RequestMethod.POST)
    public ModelAndView store(@PathVariable("departmentId") Integer departmentId, @Valid City itemToCreate, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.addObject("city", itemToCreate);
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
			itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setCreatedAt(new Date());
            cityRepository.save(itemToCreate);
            view.setViewName("redirect:/cities/" + itemToCreate.getDepartmentId());
            return view;
		} catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.addObject("zone", itemToCreate);
            view.setViewName(pathView + "/create");
            return view;
		}
    }

    // Edit
    @RequestMapping(value = "/edit/{departmentId}/{id}",method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("departmentId") Integer departmentId, @PathVariable("id") Integer id, Model model) {
        ModelAndView view = new ModelAndView();

        City itemToEdit = findById(id);
        view.addObject("city", itemToEdit);
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{departmentId}/{id}",method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("departmentId") Integer departmentId, @PathVariable("id") Integer id, @Valid City itemToEdit, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        itemToEdit.setDepartmentId(departmentId);
        if (result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }

        try {
			itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            cityRepository.save(itemToEdit);
            view.setViewName("redirect:/cities/" + itemToEdit.getDepartmentId());
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
    public @ResponseBody String delete(@PathVariable("id") Integer id, Model model) {
        try {
            City itemToDelete = findById(id);
            cityRepository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
        
    }
    
    private City findById(int id) {
        return cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
    }
}