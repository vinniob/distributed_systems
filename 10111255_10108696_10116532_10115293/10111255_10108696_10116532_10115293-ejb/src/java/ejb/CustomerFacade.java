
package ejb;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Facade EJB for customer entity class
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CustomerFacade() {
    super(Customer.class);
  }

  /**
   * Create a new customer and store to the database.
   *
   * @param name The name of the customer
   * @param username The username of the customer
   * @param email The email of the customer
   * @param password The password of the customer
   */
  public void addCustomer(String name, String username, String email,
          String password) {
    Customer cust = new Customer(name, username, email, password);
    create(cust);
  }

  /**
   * Get a customer with the given ID.
   *
   * @param id The customer ID
   * @return The matching customer if match found else null
   */
  public Customer getCustomerById(int id) {
    try {
      Customer result = (Customer) em
              .createNamedQuery("Customer.findByUsername")
              .setParameter("id", id)
              .getSingleResult();
      return result;
    } catch (javax.persistence.NoResultException e) {
      return null;
    }
  }

  /**
   * Get a customer with the given username.
   *
   * @param username The customer username
   * @return The matching customer if match found else null
   */
  public Customer getCustomerByUsername(String username) {
    try {
      Customer result = (Customer) em
              .createNamedQuery("Customer.findByUsername")
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
   * @param username The customer username
   * @return True is customer exists else false
   */
  public boolean exists(String username) {
    try {
      Customer result = (Customer) em
              .createNamedQuery("Customer.findByUsername")
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
   * @param username The customer username
   * @param password The customer (attempted) password
   * @return True if username exists and password is correct else false
   */
  public boolean validate(String username, String password) {
    try {
      Customer result = (Customer) em
              .createNamedQuery("Customer.findByUsername")
              .setParameter("username", username)
              .getSingleResult();
      //TODO Implement some form of hashing + salting method here
      return (result.getPassword().equals(password));
    } catch (javax.persistence.NoResultException e) {
      return false;
    }
  }
  
  /**
   * Check if a user is valid. Essentially an equality method
   * 
   * @param customer The customer object to validate
   * @return True if customer is valid else false
   */
  public boolean validate(Customer customer){
    return (validate(customer.getUsername(), customer.getPassword()));
  }
}