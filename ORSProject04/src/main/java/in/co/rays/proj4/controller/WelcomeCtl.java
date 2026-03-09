
package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.utility.ServletUtility;

/**
 * Welcome Controller.
 * <p>
 * Loads the welcome page of the application.
 * </p>
 *
 * @author saket
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseCtl {

    /**
     * Handles HTTP GET request to display the welcome view.
     *
     * @param request  incoming HttpServletRequest
     * @param response outgoing HttpServletResponse
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an input/output error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Returns the view constant for the welcome page.
     *
     * @return WELCOME_VIEW page path
     */
    @Override
    protected String getView() {
        return ORSView.WELCOME_VIEW;
    }
}
