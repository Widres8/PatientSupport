package patientsupport.patientsupport.models.parameters;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.cases.Case;

/**
 * Status  */
@Entity
@Table(name = "Status ", uniqueConstraints = @UniqueConstraint(columnNames = { "description" }))

public class Status extends Audit<String> {
    
    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "This field is required")
    @Length(max = 50)
    private String description;

    private boolean active;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "StatusId")
    @JsonIgnore
    private Set<StatusReason> statusReasons;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "statusId")
    @JsonIgnore
    private Set<Case> cases;

    public int getId() {
        return id;
    }

    public Set<StatusReason> getStatusReasons() {
        return statusReasons;
    }

    public void setStatusReasons(Set<StatusReason> statusReasons) {
        this.statusReasons = statusReasons;
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