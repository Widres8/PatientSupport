package patientsupport.patientsupport.models.events;

import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.parameters.Audit;

@Entity
@Table(name = "EventTypes",uniqueConstraints = @UniqueConstraint(columnNames = {"description"}))
public class EventType extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotEmpty(message = "{label.required}")
    @Length(max = 50)
    private String description;
    
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name = "EventTypeId")
    @JsonIgnore
    private Set<Event> events;

    public boolean getActive() {
        return active;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    


    
}