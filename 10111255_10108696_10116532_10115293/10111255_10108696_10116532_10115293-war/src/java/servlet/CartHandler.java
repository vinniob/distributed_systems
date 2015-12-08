/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import ejb.*;
import entity.*;
import java.io.*;
import java.util.logging.*;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.*;

/*
 * Servlet that manages the shopping cart
 *
 * @author  Rory Burns, 10108696 
 *          Vincent O'Brien, 10111255 
 *          Jamie Chambers, 10116532 
 *          Ger Lynch, 10115293
 */
@WebServlet(name = "CartHandler", urlPatterns = {"/cart", "/updateCart",
    "/purchase", "/cancel"})
public class CartHandler extends HttpServlet {

    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private AdministratorFacade administratorFacade;
    @EJB
    private ProductFacade productFacade;
    @Resource(mappedName = "jms/NewLogMessage")
    private Queue newLogMessage;
    @Resource(mappedName = "jms/NewLogMessageFactory")
    private ConnectionFactory newLogMessageFactory;
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

        //Ensure user is logged in, else redirect
        if ((session.getAttribute("cust")) instanceof Customer) {
            Customer customer = (Customer) session.getAttribute("cust");
            if (!customerFacade.validate(customer)) {
                session.invalidate();
                response.sendRedirect("signin");
                return;
            }
        } else {
            session.invalidate();
            response.sendRedirect("signin");
            return;
        }

        //-----------------------------------------------------------------------
        // If cart page requested (i.e. viewing the cart)
        //-----------------------------------------------------------------------
        if (userPath.equals("/cart")) {
            // get clear parameter from request if it exists
            String clear = request.getParameter("clear");

            if ((clear != null) && clear.equals("true")) {
                // get shopping cart from session and clear it
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                cart.clear();
            }

            //-----------------------------------------------------------------------
            // Otherwise an error has occurred
            //-----------------------------------------------------------------------
        } else {
            session.setAttribute("errorMessage", "The URL is not a functioninng part "
                    + "of this website");
            response.sendRedirect(ERROR_URL);
            return;
        }

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

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
        StringBuilder sb = new StringBuilder();
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        //Ensure user is logged in, else redirect
        if ((session.getAttribute("cust")) instanceof Customer) {
            Customer customer = (Customer) session.getAttribute("cust");;
            if (!customerFacade.validate(customer)) {
                session.invalidate();
                response.sendRedirect("signin");
                return;
            }
        } else {
            session.invalidate();
            response.sendRedirect("signin");
            return;
        }

