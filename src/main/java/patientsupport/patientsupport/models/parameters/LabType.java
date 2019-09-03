package patientsupport.patientsupport.models.parameters;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import  patientsupport.patientsupport.models.cases.Lab;

/**
 * LabType
 */
@Entity
@Table(name = "LabTypes", uniqueConstraints = @UniqueConstraint(columnNames = { "description" }))

public class LabType extends Audit<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "This field is required")
    @Length(max = 50)
    private String description;

    private boolean active;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "labTypeId")
    @JsonIgnore
    private Set<Lab> labs;

    public int getId() {
        return id;
    }

    public Set<Lab> getLabs() {
        return labs;
    }

    public void setLabs(Set<Lab> labs) {
        this.labs = labs;
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