/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Product;

/*
 * Info on product in shopping cart
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
public class ShoppingCartProductInfo {

  Product product;
  int quantity;

  /*
   * Constructor for one item.
   * @param product: productto create the line around
   */
  public ShoppingCartProductInfo(Product product) {
    this.product = product;
    this.quantity = 1;
  }

  /*
   * Constructor for multiple items.
   * 
   * @param quantity: quantity of the Product
   */
  public ShoppingCartProductInfo(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  /*
   * Get the Product in the line.
   * @return: The product in the line
   */
  public Product getProduct() {
    return product;
  }

  /*
   * Set the item type in the line.
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /*
   * Get the quantity of the Product in the line.
   * @return The quantity of items in the line
   */
  public int getQuantity() {
    return quantity;
  }

  /*
   * Set the quantity of the Product in the line.
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /*
   * Increment the amount of the Product in the line by 1.
   */
  public void incrementQuantity() {
    quantity++;
  }

  /*
   * Decrement the amount of the Product in the line by 1.
   */
  public void decrementQuantity() {
    quantity--;
  }

  /*
   * Get total value of line.
   * @return Total value of cart line
   */
  public double getTotal() {
    double amount = 0;
    amount = (this.getQuantity() * product.getPrice());
    amount = (double)Math.round(amount * 100) / 100;
    return amount;
  }
}
