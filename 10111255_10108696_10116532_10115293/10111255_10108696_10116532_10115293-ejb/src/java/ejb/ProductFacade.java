package ejb;

import entity.Product;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Facade EJB for the product entity class
 * 
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ProductFacade() {
    super(Product.class);
  }

  /**
   * Get a product with the given ID.
   *
   * @param id The product ID
   * @return The matching product if match found else null
   */
  public Product getProductById(int id) {
    try {
      Product result = (Product) em
              .createNamedQuery("Product.findById")
              .setParameter("id", id)
              .getSingleResult();
      return result;
    } catch (javax.persistence.NoResultException e) {
      return null;
    }
  }

  /**
   * Get a list of products with matching ID.
   *
   * @param id The id to search for
   * @return A list of matching products
   */
  public List<Product> getProductsById(int id) {
    return em.createNamedQuery("Product.findById")
            .setParameter("id", id)
            .getResultList();
  }

  /**
   * Get a list of products with matching titles
   *
   * @param title The title to search for
   * @return A list of matching products
   */
  public List<Product> searchByTitle(String title) {
    return em.createNamedQuery("Product.searchByTitle")
            .setParameter("title", "%" + title + "%")
            .getResultList();
  }

  /**
   * Create a new product and store to the database.
   *
   * @param title
   * @param description
   * @param price
   * @param stock
   */
  public void addProduct(String title, String description, Float price, Integer stock) {
    Product product = new Product(title, description, price, stock);
    create(product);
  }

  /**
   * Update an existing product in the database.
   *
   * @param title
   * @param description
   * @param price
   * @param stock
   * @return True if product successfully updated else false
   */
  public boolean updateProduct(Integer id, String title, String description,
          Float price, Integer stock) {
    Product product = getProductById(id);

    if (product == null) {
      return false;
    }

    if (!title.isEmpty()) {
      product.setTitle(title);
    }
    if (!description.isEmpty()) {
      product.setDescription(description);
    }
    if (price != null) {
      product.setPrice(price);
    }
    if (stock != null) {
      if (stock <= 0) {
        remove(product);
        return true;
      }
      product.setStock(stock);
    }

    //Update Product
    edit(product);
    return true;
  }

  /**
   * Checkout a product by decrementing the quantity of a product.
   *
   * @param id The id of the individual product to checkout
   * @param quantity The quantity of the product to checkout
   * @return True if product successfully checked out else false
   */
  public boolean checkout(int id, int quantity) {
    Product product = getProductById(id);

    if (product == null) {
      return false;
    }

    if (product.getStock() < quantity) {
      return false;
    }

    //Update stock count
    product.setStock(product.getStock() - quantity);
    return true;
  }

  /**
   * Remove an existing product from the database.
   *
   * @param id The id of the product to remove
   * @return True if product successfully removed else false
   */
  public boolean removeProduct(int id) {
    Product product = getProductById(id);

    if (product == null) {
      return false;
    }

    //Remove Product
    remove(product);
    return true;
  }

  /**
   * Check if a product with a given ID exists.
   *
   * @param username The product ID
   * @return True is product exists
   */
  public boolean exists(Integer id) {
    try {
      Product result = (Product) em
              .createNamedQuery("Product.findById")
              .setParameter("id", id)
              .getSingleResult();
      return (result != null);
    } catch (javax.persistence.NoResultException e) {
      return false;
    }
  }
}
