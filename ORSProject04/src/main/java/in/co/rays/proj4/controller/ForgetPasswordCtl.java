
package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.RecordNotFoundException;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * Forget Password Controller.
 * Handles validation, sending password to user's email, and rendering the view.
 *
 * @author Saket
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

    /**
     * Validates login (email) field.
     * Ensures it is not null and is in proper email format.
     *
     * @param request HTTP request
     * @return true if validation passes, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	
    	log.debug("ForgetPasswordCtl Method validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
            pass = false;
        }
        log.debug("ForgetPasswordCtl Method validate ended");


        return pass;
    }

    /**
     * Populates UserBean using login (email) parameter from request.
     *
     * @param request HTTP request
     * @return UserBean with login field populated
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.debug("ForgetPasswordCtl Method populate started");


        UserBean bean = new UserBean();

        bean.setLogin(DataUtility.getString(request.getParameter("login")));

        log.debug("ForgetPasswordCtl Method populate ended");

        return bean;
        
    }

    /**
     * Displays Forget Password view on GET request.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Handles Forget Password submission.
     * Calls UserModel to send password to user's email.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	log.debug("ForgetPasswordCtl Method doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));

        UserBean bean = (UserBean) populateBean(request);
        

        UserModel model = new UserModel();

        if (OP_GO.equalsIgnoreCase(op)) {
            try {
                boolean flag = model.forgetPassword(bean.getLogin());
                if (flag) {
                    ServletUtility.setSuccessMessage("Password has been sent to your email id", request);
                }
            } catch (RecordNotFoundException e) {
                ServletUtility.setErrorMessage(e.getMessage(), request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.setErrorMessage("Please check your internet connection..!!", request);
            }
            ServletUtility.forward(getView(), request, response);
        }
        log.debug("ForgetPasswordCtl Method doPost ended");

    }

    /**
     * Returns the JSP view for Forget Password screen.
     *
     * @return FORGET_PASSWORD_VIEW constant
     */
    @Override
    protected String getView() {
        return ORSView.FORGET_PASSWORD_VIEW;
    }
}
