
package in.co.rays.proj4.controller;

import in.co.rays.proj4.controller.ORSView;
import in.co.rays.proj4.utility.ServletUtility;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * FrontController Filter.
 * <p>
 * This filter intercepts every request going to /ctl/* and /doc/* and checks
 * whether the user's session is active or expired. If the session is missing,
 * it forwards the user to the login page with an error message.
 * </p>
 *
 * @author saket
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

    /**
     * Initializes the filter.
     *
     * @param conf FilterConfig object
     * @throws ServletException
     */
    public void init(FilterConfig conf) throws ServletException {
    }

    /**
     * Processes each request before reaching the target servlet/JSP.
     * <ul>
     *     <li>Checks if the user session exists</li>
     *     <li>If no session, forwards to login view</li>
     *     <li>If session exists, request continues normally</li>
     * </ul>
     *
     * @param req  the ServletRequest object
     * @param resp the ServletResponse object
     * @param chain FilterChain to continue request processing
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();

        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        if (session.getAttribute("user") == null) {
            request.setAttribute("error", "Your session has been expired. Please Login again!");
            ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
            return;
        } else {
            chain.doFilter(req, resp);
        }
    }

    /**
     * Destroys the filter.
     */
    public void destroy() {
    }
}

