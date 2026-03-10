package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.model.TransportModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * TransportListCtl handles listing, searching, pagination,
 * and deletion of Transport records.
 */

@WebServlet(name = "TransportListCtl", urlPatterns = { "/ctl/TransportListCtl" })

public class TransportListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TransportListCtl.class);

	/**
	 * Preload Transport list
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		log.debug("TransportListCtl preload started");

		TransportModel model = new TransportModel();

		try {

			List list = model.search(new TransportBean(), 0, 0);

			request.setAttribute("transportList", list);

		} catch (ApplicationException | DatabaseException e) {

			log.error(e);

		}

		log.debug("TransportListCtl preload ended");
	}

	/**
	 * Populate TransportBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("TransportListCtl populateBean started");

		TransportBean bean = new TransportBean();

		bean.setId(DataUtility.getLong(request.getParameter("transportId")));
		bean.setVehicleNumber(DataUtility.getString(request.getParameter("vehicleNumber")));
		bean.setDriverName(DataUtility.getString(request.getParameter("driverName")));
		bean.setVehicleType(DataUtility.getString(request.getParameter("vehicleType")));
		bean.setTransportStatus(DataUtility.getString(request.getParameter("transportStatus")));

		log.debug("TransportListCtl populateBean ended");

		return bean;
	}

	/**
	 * Display Transport List
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TransportListCtl doGet started");

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TransportBean bean = (TransportBean) populateBean(request);

		TransportModel model = new TransportModel();

		try {

			List list = model.search(bean, pageNo, pageSize);

			List next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {

				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);

			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException | DatabaseException e) {

			log.error(e);

			ServletUtility.handleException(e, request, response);

			return;
		}

		log.debug("TransportListCtl doGet ended");
	}

	/**
	 * Handles Search, Next, Previous, Delete, Reset
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TransportListCtl doPost started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		TransportBean bean = (TransportBean) populateBean(request);

		TransportModel model = new TransportModel();

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op)
					|| OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {

					pageNo = 1;

				} else if (OP_NEXT.equalsIgnoreCase(op)) {

					pageNo++;

				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {

					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TRANSPORT_CTL, request, response);

				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					TransportBean deleteBean = new TransportBean();

					for (String id : ids) {

						deleteBean.setId(DataUtility.getLong(id));

						model.delete(deleteBean);
					}

					ServletUtility.setSuccessMessage("Transport deleted successfully", request);

				} else {

					ServletUtility.setErrorMessage("Select at least one record", request);
				}

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TRANSPORT_LIST_CTL, request, response);

				return;

			} else if (OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TRANSPORT_LIST_CTL, request, response);

				return;
			}

			list = model.search(bean, pageNo, pageSize);

			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {

				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);

			ServletUtility.setPageNo(pageNo, request);

			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.setBean(bean, request);

			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException | DatabaseException e) {

			log.error(e);

			ServletUtility.handleException(e, request, response);

			return;
		}

		log.debug("TransportListCtl doPost ended");
	}

	/**
	 * Returns Transport List JSP
	 */
	@Override
	protected String getView() {

		return ORSView.TRANSPORT_LIST_VIEW;
	}
}