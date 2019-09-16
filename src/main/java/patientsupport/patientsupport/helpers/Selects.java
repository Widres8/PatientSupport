package patientsupport.patientsupport.helpers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.Country;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.models.parameters.StatusReason;
import patientsupport.patientsupport.models.parameters.Zone;
import patientsupport.patientsupport.repository.CityRepository;
import patientsupport.patientsupport.repository.CountryRepository;
import patientsupport.patientsupport.repository.DepartmentRepository;
import patientsupport.patientsupport.repository.StatusReasonRepository;
import patientsupport.patientsupport.repository.ZoneRepository;

@Component
public class Selects {

    private CountryRepository countryRepository;
    private ZoneRepository zoneRepository;
    private DepartmentRepository departmentRepository;
    private CityRepository cityRepository;
    private StatusReasonRepository statusReasonRepository;

    @Autowired
    public Selects(
        CountryRepository countryRepository,
        ZoneRepository zoneRepository,
        DepartmentRepository departmentRepository,
        CityRepository cityRepository,
        StatusReasonRepository statusReasonRepository
    ) {
        this.countryRepository = countryRepository;
        this.zoneRepository = zoneRepository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
        this.statusReasonRepository = statusReasonRepository;
    }

    public List<Country> getCountries() {
        Iterable<Country> list = () -> StreamSupport.stream(countryRepository.findAll().spliterator(), false)
        .filter(c -> c.getZones().size() > 0)
        .iterator();

        List<Country> result = StreamSupport.stream(list.spliterator(), false)
        .collect(Collectors.toList());
        return result;
    }

    public List<Zone> getZones(Integer countryId) {
        List<Zone> result = zoneRepository
        .findByCountryId(countryId)
        .stream()
        .filter( x -> x.getActive())
        .collect(Collectors.toList());
        return result;
    }

    public List<Department> getDepartments() {
        Iterable<Department> list = () -> StreamSupport
        .stream(departmentRepository.findAll().spliterator(), false)
        .filter(c -> c.getCities().size() > 0)
        .iterator();

        List<Department> result = StreamSupport.stream(list.spliterator(), false)
        .collect(Collectors.toList());
        return result;
    }

    public List<City> getCities(Integer departmentId) {
        return cityRepository.findByDepartmentId(departmentId);
    }

    public List<StatusReason> getStatusReasons(Integer statusId) {
        return statusReasonRepository.findByStatusId(statusId);
    }
}