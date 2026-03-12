package in.co.rays.proj4.controller;
import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TrainingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TrainingModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet(name = "TrainingCtl", urlPatterns = { "/ctl/TrainingCtl" })
public class TrainingCtl extends BaseCtl {
	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("code"))) {
	            request.setAttribute("code", PropertyReader.getValue("error.require", "Code"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("name"))) {
	            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("trainerName"))) {
	            request.setAttribute("trainerName", PropertyReader.getValue("error.require", "Trainer Name"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("date"))) {
	            request.setAttribute("date", PropertyReader.getValue("error.require", "Training Date"));
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        TrainingBean bean = new TrainingBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setName(DataUtility.getString(request.getParameter("name")));
	        bean.setTrainerName(DataUtility.getString(request.getParameter("trainerName")));

	        String dateStr = request.getParameter("date");
	        if (dateStr != null && !dateStr.isEmpty()) {
	            bean.setDate(LocalDate.parse(dateStr));
	        }

	        populateDTO(bean, request);

	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        TrainingModel model = new TrainingModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (id > 0 || op != null) {
	            TrainingBean bean;
	            try {
	                bean = model.findByPK(id);
	                ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        TrainingModel model = new TrainingModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (OP_SAVE.equalsIgnoreCase(op)) {

	            TrainingBean bean = (TrainingBean) populateBean(request);

	            try {
	                long pk = model.add(bean);
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Training added successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Training code already exists", request);

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

	            TrainingBean bean = (TrainingBean) populateBean(request);

	            try {
	                if (id > 0) {
	                    model.update(bean);
	                }
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Training updated successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Training code already exists", request);

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_DELETE.equalsIgnoreCase(op)) {

	            TrainingBean bean = (TrainingBean) populateBean(request);

	            try {
	                model.delete(bean);
	                ServletUtility.redirect(ORSView.TRAINING_LIST_CTL, request, response);
	                return;

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.TRAINING_LIST_CTL, request, response);
	            return;

	        } else if (OP_RESET.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.TRAINING_CTL, request, response);
	            return;
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    @Override
	    protected String getView() {
	        return ORSView.TRAINING_VIEW;
	    }
	}


