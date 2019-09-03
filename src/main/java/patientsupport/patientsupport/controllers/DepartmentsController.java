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
import patientsupport.patientsupport.models.parameters.Country;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.models.parameters.Zone;
import patientsupport.patientsupport.repository.CountryRepository;
import patientsupport.patientsupport.repository.DepartmentRepository;
import patientsupport.patientsupport.repository.ZoneRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
@RequestMapping("departments")
public class DepartmentsController {

    private String pathView = "admin/departments";
    private CountryRepository countryRepository;
    private ZoneRepository zoneRepository;
    private DepartmentRepository _repository;
    private UserService userService;

    @Autowired
    public DepartmentsController(
        CountryRepository countryRepository, 
        ZoneRepository zoneRepository, 
        DepartmentRepository _repository, 
        UserService userService){
        this.countryRepository = countryRepository;
        this.zoneRepository = zoneRepository;
        this._repository = _repository;
        this.userService = userService;
    }

    // Index
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView();
        view.addObject("Departments",_repository.findAll());
        view.setViewName(pathView + "/index");
        return view;
    }

    // Create
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public ModelAndView create(Department itemToCreate) {
        ModelAndView view = new ModelAndView();
        
        view.addObject("countries", getCountries());
        view.addObject("zones", getZones(0));
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store(@Valid Department itemToCreate, BindingResult result, Model model, @RequestParam("countryId") Integer countryId) {
        ModelAndView view = new ModelAndView();
        view.addObject("countries", getCountries());
        view.addObject("zones", getZones(countryId));
        if (result.hasErrors()) {
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
            itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setCreatedAt(new Date());
            _repository.save(itemToCreate);
            view.setViewName("redirect:/departments");
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

    // Edit
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, Model model) {
        ModelAndView view = new ModelAndView();
        Department itemToEdit = findById(id);
        model.addAttribute("department", itemToEdit);
        view.addObject("countries", getCountries());
        view.addObject("zones", getZones(itemToEdit.getZone().getCountryId()));
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid Department itemToEdit, BindingResult result, Model model) {
        ModelAndView view = new ModelAndView();
        view.addObject("countries", getCountries());
        view.addObject("zones", getZones(findById(id).getZone().getCountryId()));
        if (result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }

        try {
			itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/departments");
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
            Department itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }
    }

    @RequestMapping(value = "/getZonesByCountryId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<Zone> getZonesByCountryId(@RequestParam Integer countryId) {
		try {
			return getZones(countryId);

		} catch (Exception ex) {
			return null;
		}
	}

    private Department findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    private List<Country> getCountries() {
        Iterable<Country> list = () -> StreamSupport.stream(countryRepository.findAll().spliterator(), false)
        .filter(c -> c.getZones().size() > 0)
        .iterator();

        List<Country> result = StreamSupport.stream(list.spliterator(), false)
        .collect(Collectors.toList());
        return result;
    }

    private List<Zone> getZones(Integer countryId) {
        List<Zone> result = zoneRepository
        .findByCountryId(countryId)
        .stream()
        .filter( x -> x.getActive())
        .collect(Collectors.toList());
        return result;
    }
}