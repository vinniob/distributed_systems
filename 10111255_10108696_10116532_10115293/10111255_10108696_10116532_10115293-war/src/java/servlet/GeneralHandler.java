/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import ejb.*;
import entity.*;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.commons.lang3.StringEscapeUtils;

/*
 * Servlet to handle general operations on the website.
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@WebServlet(name = "GeneralHandler",
        loadOnStartup = 1,
        urlPatterns = {"/product", "/search", "/postcomment", "/error"})
public class GeneralHandler extends HttpServlet {

  @EJB
  private AdministratorFacade administratorFacade;
  @EJB
  private CommentFacade commentFacade;
  @EJB
  private CustomerFacade customerFacade;
  @EJB
  private ProductFacade productFacade;
  private static final String ERROR_URL = "error";

  @Override
  public void init() {
    // store category list in servlet context
    getServletContext().setAttribute("products", productFacade.findAll());
  }

  /*
   * Handles the HTTP GET method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String userPath = request.getServletPath();
    HttpSession session = request.getSession();

    //Special corner case for error page request of non-signed in user
    if (!userPath.equals("/error")) {
      //Ensure user is logged in, else redirect
      if ((session.getAttribute("cust")) instanceof Customer) {
        Customer customer = (Customer) session.getAttribute("cust");;
        if (!customerFacade.validate(customer)) {
          session.invalidate();
          response.sendRedirect("signin");
          return;
        }
      } else if ((session.getAttribute("admin")) instanceof Administrator) {
        Administrator administrator = (Administrator) session.getAttribute("admin");
        if (!administratorFacade.validate(administrator)) {
          session.invalidate();
          response.sendRedirect("signin");
          return;
        }
      } else {
        session.invalidate();
        response.sendRedirect("signin");
        return;
      }
    }

    
    // If a product page is requested    
    if (userPath.equals("/product")) {

      Integer id_n;

      //Get comment from request
      String id = request.getQueryString();

      if (id != null) {
        //Parse numerical values 
        try {
          id_n = Integer.parseInt(id);
        } catch (NumberFormatException e) {
          session.setAttribute("errorMessage", "Please enter numerical values "
                  + "where required");
          response.sendRedirect(ERROR_URL);
          return;
        }
      } else {
        session.setAttribute("errorMessage", "That product does not exist");
        response.sendRedirect(ERROR_URL);
        return;
      }

      Product selectedProduct = productFacade.find(id_n);

      if (selectedProduct != null) {
        //Place selected product in request scope
        request.setAttribute("selectedProduct", selectedProduct);
      } else {
        session.setAttribute("errorMessage", "That product does not exist");
        response.sendRedirect(ERROR_URL);
        return;
      }

      
      // If error page requested
    } else if (userPath.equals("/error")) {
      //Do nothing
      
        
      // Otherwise an error has occured      
    } else {
      //Should not get here
      session.setAttribute("errorMessage", "That product does not exist");
      response.sendRedirect(ERROR_URL);
      return;
    }

    // use RequestDispatcher to forward request 
    String url = "/WEB-INF/view" + userPath + ".jsp";

    try {
      request.getRequestDispatcher(url).forward(request, response);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /*
   * Handles the HTTP POST method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String userPath = request.getServletPath();
    HttpSession session = request.getSession();
    Customer customer = null;
    
    //Ensure user is logged in, else redirect
    if ((session.getAttribute("cust")) instanceof Customer) {
      customer = (Customer) session.getAttribute("cust");;
      if (!customerFacade.validate(customer)) {
        session.invalidate();
        response.sendRedirect("signin");
        return;
      }
    } else if (!userPath.equals("/postcomment") && (session.getAttribute("admin")) instanceof Administrator) {
      Administrator administrator = (Administrator) session.getAttribute("admin");
      if (!administratorFacade.validate(administrator)) {
        session.invalidate();
        response.sendRedirect("signin");
        return;
      }
    } else if (userPath.equals("/postcomment")) {
      session.setAttribute("errorMessage", "Admins aren't allowed to post "
              + "comments");
      response.sendRedirect(ERROR_URL);
      return;
    } else {
      session.invalidate();
      response.sendRedirect("signin");
      return;
    }

    
    //search for a product in inventory
    if (userPath.equals("/search")) {
      String keyword = request.getParameter("productId");
      List<Product> searchResults;

      //If productId is not null
      if (keyword != null) {
        //If productId is a number
        if (keyword.matches("-?\\d+(\\.\\d+)?")) {
          //selectedProduct = productFacade.find(Integer.parseInt(productId));
          searchResults = productFacade.getProductsById(Integer.parseInt(keyword));
        } else {
          searchResults = productFacade.searchByTitle(keyword);
        }

        if (searchResults != null) {
          // place selected product in request scope
          request.setAttribute("searchResults", searchResults);
        }
      }

      
      // post a comment)
    } else if (userPath.equals("/postcomment")) {
      Integer id_n = null;

      //Get comment from request
      String id = request.getParameter("product_id");

      if (id != null) {
        //Parse numerical values 
        try {
          id_n = Integer.parseInt(id);
        } catch (NumberFormatException e) {
          session.setAttribute("errorMessage", "Please enter numerical values "
                  + "where required");
          response.sendRedirect(ERROR_URL);
          return;
        }
      } else {
        return;
      }

      Product product = productFacade.getProductById(id_n);

      String comments = request.getParameter("comment");

      //Clean up comment
      comments = StringEscapeUtils.escapeHtml4(comments);

      //Create and store new comment
      Comment comment = new Comment();
      comment.setComment(comments);
      comment.setCustId(customer);
      comment.setProdId(product);
      commentFacade.create(comment);

      //Store new comment in relevant product comment collection
      product.getCommentCollection().add(comment);
      productFacade.edit(product);

      response.sendRedirect("product?" + id);
      return;

    }

    // use RequestDispatcher to forward request
    String url = "/WEB-INF/view" + userPath + ".jsp";

    try {
      request.getRequestDispatcher(url).forward(request, response);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /*
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "A servlet to handle general requests on the website";
  }
}
