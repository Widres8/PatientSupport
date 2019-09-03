package patientsupport.patientsupport.models.cases;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import patientsupport.patientsupport.models.parameters.Audit;
import patientsupport.patientsupport.models.parameters.ServiceType;


/**
 * Service
 */
@Entity
@Table(name = "Services")
public class Service extends Audit<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date scheduleDate;

    private Date serviceDate;

	private boolean provided;
	
	@NotEmpty(message = "{label.required}")
    @Length(max = 50)
    private String Reason;

    private int serviceTypeId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "serviceTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='serviceType'")
    @JsonIgnore
    private ServiceType serviceType;
	
	
    private int CaseId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caseId", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "identificador='caseModel'")
    @JsonIgnore
    private Case caseModel;

    public int getId() {
        return id;
    }

    public Case getCaseModel() {
        return caseModel;
    }

    public void setCaseModel(Case caseModel) {
        this.caseModel = caseModel;
    }

    public void setId(int id) {
		this.id = id;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public boolean isProvided() {
		return provided;
	}

	public void setProvided(boolean provided) {
		this.provided = provided;
	}

	public int getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public int getCaseId() {
		return CaseId;
	}

	public void setCaseId(int caseId) {
		CaseId = caseId;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
    
    
}