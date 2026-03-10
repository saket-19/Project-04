package in.co.rays.proj4.controller;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.NotificationBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.NotificationModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;


@WebServlet("/NotificationCtl")
public class NotificationCtl extends BaseCtl {
	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("code"))) {
	            request.setAttribute("code", PropertyReader.getValue("error.require", "Code"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("message"))) {
	            request.setAttribute("message", PropertyReader.getValue("error.require", "Message"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("sentTo"))) {
	            request.setAttribute("sentTo", PropertyReader.getValue("error.require", "Sent To"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("status"))) {
	            request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
	            pass = false;
	        }

	        return pass;
	    }

	    /**
	     * Populate Bean
	     */
	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        NotificationBean bean = new NotificationBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setMessage(DataUtility.getString(request.getParameter("message")));
	        bean.setSentTo(DataUtility.getString(request.getParameter("sentTo")));
	        bean.setStatus(DataUtility.getString(request.getParameter("status")));

	        // Convert String → LocalDateTime
	        String sentTimeStr = request.getParameter("sentTime");
	        if (sentTimeStr != null && sentTimeStr.length() > 0) {
	            bean.setSentTime(LocalDateTime.parse(sentTimeStr));
	        }

	        populateDTO(bean, request);

	        return bean;
	    }

	    /**
	     * GET Method
	     */
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        long id = DataUtility.getLong(request.getParameter("id"));
	        System.out.println("id is greater than 1: "+id);

	        NotificationModel model = new NotificationModel();

	        if (id > 0) {
	            try {
	                NotificationBean bean = model.findByPK(id);
	                ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
	                e.printStackTrace();
	            }
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    /**
	     * POST Method
	     */
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        NotificationModel model = new NotificationModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (OP_SAVE.equalsIgnoreCase(op)) {

	            NotificationBean bean = (NotificationBean) populateBean(request);

	            try {
	                long pk = model.add(bean);
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Notification added successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Notification already exists", request);

	            } catch (ApplicationException e) {
	                e.printStackTrace();
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

	            NotificationBean bean = (NotificationBean) populateBean(request);

	            try {
	                if (id > 0) {
	                    model.update(bean);
	                }

	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Notification updated successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Notification already exists", request);

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_DELETE.equalsIgnoreCase(op)) {

	            NotificationBean bean = (NotificationBean) populateBean(request);

	            try {
	                model.delete(bean);
	                ServletUtility.redirect(ORSView.NOTIFICATION_LIST_CTL, request, response);
	                return;

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.NOTIFICATION_LIST_CTL, request, response);
	            return;

	        } else if (OP_RESET.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.NOTIFICATION_CTL, request, response);
	            return;
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    /**
	     * View
	     */
	    @Override
	    protected String getView() {
	        return ORSView.NOTIFICATION_VIEW;
	    }
	}


