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

/*
 * Servlet to handle admin tasks
 *
 * @author  Rory Burns, 10108696 
 *          Vincent O'Brien, 10111255 
 *          Jamie Chambers, 10116532 
 *          Ger Lynch, 10115293
 */
@WebServlet(name = "AdminHandler", urlPatterns = {"/admin/*"})
public class AdminHandler extends HttpServlet {

    @EJB
    private LogFacade logFacade;
    @EJB
    private AdministratorFacade administratorFacade;
    @EJB
    private ProductFacade productFacade;
    @Resource(mappedName = "jms/NewLogMessage")
    private Queue newLogMessage;
    @Resource(mappedName = "jms/NewLogMessageFactory")
    private ConnectionFactory newLogMessageFactory;
    private static final String ERROR_URL = "../error";

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getPathInfo();
        //String userPath = request.getServletPath();
        HttpSession session = request.getSession();

        //Ensure admin is logged in, else redirect
        if ((session.getAttribute("admin")) instanceof Administrator) {
            Administrator administrator = (Administrator) session.getAttribute("admin");
            if (!administratorFacade.validate(administrator)) {
                session.invalidate();
                response.sendRedirect("../signin");
                return;
            }
        } else {
            session.invalidate();
            response.sendRedirect("../signin");
            return;
        }

        if (userPath == null) {
            userPath = "/";
        }

        //-----------------------------------------------------------------------
        // If main admin page requested (i.e. viewing inventory)
        //-----------------------------------------------------------------------
        if (userPath.equals("/") || userPath.equals("")) {
            userPath = "/editproducts";

            //-----------------------------------------------------------------------
            // If log page requested (i.e. viewing log)
            //-----------------------------------------------------------------------
        } else if (userPath.equals("/log")) {
            request.setAttribute("logs", logFacade.findAll());
            userPath = "/viewlogs";

            //-----------------------------------------------------------------------
            // Otherwise an error has occurred
            //-----------------------------------------------------------------------
        } else {
            session.setAttribute("errorMessage", "The URL is not a functioninng part "
                    + "of this website" + userPath);
            response.sendRedirect(ERROR_URL);
            return;
        }

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view/admin" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getPathInfo();
        HttpSession session = request.getSession();
        StringBuilder sb = new StringBuilder();

        Administrator administrator = null;

        //Ensure admin is logged in, else redirect
        if ((session.getAttribute("admin")) instanceof Administrator) {
            administrator = (Administrator) session.getAttribute("admin");;
            if (!administratorFacade.validate(administrator)) {
                session.invalidate();
                response.sendRedirect("../signin");
            }
        } else {
            session.invalidate();
            response.sendRedirect("../signin");
        }

        //-----------------------------------------------------------------------
        // If editproducts action is called (i.e. editing inventory)
        //-----------------------------------------------------------------------
        if (userPath.equals("/editproducts")) {
            Integer id_n, stock_n;
            Float price_n;
            int row = -1;
            Boolean doRemove = null;

            // Check if update or delete operation called and get relevant item
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

            //Ensure a button was actually pressed
            if (row == -1) {
                //Should never get here
                session.setAttribute("errorMessage", "An unknown error occured");
                response.sendRedirect(ERROR_URL);
                return;
            }

            if (doRemove != null && !doRemove) {

                //Get request parameter(s)
                String id = request.getParameterValues("id")[row];
                String title = request.getParameterValues("title")[row];
                String description = request.getParameterValues("description")[row];
                String price = request.getParameterValues("price")[row];
                String stock = request.getParameterValues("stock")[row];

                //Ensure form is valid
                if (id == null || title == null || description == null
                        || price == null || stock == null) {
                    session.setAttribute("errorMessage", "Please complete the form");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Ensure form is valid
                if (id.isEmpty() || title.isEmpty() || description.isEmpty()
                        || price.isEmpty() || stock.isEmpty()) {
                    session.setAttribute("errorMessage", "Please complete the form");
                    response.sendRedirect(ERROR_URL);
                    return;
                }

                //Parse numerical values 
                try {
                    id_n = Integer.parseInt(id);
                    stock_n = Integer.parseInt(stock);
                    price_n = Float.parseFloat(price);
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

                //Process update
                productFacade.updateProduct(id_n, title, description, price_n, stock_n);

                //Save a message to be added to the log
                sb.append("Modified (").append(id).append(",")
                        .append(title).append(",")
                        .append(description).append(",")
                        .append(price).append(",")
                        .append(stock).append(")");

            } else if (doRemove != null && doRemove) {

                //Get request parameter(s)
                String id = request.getParameterValues("id")[row];

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

                //Process delete
                productFacade.removeProduct(id_n);

                //Save a message to be added to the log
                sb.append("REMOVED (").append(id).append(")");

            }

            //Update the session attribute
            getServletContext().setAttribute("products", productFacade.findAll());

            //-----------------------------------------------------------------------
            // If addproduct action is called (i.e. adding a new product to inventory)
            //-----------------------------------------------------------------------
        } else if (userPath.equals("/addproduct")) {
            Integer stock_n;
            Float price_n;

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String price = request.getParameter("price");
            String stock = request.getParameter("stock");

            //Ensure form is valid
            if (title == null || description == null || price == null || stock == null) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            //Ensure form is valid
            if (title.isEmpty() || description.isEmpty() || price.isEmpty()
                    || stock.isEmpty()) {
                session.setAttribute("errorMessage", "Please complete the form");
                response.sendRedirect(ERROR_URL);
                return;
            }

            try {
                stock_n = Integer.parseInt(stock);
                price_n = Float.parseFloat(price);
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Please enter numerical values "
                        + "where required");
                response.sendRedirect(ERROR_URL);
                return;
            }

            productFacade.addProduct(title, description, price_n, stock_n);

            sb.append("ADDED (").append(title).append(",")
                    .append(description).append(",")
                    .append(price.toString()).append(",")
                    .append(stock.toString()).append(")");

            //update the session attribute
            getServletContext().setAttribute("products", productFacade.findAll());

            //-----------------------------------------------------------------------
            // Otherwise an error has occurred
            //-----------------------------------------------------------------------
        } else if (userPath.equals("/log")) {
            request.setAttribute("logs", logFacade.findAll());
            userPath = "/viewlogs";

            String url = "/WEB-INF/view/admin" + userPath + ".jsp";

            try {
                request.getRequestDispatcher(url).forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            session.setAttribute("errorMessage", "The URL is not a functioninng part "
                    + "of this website" + userPath);
            response.sendRedirect(ERROR_URL);
            return;
        }

        // Create a new log to be sent in JMS message
        Log l = new Log();

        l.setComment(sb.toString());
        l.setAdminId(administrator);

        //Send log as JMS Message
        try {
            sendJMSMessageToNewLogMessage(l);
        } catch (JMSException ex) {
            Logger.getLogger(AdminHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Forward back to admin page
        response.sendRedirect("../admin");
    }

      //-------------------------------------------------------------------------
    // Log Management (via message beans)
    //-------------------------------------------------------------------------
    /**
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

    /**
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "A servlet to handle admin tasks";
    }
}
