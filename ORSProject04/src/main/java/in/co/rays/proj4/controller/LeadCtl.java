package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.LeadBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.LeadModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet(name = "LeadCtl", urlPatterns = { "/ctl/LeadCtl" })
public class LeadCtl extends BaseCtl {

    // -------------------- Validation --------------------
    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("contactName"))) {
            request.setAttribute("contactName",
                    PropertyReader.getValue("error.require", "Contact Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("contactName"))) {
            request.setAttribute("contactName", "Invalid Contact Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile is required");
            pass = false;

        } else if (!request.getParameter("mobile").matches("^[6-9][0-9]{9}$")) {
            request.setAttribute("mobile", "Invalid Mobile Number");
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob",
                    PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        return pass;
    }

    // -------------------- Populate Bean --------------------
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        LeadBean bean = new LeadBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setContactName(DataUtility.getString(request.getParameter("contactName")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setStatus(DataUtility.getString(request.getParameter("status")));

        populateDTO(bean, request);

        return bean;
    }

    // -------------------- GET --------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));

        LeadModel model = new LeadModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0 || op != null) {

            try {
                LeadBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);

            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);
    }

    // -------------------- POST --------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));

        LeadModel model = new LeadModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        // -------------------- SAVE --------------------
        if (OP_SAVE.equalsIgnoreCase(op)) {

            LeadBean bean = (LeadBean) populateBean(request);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Lead added successfully", request);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Lead already exists", request);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        // -------------------- UPDATE --------------------
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            LeadBean bean = (LeadBean) populateBean(request);

            try {
                if (id > 0) {
                    model.update(bean);
                }

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Lead updated successfully", request);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Lead already exists", request);

            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }

        // -------------------- DELETE --------------------
        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            LeadBean bean = (LeadBean) populateBean(request);

            try {
                model.delete(bean);
                ServletUtility.redirect(ORSView.LEAD_LIST_CTL, request, response);
                return;

            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }

        // -------------------- CANCEL --------------------
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LEAD_LIST_CTL, request, response);
            return;

        // -------------------- RESET --------------------
        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LEAD_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    // -------------------- VIEW --------------------
    @Override
    protected String getView() {
        return ORSView.LEAD_VIEW;
    }
}