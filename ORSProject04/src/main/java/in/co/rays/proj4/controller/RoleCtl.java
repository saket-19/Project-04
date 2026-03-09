package in.co.rays.proj4.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * RoleCtl class is a controller to handle Role operations such as add, update,
 * and reset. It interacts with RoleModel for database operations and
 * communicates with JSP views for displaying the UI.
 * <p>
 * URL pattern for this controller is "/ctl/RoleCtl".
 * </p>
 * 
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "RoleCtl", urlPatterns = { "/ctl/RoleCtl" })
public class RoleCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(RoleCtl.class);
	

    /**
     * Validates input fields for Role form.
     * <p>
     * Checks if role name and description are not null.
     * </p>
     * 
     * @param request HttpServletRequest object containing client request data.
     * @return true if all fields are valid, false otherwise.
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	log.debug("RoleCtl Method validate Started");
    	
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Role Name"));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("desc"))) {
            request.setAttribute("desc", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }
    	log.debug("RoleCtl Method validate ended");
        return pass;
    }

    /**
     * Populates RoleBean from HTTP request parameters.
     * 
     * @param request HttpServletRequest object
     * @return BaseBean populated with request data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.debug("RoleCtl Method populate Started");
        RoleBean bean = new RoleBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDescription(DataUtility.getString(request.getParameter("desc")));

        populateDTO(bean, request);
    	log.debug("RoleCtl Method validate ended");
        return bean;
    }

    /**
     * Handles HTTP GET request.
     * <p>
     * Loads Role data for editing if an id is provided.
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
    	
    	log.debug("RoleCtl Method doGet Started");

        long id = DataUtility.getLong(request.getParameter("id"));
        RoleModel model = new RoleModel();

        if (id > 0) {
            try {
                RoleBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
    	log.debug("RoleCtl Method doGet ended");
    }

    /**
     * Handles HTTP POST request.
     * <p>
     * Performs operations like save, update, cancel, and reset based on the
     * operation parameter.
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
    	
    	log.debug("RoleCtl Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        RoleModel model = new RoleModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {
            RoleBean bean = (RoleBean) populateBean(request);
            try {
                try {
					long pk = model.add(bean);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
            RoleBean bean = (RoleBean) populateBean(request);
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
            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
    	log.debug("RoleCtl Method doPost ended");
    }

    /**
     * Returns the view page for Role module.
     * 
     * @return JSP view path
     */
    @Override
    protected String getView() {
        return ORSView.ROLE_VIEW;
    }
}