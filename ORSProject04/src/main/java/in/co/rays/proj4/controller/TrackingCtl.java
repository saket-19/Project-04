package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TrackingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TrackingModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet("/TrackingCtl")
public class TrackingCtl extends BaseCtl {

    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("number"))) {
            request.setAttribute("number",
                    PropertyReader.getValue("error.require", "Tracking Number"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("currentLocation"))) {
            request.setAttribute("currentLocation",
                    PropertyReader.getValue("error.require", "Current Location"));
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

        TrackingBean bean = new TrackingBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setNumber(DataUtility.getString(request.getParameter("number")));
        bean.setCurrentLocation(
                DataUtility.getString(request.getParameter("currentLocation")));
        bean.setStatus(DataUtility.getString(request.getParameter("status")));

        populateDTO(bean, request);

        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));

        TrackingModel model = new TrackingModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0 || op != null) {

            TrackingBean bean;
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
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String op = DataUtility.getString(request.getParameter("operation"));

        TrackingModel model = new TrackingModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            TrackingBean bean = (TrackingBean) populateBean(request);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage(
                        "Tracking added successfully", request);

            } catch (DuplicateRecordException e) {

                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage(
                        "Tracking Number already exists", request);

            } catch (ApplicationException e) {

                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            TrackingBean bean = (TrackingBean) populateBean(request);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage(
                        "Tracking updated successfully", request);

            } catch (DuplicateRecordException e) {

                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage(
                        "Tracking Number already exists", request);

            } catch (ApplicationException e) {

                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            TrackingBean bean = (TrackingBean) populateBean(request);

            try {
                model.delete(bean);
                ServletUtility.redirect(
                        ORSView.TRACKING_LIST_CTL, request, response);
                return;

            } catch (ApplicationException e) {

                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(
                    ORSView.TRACKING_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(
                    ORSView.TRACKING_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
    }

    @Override
    protected String getView() {
        return ORSView.TRACKING_VIEW;
    }
}