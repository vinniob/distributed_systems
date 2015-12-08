/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import ejb.*;
import entity.Administrator;
import entity.Customer;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/*
 * A servlet to handle authentication.
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@WebServlet(name = "AccountHandler", urlPatterns = {"/signup", "/signin",
    "/signout"})
public class AccountHandler extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private AdministratorFacade administratorFacade;
    private static final String ERROR_URL = "error";

    /**
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

        //If signout page requested
        if (userPath.equals("/signout")) {
            session.invalidate();
            response.sendRedirect("");
            return;
        }

        // use RequestDispatcher to forward request 
        String url = "/WEB-INF/view/auth" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
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

        // If signup action called 
        if (userPath.equals("/signup")) {

            //String variables to hold values from form
            String fullname, email, username, password, adminOrUser;
            Character adminOrUser_c;

            fullname = request.getParameter("fullname");
            email = request.getParameter("email");
            username = request.getParameter("username");
            password = request.getParameter("password");
            adminOrUser = request.getParameter("admin_or_user");

            //verify form is valid
            if (fullname == null || email == null || username == null
                    || password == null || adminOrUser == null) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //verify form is valid
            if (fullname.isEmpty() || email.isEmpty() || username.isEmpty()
                    || password.isEmpty() || adminOrUser.isEmpty()) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            if (adminOrUser.equals("a")) {
                //Test username doesn't exist
                if (administratorFacade.exists(username)) {
                    session.setAttribute("errorMessage", "username is already taken, please try again");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Create customer
                administratorFacade.addAdministrator(fullname, email, username, password);

                //Retrieve full entity from database
                Administrator admin = administratorFacade
                        .getAdministratorByUsername(username);

                //Set session attribute
                session.setAttribute("admin", admin);

                //If creating a new user
            } else {
                //Test username does not exist
                if (customerFacade.exists(username)) {
                    session.setAttribute("errorMessage", "That username is already taken");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Create customer
                customerFacade.addCustomer(fullname, email, username, password);

                //Retrieve full entity from database
                Customer customer = customerFacade.getCustomerByUsername(username);

                //Set session attribute
                session.setAttribute("cust", customer);
            }

            // If signin action called (i.e. signing in)            
        } else if (userPath.equals("/signin")) {

            //String variables to hold values from form
            String username, password, adminOrUser;

            username = request.getParameter("username");
            password = request.getParameter("password");
            adminOrUser = request.getParameter("admin_or_user");

            //Ensure form is valid
            if (username == null || password == null || adminOrUser == null) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Ensure form is valid
            if (username.isEmpty() || password.isEmpty() || adminOrUser.isEmpty()) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            if (adminOrUser.equals("a")) {
                //Test username does exists and password is valid
                if (!administratorFacade.exists(username)) {
                    session.setAttribute("errorMessage", "The username/password is invalid (A)");
                    response.sendRedirect(ERROR_URL);
                    return;
                } else if (!administratorFacade.validate(username, password)) {
                    session.setAttribute("errorMessage", "The username/password is invalid (A)");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                Administrator administrator = administratorFacade.getAdministratorByUsername(username);

                //  entire user object in session
                session.setAttribute("admin", administrator);

            } else {
                //Test username does exists and password is valid
                if (!customerFacade.exists(username)) {
                    session.setAttribute("errorMessage", "The username/password is invalid (C1)");
                    response.sendRedirect(ERROR_URL);
                    return;
                } else if (!customerFacade.validate(username, password)) {
                    session.setAttribute("errorMessage", "The username/password is invalid (C2)");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                Customer customer = customerFacade.getCustomerByUsername(username);

                //  entire user object in session
                session.setAttribute("cust", customer);
            }

            // If signout action called (i.e. signing out)            
        } else if (userPath.equals("/signout")) {

            //Invalidate the session, clear all attributes
            session.invalidate();

        
        // Otherwise an error has occurred
        } else {
            session.setAttribute("errorMessage", "The URL is not a functioninng part "
                    + "of this website");
            response.sendRedirect(ERROR_URL);
            return;
        }

        //Send to home page
        response.sendRedirect("");
    }

    /*
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "A servlet to handle authentication.";
    }
}
