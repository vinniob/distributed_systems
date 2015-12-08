
package model;

import entity.Product;
import java.util.ArrayList;
import java.util.Collection;

/*
 * A shopping cart.
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
public class ShoppingCart {

  Collection<ShoppingCartProductInfo> list;
  int numberOfProducts;
  double total;

  public ShoppingCart() {
    list = new ArrayList<ShoppingCartProductInfo>();
    numberOfProducts = 0;
    total = 0;
  }

  /*
   * Adds a ShoppingCartProductInfo to the ShoppingCart's items list. 
   * If item of the specified product already exists in shopping cart list, 
   * the quantity of that item is increased.
   *
   * @param product: defines the type of shopping cart item
   * 
   */
  public synchronized void addItem(Product product) {
    boolean newItem = true;

    for (ShoppingCartProductInfo item : list) {
      if (item.getProduct().getId() == product.getId()) {
        newItem = false;
        item.incrementQuantity();
      }
    }

    if (newItem) {
      ShoppingCartProductInfo item = new ShoppingCartProductInfo(product);
      list.add(item);
    }
  }

  /*
   * Updates the ShoppingCartProductInfo of the product to the specified quantity. 
   * If '0' is the given quantity, the ShoppingCartProductInfo is removed from the
   * ShoppingCart's items list.
   *
   * @param product: defines the type of shopping cart item
   * @param quantity: number which the ShoppingCartItem is updated to
   * 
   */
  public synchronized void update(Product product, Integer quantity) {
    if (quantity >= 0) {
      ShoppingCartProductInfo item = null;
      for (ShoppingCartProductInfo productOnList : list) {
        if (productOnList.getProduct().getId() == product.getId()) {
          if (quantity != 0) {
            // set item quantity to new value
            productOnList.setQuantity(quantity);
          } else {
            // if quantity equals 0, save item and break
            item = productOnList;
            break;
          }
        }
      }

      if (item != null) {
        // remove from cart
        list.remove(item);
      }
    }
  }

  /*
   * Returns the list of ShoppingCartProductInfos.
   *
   * @return  products list
   * 
   */
  public synchronized Collection<ShoppingCartProductInfo> getItems() {
    return list;
  }

  /*
   *
   * @return the number of list in shopping cart
   * 
   */
  public synchronized int getNumberOfProducts() {
    numberOfProducts = 0;

    for (ShoppingCartProductInfo line : list) {
      numberOfProducts += line.getQuantity();
    }

    return numberOfProducts;
  }

  /*
   * Calculates the total cost of the order. This calculates the sum of the
   * product price multiplied by the quantity for all list in shopping cart list
   *
   */
  public synchronized void calculateTotal() {
    double amount = 0;

    for (ShoppingCartProductInfo line : list) {
      amount += line.getTotal();
    }

    this.total = amount;
  }

  /*
   * 
   *
   * @return the cost of all items times their quantities plus surcharge
   */
  public synchronized double getTotal() {
    return total;
  }

  /*
   * Empties the shopping cart. All items are removed from the shopping cart
   * items list
   * numberOfProducts and total are reset to '0'.
   *
   */
  public synchronized void clear() {
    list.clear();
    numberOfProducts = 0;
    total = 0;
  }
  
  @Override
  public ShoppingCart clone(){
    ShoppingCart cart = new ShoppingCart();
    for (ShoppingCartProductInfo line: list){
      cart.addItem(line.product);
      cart.update(line.product, line.quantity);
      cart.calculateTotal();
    }
    return cart;
  }
}