package patientsupport.patientsupport.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import patientsupport.patientsupport.helpers.UploadService;
import patientsupport.patientsupport.models.accounts.Physician;
import patientsupport.patientsupport.repository.PhysicianRepository;
import patientsupport.patientsupport.services.UserService;

@Controller
public class UploadController {

    private UploadService uploadService;
    private UserService userService;
    private PhysicianRepository _repository;

    public UploadController(UploadService uploadService, UserService userService, PhysicianRepository _repository) {
        this.uploadService = uploadService;
        this.userService = userService;
        this._repository = _repository;
        
    }

    @RequestMapping(value = "/upload-physicians", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String uploadPhysicians(@RequestParam("file") MultipartFile file) {
        
        try {
            List<Map<String, String>> list =  uploadService.uploadExcelToJson(file);
            List<Physician> physicians = new ArrayList<>();
            list.forEach(item -> {
                Physician physician = new Physician();
                physician.setSpecialistTypeId((int)Double.parseDouble(item.get("specialist_type_id")));
                physician.setActive(((int)Double.parseDouble(item.get("active"))) == 1 ? true : false);
                physician.setFirstName(item.get("firstName"));
                physician.setLastName(item.get("lastName"));
                physician.setPhone(String.valueOf((int)Double.parseDouble(item.get("phone"))));
                physician.setAccountName(item.get("accountName"));
                physician.setCreatedAt(new Date());
                physician.setCreatedBy(userService.getAuthUser().getEmail());
                physicians.add(physician);
            });
            this._repository.saveAll(physicians);
            return "{\"status\" : \"Carga Exitosa\"}";
        } catch (Exception e) {
            return "{\"status\" : \"Carga Erronea. Verifique el archivo\"}";
        }
    }
    
}