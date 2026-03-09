
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * RoleListCtl is a controller class to handle listing, searching, paging, and
 * deleting of Role records.
 * <p>
 * URL pattern for this controller is "/ctl/RoleListCtl".
 * </p>
 * 
 * It interacts with RoleModel for database operations and forwards data to the
 * RoleListView JSP page.
 * 
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "RoleListCtl", urlPatterns = { "/ctl/RoleListCtl" })
public class RoleListCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(RoleListCtl.class);
	

    /**
     * Preloads the data required for Role list page.
     * <p>
     * Loads all roles and sets them in request attribute "roleList".
     * </p>
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
    	
    	log.debug("RoleListCtl Method preload Started");
        RoleModel roleModel = new RoleModel();

        try {
            List roleList = roleModel.list();
            request.setAttribute("roleList", roleList);
        } catch (ApplicationException e) {
        	log.error(e);
            e.printStackTrace();
        }
        
        log.debug("RoleListCtl Method preload ended");
    }

    /**
     * Populates RoleBean from HTTP request parameters for search/filtering.
     * 
     * @param request HttpServletRequest object
     * @return BaseBean populated with Role search data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.debug("RoleListCtl Method populate Started");
        RoleBean bean = new RoleBean();

        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setId(DataUtility.getLong(request.getParameter("roleId")));

        log.debug("RoleListCtl Method populate ended");
        return bean;
    }

    /**
     * Handles HTTP GET request.
     * <p>
     * Fetches list of roles for the first page and forwards it to RoleListView.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	log.debug("RoleListCtl Method doGet Started");
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        RoleBean bean = (RoleBean) populateBean(request);
        RoleModel model = new RoleModel();

        try {
            List<RoleBean> list = model.search(bean, pageNo, pageSize);
            List<RoleBean> next = model.search(bean, pageNo + 1, pageSize);

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
        	//ServletUtility.handleExceptionDBList(getView(), bean, pageNo, pageSize, request, response);
        }
        log.debug("RoleListCtl Method doGet ended");
    }

    /**
     * Handles HTTP POST request.
     * <p>
     * Performs operations like search, next/previous page navigation, new, delete,
     * reset, and back.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	log.debug("RoleListCtl Method doPost Started");
    	
    	List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        RoleBean bean = (RoleBean) populateBean(request);
        RoleModel model = new RoleModel();

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        try {

            if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    RoleBean deletebean = new RoleBean();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("User deleted successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found ", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
        	log.error(e);
            e.printStackTrace();
            return;
        }
        
        log.debug("RoleListCtl Method doPost ended");
    }

    /**
     * Returns the view page for Role list module.
     * 
     * @return JSP view path for Role list
     */
    @Override
    protected String getView() {
        return ORSView.ROLE_LIST_VIEW;
    }
}
