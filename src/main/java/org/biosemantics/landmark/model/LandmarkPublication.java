package org.biosemantics.landmark.model;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;



/**
 *
 * @author reinout
 * @author evanderhorst
 * @author mark
 * 
 * mark says:
 * a claim that doesn't pass the tests below will return to an empty form
 *  (error logged on the server): for now let's accept everything
 */
public class LandmarkPublication implements Serializable {

    private static final long serialVersionUID = 1987982394L;
    @NotEmpty(message = "Subject may not be empty")
    private String subjectUri;
    @NotEmpty(message = "Object may not be empty")
    private String objectUri;
    @NotEmpty(message = "Subject label may not be empty")
    private String subjectLabel;
    @NotEmpty(message = "Object label may not be empty")
    private String objectLabel;
    
    @NotEmpty(message = "DOI or PubMed ID may not be empty")
    private String pubmedId;
    @Past(message = "Publication date must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date publicationDate; // TODO: Date instead of String?
    private String claimDate;
    private String id; 
    private String curatorId;
    private String institution;
    private String experimentType;
    private String discoveryType;
    private String details;
    private String author;
    private String nanoPublicationURL;
    private String nanoPublicationTrigString;
    
    

    public LandmarkPublication() {
    }

    public String getId() {
		return id;
	}

	public void setId(String landmarkId) {
		this.id = landmarkId;
	}

	public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDiscoveryType() {
        return discoveryType;
    }

    public void setDiscoveryType(String discoveryType) {
        this.discoveryType = discoveryType;
    }

    public String getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

    public String getCuratorId() {
        return curatorId;
    }

    public void setCuratorId(String curatorId) {
        this.curatorId = curatorId;
    }

    public String getSubjectUri() {
        return subjectUri;
    }

    public void setSubjectUri(String subject) {
        this.subjectUri = subject;
    }

    public String getObjectUri() {
        return objectUri;
    }

    public void setObjectUri(String object) {
        this.objectUri = object;
    }

    public String getSubjectLabel() {
        return subjectLabel;
    }

    public void setSubjectLabel(String subjectLabel) {
        this.subjectLabel = subjectLabel;
    }

    public String getObjectLabel() {
        return objectLabel;
    }

    public void setObjectLabel(String objectLabel) {
        this.objectLabel = objectLabel;
    }
    
    public String getNanoPublicationURL() {
        return nanoPublicationURL;
    }

    public void setNanoPublicationURL(String nanoPublicationURL) {
        this.nanoPublicationURL = nanoPublicationURL;
    }

    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     *
     * @param publicationDate
     */
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }
    
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * @return the nanoPublicationTrigString
     */
    public String getNanoPublicationTrigString() {
        return nanoPublicationTrigString;
    }

    /**
     * @param nanoPublicationTrigString the nanoPublicationTrigString to set
     */
    public void setNanoPublicationTrigString(String nanoPublicationTrigString) {
        this.nanoPublicationTrigString = nanoPublicationTrigString;
    }

    
    
}
