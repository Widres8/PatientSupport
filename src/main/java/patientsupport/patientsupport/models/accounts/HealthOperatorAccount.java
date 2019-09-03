package patientsupport.patientsupport.models.accounts;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.cases.Case;
import patientsupport.patientsupport.models.parameters.Audit;
import patientsupport.patientsupport.models.parameters.HealthOperatorType;

@Entity
@Table(name = "HealthOperatorAccounts")
public class HealthOperatorAccount extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "{label.required}")
    @Length(max = 100)
    private String description;

    private boolean active;

    private int healthOperatorTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "healthOperatorTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='healthOperatorType'")
    @JsonIgnore
    private HealthOperatorType healthOperatorType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "healthOperatorAccountId")
    @JsonIgnore
    private Set<Case> casesHmo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "logisticOperatorId")
    @JsonIgnore
    private Set<Case> casesLo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "institutionId")
    @JsonIgnore
    private Set<Case> casesI;

    public int getId() {
        return id;
    }

    public int getHealthOperatorTypeId() {
        return healthOperatorTypeId;
    }

    public void setHealthOperatorTypeId(int healthOperatorTypeId) {
        this.healthOperatorTypeId = healthOperatorTypeId;
    }

    public HealthOperatorType getHealthOperatorType() {
        return healthOperatorType;
    }

    public void setHealthOperatorType(HealthOperatorType healthOperatorType) {
        this.healthOperatorType = healthOperatorType;
    }
    
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
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