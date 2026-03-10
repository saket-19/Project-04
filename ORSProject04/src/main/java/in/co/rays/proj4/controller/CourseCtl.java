package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * Controller to handle Course operations such as add, update and display.
 * This controller extends BaseCtl and manages form validation, bean population,
 * and request handling for GET and POST.
 *
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "CourseCtl", urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(CourseCtl.class);

    /**
     * Validates input fields of Course form.
     *
     * @param request HTTP request object
     * @return true if all fields are valid, otherwise false
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

    	log.debug("CourseCtl Method validate Started");
    	
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("duration"))) {
            request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }

        log.debug("CourseCtl Method validate ended");
        
        return pass;
    }

    /**
     * Populates CourseBean with values from request parameters.
     *
     * @param request HTTP request object
     * @return populated CourseBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

    	log.debug("CourseCtl Method populatebean Started");
    	
        CourseBean bean = new CourseBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));

        populateDTO(bean, request);

        log.debug("CourseCtl Method populatebean ended");
        return bean;
        
    }

    /**
     * Handles HTTP GET request to load Course data for editing or display.
     *
     * @param request  HTTP request object
     * @param response HTTP response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	log.debug("CourseCtl Method doGet Started");
        long id = DataUtility.getLong(request.getParameter("id"));
        CourseModel model = new CourseModel();

        if (id > 0) {
            try {
                CourseBean bean = model.FindByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
            	log.error(e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        	

        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl Method doGet ended");
    }

    /**
     * Handles HTTP POST request for Save, Update, Reset, and Cancel operations.
     *
     * @param request  HTTP request object
     * @param response HTTP response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	log.debug("CourseCtl Method doPost started");
        String op = DataUtility.getString(request.getParameter("operation"));
        CourseModel model = new CourseModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            CourseBean bean = (CourseBean) populateBean(request);
            try {
                long pk = model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("User added successfully", request);
            } catch (DuplicateRecordException e) {
            	log.error(e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
            } catch (ApplicationException e) {
            	log.error(e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            CourseBean bean = (CourseBean) populateBean(request);
            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("User updated successfully", request);
            } catch (DuplicateRecordException e) {
            	log.error(e);
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Login Id already exists", request);
            } catch (ApplicationException e) {
            	log.error(e);
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("CourseCtl Method doPost ended");
    }

    /**
     * Returns the view page for Course form.
     *
     * @return JSP view for Course
     */
    @Override
    protected String getView() {
        return ORSView.COURSE_VIEW;
    }

}