package patientsupport.patientsupport.models.parameters;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.cases.Case;

/**
 * InsuranceType
 */
@Entity
@Table(name = "InsuranceTypes", uniqueConstraints = 
    @UniqueConstraint(columnNames = {"description"})
)
public class InsuranceType extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Length(max = 50)
    @NotEmpty(message = "{label.required}")
    private String description;

    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "insuranceTypeId")
    @JsonIgnore
    private Set<Case> cases;

    public int getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}