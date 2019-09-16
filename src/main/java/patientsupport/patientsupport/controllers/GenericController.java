package patientsupport.patientsupport.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import patientsupport.patientsupport.helpers.Selects;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.StatusReason;
import patientsupport.patientsupport.models.parameters.Zone;

@Controller
@RequestMapping("generics")
public class GenericController {

    private Selects selects;

    @Autowired
    public GenericController(Selects selects) {
        this.selects = selects;
    }

    @RequestMapping(value = "/getZonesByCountryId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<Zone> getZonesByCountryId(@RequestParam Integer countryId) {
		try {
			return selects.getZones(countryId);

		} catch (Exception ex) {
			return null;
		}
    }
    
    @RequestMapping(value = "/getCitiesByDepartmentId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<City> getCitiesByDepartmentId(@RequestParam Integer departmentId) {
		try {
			return selects.getCities(departmentId);

		} catch (Exception ex) {
			return null;
		}
    }
    
    @RequestMapping(value = "/getStatusReasonByStatusId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<StatusReason> getStatusReasonByStatusId(@RequestParam Integer statusId) {
		try {
			return selects.getStatusReasons(statusId);

		} catch (Exception ex) {
			return null;
		}
    }
}