package patientsupport.patientsupport.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import patientsupport.patientsupport.models.accounts.Patient;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.repository.CityRepository;
import patientsupport.patientsupport.repository.DepartmentRepository;
import patientsupport.patientsupport.repository.DocumentTypeRepository;
import patientsupport.patientsupport.repository.PatientRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
@RequestMapping("patients")
public class PatientsController {

    private String pathView = "accounts/patients";
    private DepartmentRepository departmentRepository;
    private CityRepository cityRepository;
    private DocumentTypeRepository documentTypeRepository;
    private PatientRepository _repository;
    private UserService userService;

    @Autowired
    public PatientsController(
        DepartmentRepository departmentRepository, 
        CityRepository cityRepository,
        DocumentTypeRepository documentTypeRepository, 
        PatientRepository _repository,
        UserService userService){
        this._repository = _repository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.userService = userService;
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
        
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(0));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store(@Valid Patient itemToCreate, BindingResult result, Model model, @RequestParam("departmentId") Integer departmentId) {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(departmentId));
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
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(itemToEdit.getDepartmentId()));
        view.addObject("documenttypes", documentTypeRepository.findAll());
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid Patient itemToEdit, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(itemToEdit.getDepartmentId()));
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

    @RequestMapping(value = "/getCitiesByDepartmentId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<City> getCitiesByDepartmentId(@RequestParam Integer departmentId) {
		try {
			return getCities(departmentId);

		} catch (Exception ex) {
			return null;
		}
	}

    private Patient findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    private List<Department> getDepartments() {
        Iterable<Department> list = () -> StreamSupport.stream(departmentRepository.findAll().spliterator(), false)
        .filter(c -> c.getCities().size() > 0)
        .iterator();

        List<Department> result = StreamSupport.stream(list.spliterator(), false)
        .collect(Collectors.toList());
        return result;
    }

    private List<City> getCities(Integer departmentId) {
        return cityRepository.findByDepartmentId(departmentId);
    }
}