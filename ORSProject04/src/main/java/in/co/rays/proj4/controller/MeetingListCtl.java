package in.co.rays.proj4.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.MeetingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.MeetingModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet("/MeetingListCtl")
public class MeetingListCtl extends BaseCtl{
	
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	    	// Set default values first
	    	if (request.getAttribute("pageNo") == null) {
	    	    request.setAttribute("pageNo", 1);
	    	}
	    	if (request.getAttribute("pageSize") == null) {
	    	    request.setAttribute("pageSize", 10);
	    	}

	    	int pageNo = ServletUtility.getPageNo(request);
	    	int pageSize = ServletUtility.getPageSize(request);

	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? 10 : pageSize;

	        MeetingBean bean = (MeetingBean) populateBean(request);
	        MeetingModel model = new MeetingModel();
	        List list = null;

	        try {
	            list = model.search(bean, pageNo, pageSize);

	            if (list == null || list.size() == 0) {
	                ServletUtility.setErrorMessage("No record found", request);
	            }

	            ServletUtility.setList(list, request);
	            ServletUtility.setPageNo(pageNo, request);
	            ServletUtility.setPageSize(pageSize, request);

	            // To support Next button
	            int nextListSize = model.search(bean, pageNo + 1, pageSize).size();
	            request.setAttribute("nextListSize", nextListSize);

	            ServletUtility.forward(getView(), request, response);

	        } catch (ApplicationException e) {
	            ServletUtility.handleException(e, request, response);
	        }
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	    	// Set default values first
	    	if (request.getAttribute("pageNo") == null) {
	    	    request.setAttribute("pageNo", 1);
	    	}
	    	if (request.getAttribute("pageSize") == null) {
	    	    request.setAttribute("pageSize", 10);
	    	}

	    	int pageNo = ServletUtility.getPageNo(request);
	    	int pageSize = ServletUtility.getPageSize(request);

	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? 10 : pageSize;

	        String op = DataUtility.getString(request.getParameter("operation"));

	        MeetingBean bean = (MeetingBean) populateBean(request);
	        MeetingModel model = new MeetingModel();
	        List list = null;

	        try {
	            if (BaseCtl.OP_SEARCH.equalsIgnoreCase(op)) {
	                pageNo = 1;
	            } else if (BaseCtl.OP_NEXT.equalsIgnoreCase(op)) {
	                pageNo++;
	            } else if (BaseCtl.OP_PREVIOUS.equalsIgnoreCase(op)) {
	                pageNo--;
	            } else if (BaseCtl.OP_RESET.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.MEETING_LIST_CTL, request, response);
	                return;
	            } else if (BaseCtl.OP_NEW.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.MEETING_CTL, request, response);
	                return;
	            } else if (BaseCtl.OP_DELETE.equalsIgnoreCase(op)) {
	                String[] ids = request.getParameterValues("ids");
	                if (ids != null && ids.length > 0) {
	                    for (String id : ids) {
	                        bean.setId(DataUtility.getLong(id));
	                        model.delete(bean);
	                    }
	                    ServletUtility.setSuccessMessage("Record(s) deleted successfully", request);
	                } else {
	                    ServletUtility.setErrorMessage("Select at least one record", request);
	                }
	            }

	            list = model.search(bean, pageNo, pageSize);

	            if (list == null || list.size() == 0) {
	                ServletUtility.setErrorMessage("No record found", request);
	            }

	            ServletUtility.setList(list, request);
	            ServletUtility.setPageNo(pageNo, request);
	            ServletUtility.setPageSize(pageSize, request);

	            // For Next button
	            int nextListSize = model.search(bean, pageNo + 1, pageSize).size();
	            request.setAttribute("nextListSize", nextListSize);

	            ServletUtility.forward(getView(), request, response);

	        } catch (ApplicationException e) {
	            ServletUtility.handleException(e, request, response);
	        }
	    }

	    @Override
	    protected MeetingBean populateBean(HttpServletRequest request) {
	        MeetingBean bean = new MeetingBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setTitle(DataUtility.getString(request.getParameter("title")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        // Optional: you can add other fields like location/status if needed
	        return bean;
	    }

	    @Override
	    protected String getView() {
	        return ORSView.MEETING_LIST_VIEW;
	    }
	}


