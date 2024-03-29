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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Selects;
import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.accounts.Patient;
import patientsupport.patientsupport.repository.DocumentTypeRepository;
import patientsupport.patientsupport.repository.PatientRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
@RequestMapping("patients")
public class PatientsController {

    private String pathView = "accounts/patients";
    private DocumentTypeRepository documentTypeRepository;
    private PatientRepository _repository;
    private UserService userService;
    private Selects selects;

    @Autowired
    public PatientsController(
        Selects selects,
        DocumentTypeRepository documentTypeRepository, 
        PatientRepository _repository,
        UserService userService){
        this._repository = _repository;
        this.documentTypeRepository = documentTypeRepository;
        this.userService = userService;
        this.selects = selects;
    }

    // Index
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView();
        view.addObject("Patients",_repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }

    // Create
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public ModelAndView create(Patient itemToCreate) {
        ModelAndView view = new ModelAndView();
        
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities", selects.getCities(0));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store(@Valid Patient itemToCreate, BindingResult result, Model model, @RequestParam("departmentId") Integer departmentId) {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities", selects.getCities(departmentId));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        if (result.hasErrors()) {
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
            String middle = itemToCreate.getMiddleName() != "" ? itemToCreate.getMiddleName().substring(0, 1) : "";
            String initials = middle != "" 
            ? itemToCreate.getFirstName().substring(0, 1) + middle + itemToCreate.getLastName().substring(0,1)
            : itemToCreate.getFirstName().substring(0, 1) + itemToCreate.getLastName().substring(0, 1);
            itemToCreate.setInitials(initials.toUpperCase());
            String symbol = ", ";
            String fullName = itemToCreate.getFirstName() + symbol + itemToCreate.getLastName();
            itemToCreate.setAccountName(fullName);
            itemToCreate.setActive(true);
            itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setCreatedAt(new Date());
            _repository.save(itemToCreate);
            view.setViewName("redirect:/patients");
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

    // Show
    @RequestMapping(value = "/details/{id}",method = RequestMethod.GET)
    public ModelAndView details(@PathVariable("id") Integer id, Model model) {
        ModelAndView view = new ModelAndView();
        Patient itemToEdit = findById(id);
        model.addAttribute("patient", itemToEdit);
        view.setViewName(pathView + "/details");
        return view;
    }

    // Edit
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, Model model) {
        ModelAndView view = new ModelAndView();
        Patient itemToEdit = findById(id);
        model.addAttribute("patient", itemToEdit);
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities", selects.getCities(itemToEdit.getDepartmentId()));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid Patient itemToEdit, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities", selects.getCities(itemToEdit.getDepartmentId()));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        if (result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }

        try {
            String middle = itemToEdit.getMiddleName() != "" ? itemToEdit.getMiddleName().substring(0, 1) : "";
            String initials = middle != "" 
            ? itemToEdit.getFirstName().substring(0, 1) + middle + itemToEdit.getLastName().substring(0,1)
            : itemToEdit.getFirstName().substring(0, 1) + itemToEdit.getLastName().substring(0, 1);
            itemToEdit.setInitials(initials.toUpperCase());
            String symbol = ", ";
            String fullName = itemToEdit.getFirstName() + symbol + itemToEdit.getLastName();
            itemToEdit.setAccountName(fullName);
			itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/patients");
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
            Patient itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
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
            Patient itemToActive = findById(id);
            itemToActive.setActive(!itemToActive.getActive());
            itemToActive.setDeleteBy(userService.getAuthUser().getEmail());
            itemToActive.setDeleteAt(new Date());
            _repository.save(itemToActive);
            return "{\"Status\":\"200\",\"Message\":\"Registro actualizado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al actualizar el registro\"}";
        }
        
    }

    private Patient findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }
}