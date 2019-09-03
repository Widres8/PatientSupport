package patientsupport.patientsupport.models.cases;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.parameters.Audit;
import patientsupport.patientsupport.models.parameters.LabType;


/**
 * lab
 */
@Entity
@Table(name = "Labs")
public class Lab extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "{label.required}")
    @Length(max = 50)
    private String laboratoryName;

    private Date requestDate;

    private Date resultDate;

    private int labTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "labTypeId", referencedColumnName = "id",insertable =
    false, updatable = false)
    @Where(clause = "identificador ='labType'")
    @JsonIgnore
    private LabType labType;

    public int getId() {
        return id;
    }

    public LabType getLabType() {
        return labType;
    }

    public void setLabType(LabType labType) {
        this.labType = labType;
    }

    public int getLabTypeId() {
        return labTypeId;
    }

    public void setLabTypeId(int labTypeId) {
        this.labTypeId = labTypeId;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getLaboratoryName() {
        return laboratoryName;
    }

    public void setLaboratoryName(String laboratoryName) {
        this.laboratoryName = laboratoryName;
    }

    public void setId(int id) {
        this.id = id;
    }

}