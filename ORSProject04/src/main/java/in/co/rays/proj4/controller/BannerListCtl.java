package in.co.rays.proj4.controller;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.BannerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.model.BannerModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;
@WebServlet(name = "BannerListCtl", urlPatterns = { "/ctl/BannerListCtl" })
public class BannerListCtl extends BaseCtl{

	    @Override
	    protected void preload(HttpServletRequest request) {

	        BannerModel model = new BannerModel();

	        try {
	            List list = model.list();
	            request.setAttribute("bannerList", list);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        BannerBean bean = new BannerBean();

	        bean.setId(DataUtility.getLong(request.getParameter("bannerId")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setTitle(DataUtility.getString(request.getParameter("title")));
	        bean.setStatus(DataUtility.getString(request.getParameter("status")));

	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	System.out.println("in do get");

	        int pageNo = 1;
	        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

	        BannerBean bean = (BannerBean) populateBean(request);
	        BannerModel model = new BannerModel();

	        try {
	            List<BannerBean> list = model.search(bean, pageNo, pageSize);
	            List<BannerBean> next = model.search(bean, pageNo + 1, pageSize);

	            if (list == null || list.isEmpty()) {
	                ServletUtility.setErrorMessage("No record found", request);
	            }

	            ServletUtility.setList(list, request);
	            ServletUtility.setPageNo(pageNo, request);
	            ServletUtility.setPageSize(pageSize, request);
	            ServletUtility.setBean(bean, request);
	            request.setAttribute("nextListSize", next.size());

	            ServletUtility.forward(getView(), request, response);

	        } catch (Exception e) {
	            e.printStackTrace();
	            ServletUtility.handleException(e, request, response);
	        }
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        List list = null;
	        List next = null;

	        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
	        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

	        BannerBean bean = (BannerBean) populateBean(request);
	        BannerModel model = new BannerModel();

	        String op = DataUtility.getString(request.getParameter("operation"));
	        String[] ids = request.getParameterValues("ids");

	        try {

	            if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

	                if (OP_SEARCH.equalsIgnoreCase(op)) {
	                    pageNo = 1;
	                } else if (OP_NEXT.equalsIgnoreCase(op)) {
	                    pageNo++;
	                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
	                    pageNo--;
	                }

	            } else if (OP_NEW.equalsIgnoreCase(op)) {

	                ServletUtility.redirect(ORSView.BANNER_CTL, request, response);
	                return;

	            } else if (OP_DELETE.equalsIgnoreCase(op)) {

	                pageNo = 1;

	                if (ids != null && ids.length > 0) {

	                    BannerBean deleteBean = new BannerBean();

	                    for (String id : ids) {
	                        deleteBean.setId(DataUtility.getLong(id));
	                        model.delete(deleteBean);
	                    }

	                    ServletUtility.setSuccessMessage("Banner deleted successfully", request);

	                } else {
	                    ServletUtility.setErrorMessage("Select at least one record", request);
	                }

	            } else if (OP_RESET.equalsIgnoreCase(op)) {

	                ServletUtility.redirect(ORSView.BANNER_LIST_CTL, request, response);
	                return;

	            } else if (OP_BACK.equalsIgnoreCase(op)) {

	                ServletUtility.redirect(ORSView.BANNER_LIST_CTL, request, response);
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

	        } catch (Exception e) {
	            e.printStackTrace();
	            //ServletUtility.handleException(e, request, response);
	        }
	    }

	    @Override
	    protected String getView() {
	        return ORSView.BANNER_LIST_VIEW;
	    }
	}


