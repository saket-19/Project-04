package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TransportModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet(name = "TransportCtl", urlPatterns = { "/ctl/TransportCtl" })
public class TransportCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TransportCtl.class);

	/**
	 * Validate Transport Form
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("TransportCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("vehicleNumber"))) {
			request.setAttribute("vehicleNumber", PropertyReader.getValue("error.require", "Vehicle Number"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("driverName"))) {
			request.setAttribute("driverName", PropertyReader.getValue("error.require", "Driver Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("driverName"))) {
			request.setAttribute("driverName", "Invalid Driver Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("vehicleType"))) {
			request.setAttribute("vehicleType", PropertyReader.getValue("error.require", "Vehicle Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("transportStatus"))) {
			request.setAttribute("transportStatus", PropertyReader.getValue("error.require", "Transport Status"));
			pass = false;
		}

		log.debug("TransportCtl Method validate Ended");

		return pass;
	}

	/**
	 * Populate TransportBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("TransportCtl Method populateBean Started");

		TransportBean bean = new TransportBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setVehicleNumber(DataUtility.getString(request.getParameter("vehicleNumber")));
		bean.setDriverName(DataUtility.getString(request.getParameter("driverName")));
		bean.setVehicleType(DataUtility.getString(request.getParameter("vehicleType")));
		bean.setTransportStatus(DataUtility.getString(request.getParameter("transportStatus")));

		populateDTO(bean, request);

		log.debug("TransportCtl Method populateBean Ended");

		return bean;
	}

	/**
	 * Handles GET request
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TransportCtl Method doGet Started");

		long id = DataUtility.getLong(request.getParameter("id"));

		TransportModel model = new TransportModel();

		if (id > 0) {

			try {

				TransportBean bean = model.findByPK(id);

				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {

				log.error(e);

				ServletUtility.handleException(e, request, response);

				return;
			}
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("TransportCtl Method doGet Ended");
	}

	/**
	 * Handles POST request
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TransportCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		TransportModel model = new TransportModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {

			TransportBean bean = (TransportBean) populateBean(request);

			try {

				long pk = model.add(bean);

				ServletUtility.setBean(bean, request);

				ServletUtility.setSuccessMessage("Transport added successfully", request);

			} catch (DuplicateRecordException e) {

				log.error(e);

				ServletUtility.setBean(bean, request);

				ServletUtility.setErrorMessage("Vehicle Number already exists", request);

			} catch (ApplicationException e) {

				log.error(e);

				ServletUtility.handleException(e, request, response);

				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			TransportBean bean = (TransportBean) populateBean(request);

			try {

				if (id > 0) {

					model.update(bean);
				}

				ServletUtility.setBean(bean, request);

				ServletUtility.setSuccessMessage("Transport updated successfully", request);

			} catch (DuplicateRecordException e) {

				log.error(e);

				ServletUtility.setBean(bean, request);

				ServletUtility.setErrorMessage("Vehicle Number already exists", request);

			} catch (ApplicationException e) {

				log.error(e);

				ServletUtility.handleException(e, request, response);

				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TRANSPORT_LIST_CTL, request, response);

			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TRANSPORT_CTL, request, response);

			return;
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("TransportCtl Method doPost Ended");
	}

	/**
	 * Returns Transport JSP View
	 */
	@Override
	protected String getView() {

		return ORSView.TRANSPORT_VIEW;
	}

}