        // If adding item to cart
        if (userPath.equals("/cart")) {

            Integer id_n;

            //If user is adding item to cart for first time, create cart object and 
            //  attach it to user session
            if (cart == null) {
                cart = new ShoppingCart();
                session.setAttribute("cart", cart);
            }

            //Get productID from request
            String id = request.getParameter("productId");

            //Ensure form is valid
            if (id == null) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Ensure form is valid
            if (id.isEmpty()) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Parse numerical values 
            try {
                id_n = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Please enter numerical values "
                        + "where required");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Ensure product exists
            if (!productFacade.exists(id_n)) {
                session.setAttribute("errorMessage", "That product does not exist");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Find matching product
            Product product = productFacade.find(id_n);

            //Process add
            cart.addItem(product);

            response.sendRedirect("cart");
            return;

            //if updateCart action is called 
        } else if (userPath.equals("/updateCart")) {

            Integer id_n, quantity_n;
            Integer row = -1;
            Boolean doRemove = null;

            //Check if update or delete operation called and get relevant item
            String[] buttons;
            if ((buttons = request.getParameterValues("update")) != null) {
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i] != null) {
                        row = i;
                        doRemove = false;
                        break;
                    }
                }
            } else if ((buttons = request.getParameterValues("remove")) != null) {
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i] != null) {
                        row = i;
                        doRemove = true;
                        break;
                    }
                }
            } else {
                //Should never get here
                session.setAttribute("errorMessage", "An unknown error occured");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //If editing quantities of an item to the cart
            if (doRemove != null && !doRemove) {

                //Get request parameter(s)
                String id = request.getParameterValues("id")[row];
                String quantity = request.getParameterValues("quantity")[row];

                //Ensure form is valid
                if (id == null || quantity == null) {
                    session.setAttribute("errorMessage", "Please complete the form");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Parse numerical values 
                try {
                    id_n = Integer.parseInt(id);
                    quantity_n = Integer.parseInt(quantity);
                } catch (NumberFormatException e) {
                    session.setAttribute("errorMessage", "Please enter numerical values "
                            + "where required");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Ensure product exists
                if (!productFacade.exists(id_n)) {
                    session.setAttribute("errorMessage", "That product does not exist");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Find matching product
                Product product = productFacade.find(id_n);

                //Ensure no more than available stock is added to the backet
                if (quantity_n > product.getStock()) {
                    quantity_n = product.getStock();
                }

                //Process delete
                cart.update(product, quantity_n);

                //If removing an item from the cart
            } else if (doRemove != null && doRemove) {

                //Get request parameter(s)
                String id = request.getParameterValues("id")[row];

                //Ensure form is valid
                if (id == null) {
                    session.setAttribute("errorMessage", "Please complete the form");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Parse numerical values
                try {
                    id_n = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    session.setAttribute("errorMessage", "Please enter numerical values "
                            + "where required");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Ensure product exists
                if (!productFacade.exists(id_n)) {
                    session.setAttribute("errorMessage", "That product does not exist");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Find matching product
                Product product = productFacade.find(id_n);

                //Process delete by updating with a quantity of 0
                cart.update(product, 0);
            }

            response.sendRedirect("cart");
            return;

            
            // If purchase action is called            
        } else if (userPath.equals("/purchase")) {

            //If the cart is empty, there is nothing to checkout so return
            if (cart == null) {
                response.sendRedirect("cart");
                return;
            }

            StringBuilder errorMessage = new StringBuilder();

            // Ensure there is enough stock to cover the order
            for (ShoppingCartProductInfo prodInfo : cart.getItems()) {
                if (prodInfo.getProduct().getStock() < prodInfo.getQuantity()) {
                    errorMessage.append(" '").append(prodInfo.getProduct().getTitle())
                            .append("'");
                }
            }

            if (errorMessage.length() > 0) {
                session.setAttribute("errorMessage", "The quantities of the following "
                        + "items exceed stock levels:" + errorMessage);
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Save a message to be added to the log
            sb.append("Order Complete (");

            //Checkout each item in the shopping cart
            for (ShoppingCartProductInfo prodInfo1 : cart.getItems()) {
                sb.append(prodInfo1.getProduct().getTitle())
                        .append(" ")
                        .append(prodInfo1.getQuantity())
                        .append(",");
                productFacade.checkout(prodInfo1.getProduct().getId(), prodInfo1.getQuantity());
            }
            sb.append(")");

            ShoppingCart checkoutCart;
            checkoutCart = (ShoppingCart) cart.clone();

            request.setAttribute("checkoutCart", checkoutCart);

            //Clear the cart
            cart.clear();

            userPath = "/confirmation";

            
            
            //if cancel action is called 
        } else if (userPath.equals("/cancel")) {
            Customer customer = new Customer();
            
            //If the cart is empty, there is nothing to cancel so return
            if (cart == null) {
                response.sendRedirect("cart");
                return;
            } else //Clear the cart
            {
                cart.clear();
            }
            
            //Save a message to be added to the log
            sb.append("Order cancelled");

            userPath = "/cart";
        // Otherwise an error has occurred
        } else {
            session.setAttribute("errorMessage", "The URL is not a functioninng part "
                    + "of this website");
            response.sendRedirect(ERROR_URL);
            return;
        }

        if (sb.length() > 0) {
            Administrator admin = administratorFacade.getAdministratorById(1);

            // Create a new log to be sent in JMS message
            Log l = new Log();

            l.setComment(sb.toString());
            l.setAdminId(admin);

            //Send log as JMS Message
            try {
                sendJMSMessageToNewLogMessage(l);
            } catch (JMSException ex) {
                Logger.getLogger(AdminHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   
    // Log Management (via message beans)
    
    /*
     * Create a new JMS Message object using the message data
     *
     * @param session
     * @param messageData
     * @return The JMS message object
     * @throws JMSException
     */
    private Message createJMSMessageForjmsNewLogMessage(Session session, Object messageData) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject((Serializable) messageData);
        return message;
    }

    /*
     * Send a new JMS Message containing the object passed in as a parameter
     *
     * @param messageData
     * @throws JMSException
     */
    private void sendJMSMessageToNewLogMessage(Object messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = newLogMessageFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(newLogMessage);
            messageProducer.send(createJMSMessageForjmsNewLogMessage(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /*
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "A servlet to handle management of the shopping cart";
    }
}
