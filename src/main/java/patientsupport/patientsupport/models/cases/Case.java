package patientsupport.patientsupport.models.cases;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import patientsupport.patientsupport.models.parameters.Audit;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.models.parameters.InsuranceType;
import patientsupport.patientsupport.models.parameters.Stage;
import patientsupport.patientsupport.models.parameters.Status;
import patientsupport.patientsupport.models.parameters.StatusReason;
import patientsupport.patientsupport.models.parameters.Therapy;
import patientsupport.patientsupport.models.accounts.HealthOperatorAccount;
import patientsupport.patientsupport.models.accounts.Patient;
import patientsupport.patientsupport.models.accounts.Physician;

import javax.persistence.*;

/**
 * Case
 */
 @Entity
 @Table(name = "Cases")
public class Case extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Generated( value = GenerationTime.ALWAYS )
    @Column(columnDefinition = "AS 'PSP'+right('0000'+CONVERT([varchar],[id]),(4))")
    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date therapyEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date therapyStartDate;

    private int fieldNurse;

    private int insuranceTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "insuranceTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='insuranceType'")
    @JsonIgnore
    private InsuranceType insuranceType;

    private int healthOperatorAccountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "healthOperatorAccountId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='healthOperatorAccount'")
    @JsonIgnore
    private HealthOperatorAccount healthOperatorAccount;

    private int logisticOperatorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "logisticOperatorId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='logisticOperator'")
    @JsonIgnore
    private HealthOperatorAccount logisticOperator;

    private int institutionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institutionId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='institution'")
    @JsonIgnore
    private HealthOperatorAccount institution;

    private int patientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patientId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='patient'")
    @JsonIgnore
    private Patient patient;

    private int physicianId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "physicianId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='physician'")
    @JsonIgnore
    private Physician physician;

    private int statusReasonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusReasonId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='statusReason'")
    @JsonIgnore
    private StatusReason statusReason;

    private int stageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stageId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='stage'")
    @JsonIgnore
    private Stage stage;

    private int statusId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='status'")
    @JsonIgnore
    private Status status;

    private int theraphyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theraphyId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='theraphy'")
    @JsonIgnore
    private Therapy theraphy;

    private int treatmentDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "treatmentDepartmentId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='treatmentDepartment'")
    @JsonIgnore
    private Department treatmentDepartment;

    private int treatmentCityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "treatmentCityId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='treatmentCity'")
    @JsonIgnore
    private City treatmentCity;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Date getTherapyEndDate() {
        return therapyEndDate;
    }

    public void setTherapyEndDate(Date therapyEndDate) {
        this.therapyEndDate = therapyEndDate;
    }

    public Date getTherapyStartDate() {
        return therapyStartDate;
    }

    public void setTherapyStartDate(Date therapyStartDate) {
        this.therapyStartDate = therapyStartDate;
    }

    public int getFieldNurse() {
        return fieldNurse;
    }

    public void setFieldNurse(int fieldNurse) {
        this.fieldNurse = fieldNurse;
    }

    public int getInsuranceTypeId() {
        return insuranceTypeId;
    }

    public void setInsuranceTypeId(int insuranceTypeId) {
        this.insuranceTypeId = insuranceTypeId;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public int getHealthOperatorAccountId() {
        return healthOperatorAccountId;
    }

    public void setHealthOperatorAccountId(int healthOperatorAccountId) {
        this.healthOperatorAccountId = healthOperatorAccountId;
    }

    public HealthOperatorAccount getHealthOperatorAccount() {
        return healthOperatorAccount;
    }

    public void setHealthOperatorAccount(HealthOperatorAccount healthOperatorAccount) {
        this.healthOperatorAccount = healthOperatorAccount;
    }

    public int getLogisticOperatorId() {
        return logisticOperatorId;
    }

    public void setLogisticOperatorId(int logisticOperatorId) {
        this.logisticOperatorId = logisticOperatorId;
    }

    public HealthOperatorAccount getLogisticOperator() {
        return logisticOperator;
    }

    public void setLogisticOperator(HealthOperatorAccount logisticOperator) {
        this.logisticOperator = logisticOperator;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public HealthOperatorAccount getInstitution() {
        return institution;
    }

    public void setInstitution(HealthOperatorAccount institution) {
        this.institution = institution;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(int physicianId) {
        this.physicianId = physicianId;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public int getStatusReasonId() {
        return statusReasonId;
    }

    public void setStatusReasonId(int statusReasonId) {
        this.statusReasonId = statusReasonId;
    }

    public StatusReason getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(StatusReason statusReason) {
        this.statusReason = statusReason;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTheraphyId() {
        return theraphyId;
    }

    public void setTheraphyId(int theraphyId) {
        this.theraphyId = theraphyId;
    }

    public Therapy getTheraphy() {
        return theraphy;
    }

    public void setTheraphy(Therapy theraphy) {
        this.theraphy = theraphy;
    }

    public int getTreatmentDepartmentId() {
        return treatmentDepartmentId;
    }

    public void setTreatmentDepartmentId(int treatmentDepartmentId) {
        this.treatmentDepartmentId = treatmentDepartmentId;
    }

    public Department getTreatmentDepartment() {
        return treatmentDepartment;
    }

    public void setTreatmentDepartment(Department treatmentDepartment) {
        this.treatmentDepartment = treatmentDepartment;
    }

    public int getTreatmentCityId() {
        return treatmentCityId;
    }

    public void setTreatmentCityId(int treatmentCityId) {
        this.treatmentCityId = treatmentCityId;
    }

    public City getTreatmentCity() {
        return treatmentCity;
    }

    public void setTreatmentCity(City treatmentCity) {
        this.treatmentCity = treatmentCity;
    }
    
    
}

