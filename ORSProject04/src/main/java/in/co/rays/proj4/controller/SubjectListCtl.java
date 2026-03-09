
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * SubjectListCtl is a controller class to handle displaying, searching,
 * deleting, and managing the list of Subjects.
 * <p>
 * It interacts with SubjectModel and CourseModel for database operations and
 * forwards data to SubjectListView JSP.
 * </p>
 * <p>
 * URL pattern for this controller is "/ctl/SubjectListCtl".
 * </p>
 * <p>
 * It supports preloading of subject and course lists for dropdowns, paging,
 * and CRUD operations from the list page.
 * </p>
 * 
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "SubjectListCtl", urlPatterns = { "/ctl/SubjectListCtl" })
public class SubjectListCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(SubjectListCtl.class);

    /**
     * Preloads the list of subjects and courses for search filters or dropdowns
     * on the Subject list page.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
    	
    	log.debug("SubjectListCtl Method preload started ");

        SubjectModel subjectModel = new SubjectModel();
        CourseModel courseModel = new CourseModel();

        try {
            List subjectList = subjectModel.list();
            request.setAttribute("subjectList", subjectList);

            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);

        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	log.debug("SubjectListCtl Method preload ended");

    }

    /**
     * Populates a SubjectBean from request parameters.
     * <p>
     * This bean is used for searching/filtering the Subject list.
     * </p>
     * 
     * @param request HttpServletRequest object
     * @return BaseBean populated with request data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("SubjectListCtl Method populate started ");


        SubjectBean bean = new SubjectBean();

        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setCourseName(DataUtility.getString(request.getParameter("courseName")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setId(DataUtility.getLong(request.getParameter("subjectId")));

    	log.debug("SubjectListCtl Method populate ended");

        return bean;
    }

    /**
     * Handles HTTP GET request to display the list of Subjects.
     * <p>
     * Supports pagination and forwards the data to SubjectListView JSP.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("SubjectListCtl Method doget started ");


        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        SubjectBean bean = (SubjectBean) populateBean(request);
        SubjectModel model = new SubjectModel();

        try {
            List<SubjectBean> list = model.search(bean, pageNo, pageSize);
            List<SubjectBean> next = model.search(bean, pageNo + 1, pageSize);

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
        } catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	log.debug("SubjectListCtl Method doGet ended");

    }

    /**
     * Handles HTTP POST request for searching, paging, deleting, and resetting
     * the Subject list.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.debug("SubjectListCtl Method doPost started ");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        SubjectBean bean = (SubjectBean) populateBean(request);
        SubjectModel model = new SubjectModel();

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
                ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    SubjectBean deletebean = new SubjectBean();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.Delete(deletebean);
                        ServletUtility.setSuccessMessage("User deleted successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
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
            e.printStackTrace();
        } catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	log.debug("SubjectListCtl Method doPost ended");

    }

    /**
     * Returns the view page for Subject list.
     * 
     * @return JSP view path for Subject list page
     */
    @Override
    protected String getView() {
        return ORSView.SUBJECT_LIST_VIEW;
    }
}
