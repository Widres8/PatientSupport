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
import patientsupport.patientsupport.models.parameters.Zone;
import patientsupport.patientsupport.repository.CountryRepository;
import patientsupport.patientsupport.repository.ZoneRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
@RequestMapping("zones")
public class ZonesController {

    private String pathView = "admin/zones";
    private final ZoneRepository _repository;
    private CountryRepository countryRepository;
    private UserService userService;

    @Autowired
    public ZonesController(
        ZoneRepository _repository, 
        CountryRepository countryRepository, 
        UserService userService) {
        this._repository = _repository;
        this.countryRepository = countryRepository;
        this.userService = userService;
    }

    // Index
    @RequestMapping(value = "/{countryId}",method = RequestMethod.GET)
    public ModelAndView list(@PathVariable("countryId") Integer countryId) {
        ModelAndView view = new ModelAndView();
        view.addObject("Country", countryRepository.findById(countryId).get());
        view.addObject("Zones", _repository.findByCountryId(countryId));
        view.setViewName(pathView + "/index");

        return view;
    }

    // Create
    @RequestMapping(value = "/create/{countryId}",method = RequestMethod.GET)
    public ModelAndView create(@PathVariable("countryId") Integer countryId, Zone itemToCreate, Model model) {
        ModelAndView view = new ModelAndView();
        itemToCreate.setCountryId(countryId);
        view.addObject("zone", itemToCreate);
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create/{countryId}",method = RequestMethod.POST)
    public ModelAndView store(@PathVariable("countryId") Integer countryId, @Valid Zone itemToCreate, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.addObject("zone", itemToCreate);
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
			itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setCreatedAt(new Date());
            itemToCreate.setActive(true);
            _repository.save(itemToCreate);
            view.setViewName("redirect:/zones/" + itemToCreate.getCountryId());
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
    @RequestMapping(value = "/edit/{countryId}/{id}",method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("countryId") Integer countryId, @PathVariable("id") Integer id, Model model) {
        ModelAndView view = new ModelAndView();

        Zone itemToEdit = findById(id);
        view.addObject("zone", itemToEdit);
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{countryId}/{id}",method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("countryId") Integer countryId, @PathVariable("id") Integer id, @Valid Zone itemToEdit, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        itemToEdit.setCountryId(countryId);
        if (result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }

        try {
			itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/zones/" + itemToEdit.getCountryId());
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
            Zone itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
        
    }

    @RequestMapping(value = "/active/{id}",method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String enable(@PathVariable("id") Integer id, Model model) {
        try {
            Zone itemToActive = findById(id);
            itemToActive.setActive(!itemToActive.getActive());
            itemToActive.setDeleteBy(userService.getAuthUser().getEmail());
            itemToActive.setDeleteAt(new Date());
            _repository.save(itemToActive);
            return "{\"Status\":\"200\",\"Message\":\"Registro actualizado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al actualizar el registro\"}";
        }
        
    }

    private Zone findById(int id) {
        return _repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
    }
}