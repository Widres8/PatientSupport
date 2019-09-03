package patientsupport.patientsupport.models.accounts;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import patientsupport.patientsupport.models.cases.Case;
import patientsupport.patientsupport.models.parameters.Audit;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.Department;
import patientsupport.patientsupport.models.parameters.DocumentType;

@Entity
@Table(name = "Patients", indexes = {
    //@Index(columnList = "email, document", unique = true)
})
public class Patient extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "This field is required")
    @Length(max = 50)
    private String firstName;

    @Length(max = 50)
    private String middleName;

    @NotEmpty(message = "This field is required")
    @Length(max = 50)
    private String lastName;

    @NotEmpty(message = "This field is required")
    @Length(max = 100)
    private String accountName;

    @NotEmpty(message = "This field is required")
    @Length(max = 150)
    private String address;

    @NotEmpty(message = "This field is required")
    @Length(max = 100)
    private String email;

    @NotEmpty(message = "This field is required")
    @Length(max = 20)
    private String document;

    @NotEmpty(message = "This field is required")
    @Length(max = 20)
    private String gender;

    @Length(max = 10)
    private String initials;

    @NotEmpty(message = "This field is required")
    @Length(max = 20)
    private String phone1;

    @Length(max = 20)
    private String phone2;

    @Min(value = 0)
    private int age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;

    private boolean active;

    private int departmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departmentId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='country'")
    @JsonIgnore
    private Department department;

    private int cityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cityId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='country'")
    @JsonIgnore
    private City city;

    private int documentTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "documentTypeId", referencedColumnName = "id", insertable= false, updatable = false)
    @Where(clause = "identificador='country'")
    @JsonIgnore
    private DocumentType documentType;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "patientId")
    @JsonIgnore
    private Set<Case> cases;

    public String fullName() {
        return firstName + ' ' + middleName + ' '  + lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Set<Case> getCases() {
        return cases;
    }

    public void setCases(Set<Case> cases) {
        this.cases = cases;
    }

}