
package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * <p>
 * <b>MyProfileCtl</b> manages the profile of the currently logged-in user.
 * It allows the user to:
 * </p>
 * <ul>
 *     <li>View their profile information</li>
 *     <li>Update personal details</li>
 *     <li>Navigate to the Change Password page</li>
 * </ul>
 *
 * <p>
 * This controller uses {@link UserModel} to interact with the database and
 * performs server-side validation before updating profile data.
 * </p>
 *
 * <p>
 * Accessible via URL: <b>/ctl/MyProfileCtl</b>
 * </p>
 *
 * @author saket
 */
@WebServlet(name = "MyProfileCtl", urlPatterns = { "/ctl/MyProfileCtl" })
public class MyProfileCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(MyProfileCtl.class);
	

    /** Operation constant for Change Password button. */
    public static final String OP_CHANGE_MY_PASSWORD = "Change Password";

    /**
     * Validates the user profile fields before allowing update.
     *
     * @param request the incoming HTTP request
     * @return true if all inputs are valid, otherwise false
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	
    	log.debug("MyProfileCtl Method validate Started");

        boolean pass = true;
        String op = DataUtility.getString(request.getParameter("operation"));

        // Skip validations during Change Password operation
        if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op) || op == null) {
            return pass;
        }

        // First Name validation
        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName",
                    PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
        }

        // Last Name validation
        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName",
                    PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
        }

        // Gender validation
        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender",
                    PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }

        // Mobile Number validation
        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo",
                    PropertyReader.getValue("error.require", "Mobile No"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
        }

        // Date of Birth validation
        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob",
                    PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        }

        log.debug("MyProfileCtl Method validate ended");
        
        return pass;
    }

    /**
     * Populates a {@link UserBean} with values from the request.
     *
     * @param request the incoming HTTP request
     * @return populated UserBean instance
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("MyProfileCtl Method populate Started");

        UserBean bean = new UserBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));

        populateDTO(bean, request);
        log.debug("MyProfileCtl Method populate ended");
        return bean;
    }

    /**
     * Displays the logged-in user's profile data.
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("MyProfileCtl Method doGet Started");

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();

        UserModel model = new UserModel();

        try {
            if (id > 0) {
                UserBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
            ServletUtility.handleException(e, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        
        log.debug("MyProfileCtl Method doGet ended");
    }

    /**
     * Handles Profile Update and Change Password operations.
     *
     * @param request  incoming HTTP request
     * @param response outgoing HTTP response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("MyProfileCtl Method doPost Started");

        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("user");
        long id = user.getId();
        String op = DataUtility.getString(request.getParameter("operation"));

        UserModel model = new UserModel();

        // Save/Update Profile
        if (OP_SAVE.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(request);

            try {
                if (id > 0) {
                    user.setFirstName(bean.getFirstName());
                    user.setLastName(bean.getLastName());
                    user.setGender(bean.getGender());
                    user.setMobileNo(bean.getMobileNo());
                    user.setDob(bean.getDob());
                    model.update(user);
                }

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Profile has been updated successfully.", request);

            } catch (DuplicateRecordException e) {
            	log.error(e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login ID already exists", request);

            } catch (ApplicationException e) {
            	log.error(e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
            
            
        }

        // Change Password operation
        else if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("MyProfileCtl Method doPost ended");
    }

    /**
     * Returns the profile view JSP.
     *
     * @return path of MY_PROFILE_VIEW
     */
    @Override
    protected String getView() {
        return ORSView.MY_PROFILE_VIEW;
    }
}
