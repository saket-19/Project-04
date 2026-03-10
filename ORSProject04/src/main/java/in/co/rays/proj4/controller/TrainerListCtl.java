package in.co.rays.proj4.controller;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TrainerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.model.TrainerModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet("/TrainerListCtl")
public class TrainerListCtl extends BaseCtl{
	    @Override
	    protected void preload(HttpServletRequest request) {
	        TrainerModel model = new TrainerModel();
	        try {
	            List list = model.list();
	            request.setAttribute("trainerList", list);
	        } catch (ApplicationException e) {
	            e.printStackTrace();
	        } catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        TrainerBean bean = new TrainerBean();

	        bean.setId(DataUtility.getLong(request.getParameter("trainerId")));
	        bean.setCode(DataUtility.getString(request.getParameter("code")));
	        bean.setName(DataUtility.getString(request.getParameter("name")));
	        bean.setSpecialization(DataUtility.getString(request.getParameter("specialization")));
	        bean.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));

	        return bean;
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        int pageNo = 1;
	        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

	        TrainerBean bean = (TrainerBean) populateBean(request);
	        TrainerModel model = new TrainerModel();

	        try {
	            List<TrainerBean> list = model.search(bean, pageNo, pageSize);
	            List<TrainerBean> next = model.search(bean, pageNo + 1, pageSize);

	            if (list == null || list.isEmpty()) {
	                ServletUtility.setErrorMessage("No record found", request);
	            }

	            ServletUtility.setList(list, request);
	            ServletUtility.setPageNo(pageNo, request);
	            ServletUtility.setPageSize(pageSize, request);
	            ServletUtility.setBean(bean, request);
	            request.setAttribute("nextListSize", next.size());

	            ServletUtility.forward(getView(), request, response);

	        } catch (ApplicationException e) {
	            e.printStackTrace();
	            ServletUtility.handleException(e, request, response);
	            return;
	        } catch (DatabaseException e) {
	            e.printStackTrace();
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

	        TrainerBean bean = (TrainerBean) populateBean(request);
	        TrainerModel model = new TrainerModel();

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
	                ServletUtility.redirect(ORSView.TRAINER_CTL, request, response);
	                return;

	            } else if (OP_DELETE.equalsIgnoreCase(op)) {
	                pageNo = 1;

	                if (ids != null && ids.length > 0) {
	                    TrainerBean deleteBean = new TrainerBean();

	                    for (String id : ids) {
	                        deleteBean.setId(DataUtility.getLong(id));
	                        model.delete(deleteBean);
	                    }

	                    ServletUtility.setSuccessMessage("Trainer deleted successfully", request);

	                } else {
	                    ServletUtility.setErrorMessage("Select at least one record", request);
	                }

	            } else if (OP_RESET.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.TRAINER_LIST_CTL, request, response);
	                return;

	            } else if (OP_BACK.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.TRAINER_LIST_CTL, request, response);
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

	        } catch (ApplicationException e) {
	            e.printStackTrace();
	            ServletUtility.handleException(e, request, response);
	            return;
	        } catch (DatabaseException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    protected String getView() {
	        return ORSView.TRAINER_LIST_VIEW;
	    }
	}


