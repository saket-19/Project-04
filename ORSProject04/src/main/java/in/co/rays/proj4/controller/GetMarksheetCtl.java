
package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

/**
 * Controller to fetch a Marksheet using Roll Number.
 * <p>
 * This controller validates roll number input, populates the MarksheetBean,
 * retrieves the corresponding marksheet from the database, and forwards the
 * result to the appropriate view.
 * </p>
 *
 * @author saket
 */
@WebServlet(name = "GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(GetMarksheetCtl.class);

    /**
     * Validates user input for roll number.
     *
     * @param request HttpServletRequest
     * @return true if valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
		
		log.debug("GetMarksheetCTL Method validate Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("rollNo"))) {
            request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
            pass = false;
        }
        
        log.debug("GetMarksheetCTL Method validate ended");

        return pass;
    }

    /**
     * Populates a MarksheetBean with request parameters.
     *
     * @param request HttpServletRequest
     * @return populated MarksheetBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("GetMarksheetCTL Method populate Started");

        MarksheetBean bean = new MarksheetBean();

        bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

        log.debug("GetMarksheetCTL Method populate ended");
        
        return bean;
    }

    /**
     * Displays the marksheet page.
     */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Handles form submission and fetches the marksheet by roll number.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("GetMarksheetCTL Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));

        MarksheetModel model = new MarksheetModel();

        MarksheetBean bean = (MarksheetBean) populateBean(request);

        if (OP_GO.equalsIgnoreCase(op)) {
            try {
                bean = model.findByRollNo(bean.getRollNo());
                if (bean != null) {
                    ServletUtility.setBean(bean, request);
                } else {
                    ServletUtility.setErrorMessage("RollNo Does Not exists", request);
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
        
        log.debug("GetMarksheetCTL Method doPost ended");
    }

    /**
     * Returns the view constant for marksheet display.
     *
     * @return GET_MARKSHEET_VIEW
     */
    @Override
    protected String getView() {
        return ORSView.GET_MARKSHEET_VIEW;
    }

}
