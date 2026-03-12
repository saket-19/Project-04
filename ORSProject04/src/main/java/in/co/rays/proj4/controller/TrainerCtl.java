package in.co.rays.proj4.controller;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TrainerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TrainerModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;
@WebServlet(name = "TrainerCtl", urlPatterns = { "/ctl/TrainerCtl" })
public class TrainerCtl extends BaseCtl{
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
	        } else if (!DataValidator.isName(request.getParameter("name"))) {
	            request.setAttribute("name", "Invalid Name");
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("specialization"))) {
	            request.setAttribute("specialization", PropertyReader.getValue("error.require", "Specialization"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("contactNumber"))) {
	            request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
	            pass = false;
	        } else if (!DataValidator.isPhoneNo(request.getParameter("contactNumber"))) {
	            request.setAttribute("contactNumber", "Invalid Contact Number");
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        TrainerBean bean = new TrainerBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setName(DataUtility.getString(request.getParameter("name")));
	        bean.setSpecialization(DataUtility.getString(request.getParameter("specialization")));
	        bean.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

	        populateDTO(bean, request);

	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        TrainerModel model = new TrainerModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (id > 0 || op != null) {

	            try {
	                TrainerBean bean = model.findByPK(id);
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

	        TrainerModel model = new TrainerModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        // 🔹 Save
	        if (OP_SAVE.equalsIgnoreCase(op)) {

	            TrainerBean bean = (TrainerBean) populateBean(request);

	            try {
	                long pk = model.add(bean);

	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Trainer added successfully", request);

	            } catch (DuplicateRecordException e) {

	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Trainer already exists", request);

	            } catch (ApplicationException e) {

	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        }
	        // 🔹 Update
	        else if (OP_UPDATE.equalsIgnoreCase(op)) {

	            TrainerBean bean = (TrainerBean) populateBean(request);

	            try {

	                if (id > 0) {
	                    model.update(bean);
	                }

	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Trainer updated successfully", request);

	            } catch (DuplicateRecordException e) {

	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Trainer already exists", request);

	            } catch (ApplicationException e) {

	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        }
	        // 🔹 Delete
	        else if (OP_DELETE.equalsIgnoreCase(op)) {

	            TrainerBean bean = (TrainerBean) populateBean(request);

	            try {

	                model.delete(bean);
	                ServletUtility.redirect(ORSView.TRAINER_LIST_CTL, request, response);
	                return;

	            } catch (ApplicationException e) {

	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        }
	        // 🔹 Cancel
	        else if (OP_CANCEL.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.TRAINER_LIST_CTL, request, response);
	            return;

	        }
	        // 🔹 Reset
	        else if (OP_RESET.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.TRAINER_CTL, request, response);
	            return;
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    @Override
	    protected String getView() {
	        return ORSView.TRAINER_VIEW;
	    }
	}


