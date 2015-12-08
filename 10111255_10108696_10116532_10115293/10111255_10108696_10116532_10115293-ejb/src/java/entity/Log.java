
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The log entity class
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Entity
@Table(name = "LOG")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
  @NamedQuery(name = "Log.findById", query = "SELECT l FROM Log l WHERE l.id = :id"),
  @NamedQuery(name = "Log.findByComment", query = "SELECT l FROM Log l WHERE l.comment = :comment"),
  @NamedQuery(name = "Log.findByDateCreated", query = "SELECT l FROM Log l WHERE l.dateCreated = :dateCreated")})
public class Log implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "COMMENT")
  private String comment;
  @Basic(optional = false)
  @Column(name = "DATE_CREATED", insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated;
  @JoinColumn(name = "ADMIN_ID", referencedColumnName = "ID")
  @ManyToOne(optional = false)
  private Administrator adminId;

  public Log() {
  }

  public Log(Integer id) {
    this.id = id;
  }

  public Log(Integer id, String comment, Date dateCreated) {
    this.id = id;
    this.comment = comment;
    this.dateCreated = dateCreated;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Administrator getAdminId() {
    return adminId;
  }

  public void setAdminId(Administrator adminId) {
    this.adminId = adminId;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Log)) {
      return false;
    }
    Log other = (Log) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Log[ id=" + id + " ]";
  }
  
}
