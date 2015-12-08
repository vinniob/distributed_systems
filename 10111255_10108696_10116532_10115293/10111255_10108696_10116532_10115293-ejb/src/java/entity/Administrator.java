
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Administrator entity class
 * 
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Entity
@Table(name = "ADMINISTRATOR")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
  @NamedQuery(name = "Administrator.findById", query = "SELECT a FROM Administrator a WHERE a.id = :id"),
  @NamedQuery(name = "Administrator.findByFullname", query = "SELECT a FROM Administrator a WHERE a.fullname = :fullname"),
  @NamedQuery(name = "Administrator.findByEmail", query = "SELECT a FROM Administrator a WHERE a.email = :email"),
  @NamedQuery(name = "Administrator.findByUsername", query = "SELECT a FROM Administrator a WHERE a.username = :username"),
  @NamedQuery(name = "Administrator.findByPassword", query = "SELECT a FROM Administrator a WHERE a.password = :password")})
public class Administrator implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "FULLNAME")
  private String fullname;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "EMAIL")
  private String email;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "USERNAME")
  private String username;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "PASSWORD")
  private String password;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "adminId")
  private Collection<Log> logCollection;

  public Administrator() {
  }

  public Administrator(Integer id) {
    this.id = id;
  }

  public Administrator(String fullname, String email, String username, String password) {
    this.fullname = fullname;
    this.email = email;
    this.username = username;
    this.password = password;
  }
  
  public Administrator(Integer id, String fullname, String email, String username, String password) {
    this.id = id;
    this.fullname = fullname;
    this.email = email;
    this.username = username;
    this.password = password;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @XmlTransient
  public Collection<Log> getLogCollection() {
    return logCollection;
  }

  public void setLogCollection(Collection<Log> logCollection) {
    this.logCollection = logCollection;
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
    if (!(object instanceof Administrator)) {
      return false;
    }
    Administrator other = (Administrator) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Administrator[ id=" + id + " ]";
  }
  
}
