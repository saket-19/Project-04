
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MeetingBean;
import in.co.rays.proj4.model.MeetingModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;
@WebServlet("/MeetingCtl")
public class MeetingCtl extends BaseCtl{
	

	    public static final String OP_SAVE = "Save";
	    public static final String OP_UPDATE = "Update";
	    public static final String OP_DELETE = "Delete";
	    public static final String OP_RESET = "Reset";
	    public static final String OP_CANCEL = "Cancel";

	    // ------------------ Validate ------------------
	    protected boolean validate(HttpServletRequest request) {

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("code"))) {
	            request.setAttribute("code",
	                    PropertyReader.getValue("error.require", "Code"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("title"))) {
	            request.setAttribute("title",
	                    PropertyReader.getValue("error.require", "Title"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("time"))) {
	            request.setAttribute("time",
	                    PropertyReader.getValue("error.require", "Time"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("location"))) {
	            request.setAttribute("location",
	                    PropertyReader.getValue("error.require", "Location"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("status"))) {
	            request.setAttribute("status",
	                    PropertyReader.getValue("error.require", "Status"));
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        MeetingBean bean = new MeetingBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setTitle(DataUtility.getString(request.getParameter("title")));
	        bean.setLocation(DataUtility.getString(request.getParameter("location")));
	        bean.setStatus(DataUtility.getString(request.getParameter("status")));

	        String time = request.getParameter("time");
	        if (time != null && !time.isEmpty()) {
	            bean.setTime(java.time.LocalDateTime.parse(time));
	        }

	       
	        populateDTO(bean, request);

	        return bean;
	    }
	    

	    // ------------------ doGet ------------------
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        long id = DataUtility.getLong(request.getParameter("id"));

	        MeetingModel model = new MeetingModel();

	        if (id > 0) {
	            try {
	                MeetingBean bean = model.findByPK(id);
	                ServletUtility.setBean(bean, request);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        ServletUtility.forward(ORSView.MEETING_VIEW, request, response);
	    }

	    // ------------------ doPost ------------------
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        MeetingModel model = new MeetingModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        // -------- SAVE / UPDATE --------
	        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

	            MeetingBean bean = (MeetingBean) populateBean(request);

	            if (!validate(request)) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.forward(ORSView.MEETING_VIEW, request, response);
	                return;
	            }

	            try {

	                if (id > 0) {
	                    model.update(bean);
	                    ServletUtility.setSuccessMessage("Meeting updated successfully", request);
	                } else {
	                    long pk = model.add(bean);
	                    ServletUtility.setSuccessMessage("Meeting added successfully", request);
	                }

	                ServletUtility.forward(ORSView.MEETING_VIEW, request, response);

	            } catch (Exception e) {
	                e.printStackTrace();
	                ServletUtility.setErrorMessage("Database error", request);
	                ServletUtility.forward(ORSView.MEETING_VIEW, request, response);
	            }
	        }

	        // -------- DELETE --------
	        else if (OP_DELETE.equalsIgnoreCase(op)) {

	            MeetingBean bean = (MeetingBean) populateBean(request);

	            try {
	                model.delete(bean);
	                ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
	                return;
	            } catch (Exception e) {
	                e.printStackTrace();
	                ServletUtility.setErrorMessage("Error deleting record", request);
	                ServletUtility.forward(ORSView.MEETING_VIEW, request, response);
	            }
	        }

	        // -------- RESET --------
	        else if (OP_RESET.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
	            return;
	        }

	        // -------- CANCEL --------
	        else if (OP_CANCEL.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
	            return;
	        }
	    }

	    @Override
	    protected String getView() {
	        return ORSView.MEETING_VIEW;
	    }
	}
	


