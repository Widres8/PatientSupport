package patientsupport.patientsupport.models.parameters;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.cases.Case;

/**
 * Therapy
 */
@Entity
@Table(name = "Therapies", indexes = {
    @Index(columnList = "description, product", unique = true)
})
public class Therapy extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Length(max = 50)
    @NotEmpty(message = "{label.required}")
    private String description;

    @Length(max = 50)
    @NotEmpty(message = "{label.required}")
    private String product;

    @Length(max = 255)
    @NotEmpty(message = "{label.required}")
    private String indication;

    private Boolean active;

    private Boolean core;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "statusReasonId")
    @JsonIgnore
    private Set<Case> cases;

    public int getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public Boolean getCore() {
        return core;
    }

    public void setCore(Boolean core) {
        this.core = core;
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