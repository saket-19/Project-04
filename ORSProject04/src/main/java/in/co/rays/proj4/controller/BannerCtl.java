package in.co.rays.proj4.controller;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.BannerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.BannerModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;
@WebServlet(name = "BannerCtl", urlPatterns = { "/ctl/BannerCtl" })
public class BannerCtl extends BaseCtl{


	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("code"))) {
	            request.setAttribute("code", PropertyReader.getValue("error.require", "Code"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("title"))) {
	            request.setAttribute("title", PropertyReader.getValue("error.require", "Title"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("imagePath"))) {
	            request.setAttribute("imagePath", PropertyReader.getValue("error.require", "Image Path"));
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("status"))) {
	            request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
	            pass = false;
	        }

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        BannerBean bean = new BannerBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setTitle(DataUtility.getString(request.getParameter("title")));
	        bean.setImagePath(DataUtility.getString(request.getParameter("imagePath")));
	        bean.setStatus(DataUtility.getString(request.getParameter("status")));

	        populateDTO(bean, request);

	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));

	        BannerModel model = new BannerModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (id > 0 || op != null) {
	            BannerBean bean;
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

	        BannerModel model = new BannerModel();

	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (OP_SAVE.equalsIgnoreCase(op)) {

	            BannerBean bean = (BannerBean) populateBean(request);

	            try {
	                long pk = model.add(bean);
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Banner added successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Banner code already exists", request);

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

	            BannerBean bean = (BannerBean) populateBean(request);

	            try {
	                if (id > 0) {
	                    model.update(bean);
	                }
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Banner updated successfully", request);

	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Banner code already exists", request);

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_DELETE.equalsIgnoreCase(op)) {

	            BannerBean bean = (BannerBean) populateBean(request);

	            try {
	                model.delete(bean);
	                ServletUtility.redirect(ORSView.BANNER_LIST_CTL, request, response);
	                return;

	            } catch (ApplicationException e) {
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.BANNER_LIST_CTL, request, response);
	            return;

	        } else if (OP_RESET.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.BANNER_CTL, request, response);
	            return;
	        }

	        ServletUtility.forward(getView(), request, response);
	    }

	    @Override
	    protected String getView() {
	        return ORSView.BANNER_VIEW;
	    }
	}


