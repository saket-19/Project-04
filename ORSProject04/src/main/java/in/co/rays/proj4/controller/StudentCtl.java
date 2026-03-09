
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * StudentCtl is a controller class to handle operations related to Student.
 * <p>
 * It handles preload of college list, validation, adding, updating, and
 * displaying student information.
 * </p>
 * URL pattern for this controller is "/ctl/StudentCtl".
 * 
 * It interacts with StudentModel and CollegeModel for database operations and
 * forwards data to StudentView JSP page.
 * 
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "StudentCtl", urlPatterns = { "/ctl/StudentCtl" })
public class StudentCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(StudentCtl.class);

    /**
     * Preloads data required for Student form.
     * <p>
     * Loads the list of colleges from CollegeModel and sets it in request
     * attribute "collegelist".
     * </p>
     * 
     * @param request HttpServletRequest object
     */
	@Override
	protected void preload(HttpServletRequest request) {

	    CollegeModel collegemodel = new CollegeModel();

	    try {
	        List<CollegeBean> collegeList = collegemodel.list();
	        request.setAttribute("collegeList", collegeList);

	    } catch (ApplicationException e) {
	        e.printStackTrace();
	    }
	}

    /**
     * Validates the student form input data.
     * <p>
     * Checks for null values, correct email format, valid date, valid name, and
     * correct mobile number format.
     * </p>
     * 
     * @param request HttpServletRequest object
     * @return true if validation passes, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	
    	log.debug("StudentCtl Method validate Started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("firstName"))) {
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("firstName"))) {
            request.setAttribute("firstName", "Invalid First Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("lastName"))) {
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("lastName"))) {
            request.setAttribute("lastName", "Invalid Last Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("email"))) {
            request.setAttribute("email", PropertyReader.getValue("error.email", "Email "));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("gender"))) {
            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("college"))) {
            request.setAttribute("college", PropertyReader.getValue("error.require", "college"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
            request.setAttribute("mobileNo", "Invalid Mobile No");
            pass = false;
        }

        log.debug("StudentCtl Method validate ended");
        return pass;
    }

    /**
     * Populates a StudentBean from HTTP request parameters.
     * 
     * @param request HttpServletRequest object
     * @return BaseBean populated with Student data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("StudentCtl Method populate Started");

        StudentBean bean = new StudentBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setGender(DataUtility.getString(request.getParameter("gender")));
        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));
        bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));

        populateDTO(bean, request);
        
        log.debug("StudentCtl Method populate ended ");
        return bean;

    }

    /**
     * Handles HTTP GET request.
     * <p>
     * Fetches student record by primary key if ID is present and forwards to view.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.debug("StudentCtl Method doget started ");

        long id = DataUtility.getLong(request.getParameter("id"));

        StudentModel model = new StudentModel();

        if (id > 0) {
            try {
                StudentBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("StudentCtl Method doget ended ");
    }

    /**
     * Handles HTTP POST request.
     * <p>
     * Performs operations like save, update, cancel, and reset.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.debug("StudentCtl Method doPost started ");

        String op = DataUtility.getString(request.getParameter("operation"));

        StudentModel model = new StudentModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {
            StudentBean bean = (StudentBean) populateBean(request);
            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("User added successfully", request);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            StudentBean bean = (StudentBean) populateBean(request);
            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("User updated successfully", request);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("StudentCtl Method doPost ended ");
    }

    /**
     * Returns the view page for Student module.
     * 
     * @return JSP view path for Student
     */
    @Override
    protected String getView() {
        return ORSView.STUDENT_VIEW;
    }
}
