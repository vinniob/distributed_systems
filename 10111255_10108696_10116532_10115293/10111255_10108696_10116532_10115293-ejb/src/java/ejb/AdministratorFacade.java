
package ejb;

import entity.Administrator;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * A facade EJB for the administrator entity class
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Stateless
public class AdministratorFacade extends AbstractFacade<Administrator> {

  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AdministratorFacade() {
    super(Administrator.class);
  }

  /**
   * Create a new administrator and store to the database.
   *
   * @param name The name of the administrator
   * @param username The username of the administrator
   * @param email The email of the administrator
   * @param password The password of the administrator
   */
  public void addAdministrator(String name, String username, String email,
          String password) {
    Administrator admin = new Administrator(name, username, email, password);
    create(admin);
  }

  /**
   * Get a product with the given ID.
   *
   * @param id The administrator ID
   * @return The matching administrator if match found else null
   */
  public Administrator getAdministratorById(int id) {
    try {
      Administrator result = (Administrator) em
              .createNamedQuery("Administrator.findById")
              .setParameter("id", id)
              .getSingleResult();

      return result;
    } catch (javax.persistence.NoResultException e) {
      return null;
    }
  }

  /**
   * Get an administrator with the given username.
   *
   * @param username The administrator username
   * @return The matching administrator if match found else null
   */
  public Administrator getAdministratorByUsername(String username) {
    try {
      Administrator result = (Administrator) em
              .createNamedQuery("Administrator.findByUsername")
              .setParameter("username", username)
              .getSingleResult();

      return result;
    } catch (javax.persistence.NoResultException e) {
      return null;
    }
  }

  /**
   * Check if a customer with a given username exists.
   *
   * @param username The administrator username
   * @return True is administrator exists else false
   */
  public boolean exists(String username) {
    try {
      Administrator result = (Administrator) em
              .createNamedQuery("Administrator.findByUsername")
              .setParameter("username", username)
              .getSingleResult();

      return (result != null);
    } catch (javax.persistence.NoResultException e) {
      return false;
    }
  }

  /**
   * Check if a password is correct
   *
   * @param username The administrator username
   * @param password The administrator (attempted) password
   * @return True if username exists and password is correct else false
   */
  public boolean validate(String username, String password) {
    Administrator result = (Administrator) em
            .createNamedQuery("Administrator.findByUsername")
            .setParameter("username", username)
            .getSingleResult();

    //Ensure user actually exists
    if (result == null) {
      return false;
    }

    //TODO Implement some form of hashing + salting method here
    return (result.getPassword().equals(password));
  }
  
  /**
   * Check if a user is valid. Essentially an equality method
   * 
   * @param customer The customer object to validate
   * @return True if customer is valid else false
   */
  public boolean validate(Administrator administrator){
    return (validate(administrator.getUsername(), administrator.getPassword()));
  }
}
