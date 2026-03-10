package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.ComplaintBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.ComplaintModel;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet("/ComplaintCtl")
public class ComplaintCtl extends BaseCtl{
	@Override
	protected boolean validate(HttpServletRequest request) {

	    boolean pass = true;

	    // Title
	    if (DataValidator.isNull(request.getParameter("title"))) {
	        request.setAttribute("title",
	                PropertyReader.getValue("error.require", "Title"));
	        pass = false;
	    }

	    // Description
	    if (DataValidator.isNull(request.getParameter("description"))) {
	        request.setAttribute("description",
	                PropertyReader.getValue("error.require", "Description"));
	        pass = false;
	    }

	    // Type
	    if (DataValidator.isNull(request.getParameter("type"))) {
	        request.setAttribute("type",
	                PropertyReader.getValue("error.require", "Type"));
	        pass = false;
	    }

	    // Raised By
	    if (DataValidator.isNull(request.getParameter("raisedBy"))) {
	        request.setAttribute("raisedBy",
	                PropertyReader.getValue("error.require", "Raised By"));
	        pass = false;
	    }

	    // Assigned To
	    if (DataValidator.isNull(request.getParameter("assignedTo"))) {
	        request.setAttribute("assignedTo",
	                PropertyReader.getValue("error.require", "Assigned To"));
	        pass = false;
	    }

	    // Priority
	    if (DataValidator.isNull(request.getParameter("priority"))) {
	        request.setAttribute("priority",
	                PropertyReader.getValue("error.require", "Priority"));
	        pass = false;
	    }

	    // Status
	    if (DataValidator.isNull(request.getParameter("status"))) {
	        request.setAttribute("status",
	                PropertyReader.getValue("error.require", "Status"));
	        pass = false;
	    }

	    return pass;
	}
	@Override
	protected void preload(HttpServletRequest request) {

	    HashMap<String, String> priorityMap = new HashMap<>();
	    priorityMap.put("High", "High");
	    priorityMap.put("Medium", "Medium");
	    priorityMap.put("Low", "Low");

	    HashMap<String, String> statusMap = new HashMap<>();
	    statusMap.put("Open", "Open");
	    statusMap.put("In Progress", "In Progress");
	    statusMap.put("Closed", "Closed");

	    request.setAttribute("priorityMap", priorityMap);
	    request.setAttribute("statusMap", statusMap);
	}
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

	    ComplaintBean bean = new ComplaintBean();

	    bean.setId(DataUtility.getLong(request.getParameter("id")));
	    bean.setTitle(DataUtility.getString(request.getParameter("title")));
	    bean.setDescription(DataUtility.getString(request.getParameter("description")));
	    bean.setType(DataUtility.getString(request.getParameter("type")));
	    bean.setRaisedBy(DataUtility.getString(request.getParameter("raisedBy")));
	    bean.setAssignedTo(DataUtility.getString(request.getParameter("assignedTo")));
	    bean.setPriority(DataUtility.getString(request.getParameter("priority")));
	    bean.setStatus(DataUtility.getString(request.getParameter("status")));

	    // Common fields (createdBy, modifiedBy, datetime)
	    populateDTO(bean, request);

	    return bean;
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String op = DataUtility.getString(request.getParameter("operation"));

	    // Get model
	    ComplaintModel model = new ComplaintModel();

	    long id = DataUtility.getLong(request.getParameter("id"));

	    if (id > 0 || op != null) {
	        ComplaintBean bean;
	        try {
	            bean = model.findByPK(id);
	            ServletUtility.setBean(bean, request);
	        } catch (ApplicationException e) {
	            ServletUtility.handleException(e, request, response);
	            return;
	        }
	    }

	    // Forward to view
	    ServletUtility.forward(getView(), request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String op = DataUtility.getString(request.getParameter("operation"));

	    ComplaintModel model = new ComplaintModel();

	    long id = DataUtility.getLong(request.getParameter("id"));

	    // SAVE
	    if (OP_SAVE.equalsIgnoreCase(op)) {

	        ComplaintBean bean = (ComplaintBean) populateBean(request);

	        try {
	            long pk = model.add(bean);
	            ServletUtility.setBean(bean, request);
	            ServletUtility.setSuccessMessage("Complaint added successfully", request);

	        } catch (DuplicateRecordException e) {
	            ServletUtility.setBean(bean, request);
	            ServletUtility.setErrorMessage("Complaint already exists", request);

	        } catch (ApplicationException e) {
	            e.printStackTrace();
	            ServletUtility.handleException(e, request, response);
	            return;
	        }
	    }

	    // UPDATE
	    else if (OP_UPDATE.equalsIgnoreCase(op)) {

	        ComplaintBean bean = (ComplaintBean) populateBean(request);

	        try {
	            if (id > 0) {
	                model.update(bean);
	            }
	            ServletUtility.setBean(bean, request);
	            ServletUtility.setSuccessMessage("Complaint updated successfully", request);

	        } catch (ApplicationException e) {
	            ServletUtility.handleException(e, request, response);
	            return;

	        } catch (DuplicateRecordException e) {
	            ServletUtility.setBean(bean, request);
	            ServletUtility.setErrorMessage("Complaint already exists", request);
	        }
	    }

	    // DELETE
	    else if (OP_DELETE.equalsIgnoreCase(op)) {

	        ComplaintBean bean = (ComplaintBean) populateBean(request);

	        try {
	            model.delete(bean);
	            ServletUtility.redirect(ORSView.COMPLAINT_LIST_CTL, request, response);
	            return;

	        } catch (ApplicationException e) {
	            ServletUtility.handleException(e, request, response);
	            return;
	        }
	    }

	    // CANCEL
	    else if (OP_CANCEL.equalsIgnoreCase(op)) {

	        ServletUtility.redirect(ORSView.COMPLAINT_LIST_CTL, request, response);
	        return;
	    }

	    // RESET
	    else if (OP_RESET.equalsIgnoreCase(op)) {

	        ServletUtility.redirect(ORSView.COMPLAINT_CTL, request, response);
	        return;
	    }

	    // Forward
	    ServletUtility.forward(getView(), request, response);
	}
	@Override
	protected String getView() {
		
		return ORSView.COMPLAINT_VIEW;
	}
	

}
