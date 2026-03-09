
package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * <p><b>MarksheetMeritListCtl</b> is responsible for displaying
 * the Merit List of students based on their marks.</p>
 *
 * <p>This controller performs:</p>
 * <ul>
 *   <li>Fetching merit list using MarksheetModel</li>
 *   <li>Displaying results with pagination</li>
 *   <li>Handling BACK navigation</li>
 * </ul>
 *
 * <p>Accessible at: <b>/ctl/MarksheetMeritListCtl</b></p>
 * 
 * @author Krati
 */
@WebServlet(name = "MarksheetMeritListCtl", urlPatterns = { "/ctl/MarksheetMeritListCtl" })
public class MarksheetMeritListCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(MarksheetMeritListCtl.class);
    /**
     * Handles GET request to load and display the Merit List.
     *
     * @param request  incoming HTTP request
     * @param response HTTP response to be sent
     * @throws ServletException if any servlet-related issue occurs
     * @throws IOException      if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("MarksheetMeritListCtl Method doGet Started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        MarksheetModel model = new MarksheetModel();

        try {
            List<MarksheetBean> list = model.getMeritList(pageNo, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
        	log.error(e);
            e.printStackTrace();
            ServletUtility.handleException(e, request, response);
        }
        
        log.debug("MarksheetMeritListCtl Method doGet ended");
    }

    /**
     * Handles POST request for BACK operation.
     *
     * @param request  incoming HTTP request
     * @param response HTTP response to redirect or forward
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	log.debug("MarksheetMeritListCtl Method doPost Started");
        String op = DataUtility.getString(request.getParameter("operation"));

        if (OP_BACK.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
        }
        
        log.debug("MarksheetMeritListCtl Method doPost ended");
    }

    /**
     * Returns the JSP view used to display the Merit List.
     *
     * @return path to MARKSHEET_MERIT_LIST_VIEW JSP
     */
    @Override
    protected String getView() {
        return ORSView.MARKSHEET_MERIT_LIST_VIEW;
    }
}
