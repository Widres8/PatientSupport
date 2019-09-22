package patientsupport.patientsupport.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Selects;
import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.helpers.mail.EmailServiceImpl;
import patientsupport.patientsupport.helpers.mail.Mail;
import patientsupport.patientsupport.models.User;
import patientsupport.patientsupport.models.accounts.Patient;
import patientsupport.patientsupport.models.cases.Case;
import patientsupport.patientsupport.repository.CaseRepository;
import patientsupport.patientsupport.repository.HealtOperatorAccountRepository;
import patientsupport.patientsupport.repository.InsuranceTypeRepository;
import patientsupport.patientsupport.repository.PatientRepository;
import patientsupport.patientsupport.repository.PhysicianRepository;
import patientsupport.patientsupport.repository.StageRepository;
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
    private TherapyRepository therapyRepository;
    private StatusRepository statusRepository;
    private StageRepository stageRepository;
    private PhysicianRepository physicianRepository;
    private PatientRepository patientRepository;
    private HealtOperatorAccountRepository healtOperatorAccountRepository;
    private InsuranceTypeRepository insuranceTypeRepository;
    private Selects selects;
    private EmailServiceImpl emailService;

    /**
     * 
     * @param _repository
     * @param userService
     */
    @Autowired
    public CasesController(
        Selects selects,
        EmailServiceImpl emailService,
        CaseRepository _repository,
        TherapyRepository therapyRepository,
        StatusRepository statusRepository,
        StageRepository stageRepository,
        PhysicianRepository physicianRepository,
        PatientRepository patientRepository,
        HealtOperatorAccountRepository healtOperatorAccountRepository,
        InsuranceTypeRepository insuranceTypeRepository,
        UserRepository userRepository,
        UserService userService) {
        this._repository = _repository;
        this.userService = userService;
        this.therapyRepository = therapyRepository;
        this.statusRepository = statusRepository;
        this.stageRepository = stageRepository;
        this.physicianRepository = physicianRepository;
        this.patientRepository = patientRepository;
        this.healtOperatorAccountRepository = healtOperatorAccountRepository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.userRepository = userRepository;
        this.selects = selects;
        this.emailService = emailService;
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
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities",selects.getCities(0));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("statusReasons", selects.getStatusReasons(0));
        view.addObject("stages", stageRepository.findAll());
        view.addObject("physicians", physicianRepository.findAll());
        view.addObject("patients", patientRepository.findAll());
        view.addObject("healtOperatorAccounts", healtOperatorAccountRepository.findAll());
        view.addObject("insuranceTypes", insuranceTypeRepository.findAll());
        view.addObject("users", userRepository.findAll());
        view.setViewName(pathView + "/create");
        return view;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView store(@Valid Case itemToCreate, BindingResult result, Model model, @RequestParam("treatmentDepartmentId") Integer treatmentDepartmentId) throws Exception {
        ModelAndView view = new ModelAndView();
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities",selects.getCities(treatmentDepartmentId));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("statusReasons", selects.getStatusReasons(itemToCreate.getStatusReasonId()));
        view.addObject("stages", stageRepository.findAll());
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
            Case itemSaved = _repository.save(itemToCreate);

            sendEmail(itemSaved);
            
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id,Model model) {
        ModelAndView view = new ModelAndView();
        Case itemToEdit = findById(id);
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities",selects.getCities(itemToEdit.getTreatmentDepartmentId()));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("statusReasons", selects.getStatusReasons(itemToEdit.getStatusId()));
        view.addObject("stages", stageRepository.findAll());
        view.addObject("physicians", physicianRepository.findAll());
        view.addObject("patients", patientRepository.findAll());
        view.addObject("healtOperatorAccounts", healtOperatorAccountRepository.findAll());
        view.addObject("insuranceTypes", insuranceTypeRepository.findAll());
        view.addObject("users", userRepository.findAll());
        view.addObject("case", itemToEdit);
        view.setViewName(pathView + "/edit");
        return view;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id, @Valid Case itemToEdit,BindingResult result, Model model) throws Exception {

        ModelAndView view = new ModelAndView();
        view.addObject("departments", selects.getDepartments());
        view.addObject("cities",selects.getCities(itemToEdit.getTreatmentDepartmentId()));
        view.addObject("therapies", therapyRepository.findAll());
        view.addObject("status", statusRepository.findAll());
        view.addObject("statusReasons", selects.getStatusReasons(itemToEdit.getStatusId()));
        view.addObject("stages", stageRepository.findAll());
        view.addObject("physicians", physicianRepository.findAll());
        view.addObject("patients", patientRepository.findAll());
        view.addObject("healtOperatorAccounts", healtOperatorAccountRepository.findAll());
        view.addObject("insuranceTypes", insuranceTypeRepository.findAll());
        view.addObject("users", userRepository.findAll());
        if(result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            itemToEdit.setLastModifiedAt(new Date());
            Case itemSaved = _repository.save(itemToEdit);

            sendEmail(itemSaved);
            view.setViewName("redirect:/cases");
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

    private Case findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    private void sendEmail(Case item) throws Exception {
        Mail mail = new Mail();
        User user = userRepository.findById(item.getFieldNurse()).get();
        Patient patient = patientRepository.findById(item.getPatientId()).get();

        mail.setTo(user.getEmail());
        mail.setFrom("no-reply@patientsupport.com");
        mail.setSubject("Creación Caso");

        Map<String, Object> modelEmail = new HashMap<String, Object>();
        modelEmail.put("name", user.getFirstName());
        modelEmail.put("caseid", item.getCode());
        modelEmail.put("patientname",patient.getAccountName());
        mail.setModel(modelEmail);
        emailService.sendSimpleMessage(mail, "mailtemplate");
    }
  
}