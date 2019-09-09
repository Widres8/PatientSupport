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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.cases.Case;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.repository.CaseRepository;
import patientsupport.patientsupport.repository.CityRepository;
import patientsupport.patientsupport.repository.DepartmentRepository;
import patientsupport.patientsupport.repository.HealtOperatorAccountRepository;
import patientsupport.patientsupport.repository.InsuranceTypeRepository;
import patientsupport.patientsupport.repository.PatientRepository;
import patientsupport.patientsupport.repository.PhysicianRepository;
import patientsupport.patientsupport.repository.StageRepository;
import patientsupport.patientsupport.repository.StatusReasonRepository;
import patientsupport.patientsupport.repository.StatusRepository;
import patientsupport.patientsupport.repository.TherapyRepository;
import patientsupport.patientsupport.repository.UserRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * CasesController
 */
@Controller
@RequestMapping("cases")
public class CasesController {

    private String pathView = "cases";
    private CaseRepository _repository;
    private UserService userService;
    private UserRepository userRepository;
    private DepartmentRepository departmentRepository;
    private CityRepository cityRepository;
    private TherapyRepository therapyRepository;
    private StatusRepository statusRepository;
    private StageRepository stageRepository;
    private StatusReasonRepository statusReasonRepository;
    private PhysicianRepository physicianRepository;
    private PatientRepository patientRepository;
    private HealtOperatorAccountRepository healtOperatorAccountRepository;
    private InsuranceTypeRepository insuranceTypeRepository;


    /**
     * 
     * @param _repository
     * @param userService
     */
    @Autowired
    public CasesController(
        CaseRepository _repository,
        DepartmentRepository departmentRepository,
        CityRepository cityRepository,
        TherapyRepository therapyRepository,
        StatusRepository statusRepository,
        StageRepository stageRepository,
        StatusReasonRepository statusReasonRepository,
        PhysicianRepository physicianRepository,
        PatientRepository patientRepository,
        HealtOperatorAccountRepository healtOperatorAccountRepository,
        InsuranceTypeRepository insuranceTypeRepository,
        UserRepository userRepository,
        UserService userService) {
        this._repository = _repository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
        this.therapyRepository = therapyRepository;
        this.statusRepository = statusRepository;
        this.stageRepository = stageRepository;
        this.statusReasonRepository = statusReasonRepository;
        this.physicianRepository = physicianRepository;
        this.patientRepository = patientRepository;
        this.healtOperatorAccountRepository = healtOperatorAccountRepository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Action list rows for table
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView view = new ModelAndView();
        view.addObject("cases", _repository.findAll());
        view.setViewName(pathView + "/index");

        return view;
    }

    @RequestMapping(value="/create", method = RequestMethod.GET)
    public ModelAndView create(Case itemToCreate){
        ModelAndView view = new ModelAndView();
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(0));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("stages", stageRepository.findAll());
        view.addObject("statusReasons", statusReasonRepository.findAll());
        view.addObject("physicians", physicianRepository.findAll());
        view.addObject("patients", patientRepository.findAll());
        view.addObject("healtOperatorAccounts", healtOperatorAccountRepository.findAll());
        view.addObject("insuranceTypes", insuranceTypeRepository.findAll());
        view.addObject("users", userRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store(@Valid Case itemToCreate, BindingResult result, Model model, @RequestParam("treatmentDepartmentId") Integer treatmentDepartmentId) {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", getDepartments());
        view.addObject("cities", getCities(treatmentDepartmentId));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("stages", stageRepository.findAll());
        view.addObject("statusReasons", statusReasonRepository.findAll());
        view.addObject("physicians", physicianRepository.findAll());
        view.addObject("patients", patientRepository.findAll());
        view.addObject("healtOperatorAccounts", healtOperatorAccountRepository.findAll());
        view.addObject("insuranceTypes", insuranceTypeRepository.findAll());
        view.addObject("users", userRepository.findAll());
        
        if (result.hasErrors()) {
            view.setViewName(pathView + "/create");
            return view;
        }

        try {
           itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            itemToCreate.setEnrollmentDate(new Date());
            _repository.save(itemToCreate);
            view.setViewName("redirect:/cases");
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