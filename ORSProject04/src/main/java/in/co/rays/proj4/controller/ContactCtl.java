package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.ContactBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.ContactModel;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.ServletUtility;
@WebServlet(name = "ContactCtl", urlPatterns = { "/ctl/ContactCtl" })
public class ContactCtl extends BaseCtl{
	protected BaseBean populateBean(HttpServletRequest request) {

		ContactBean bean = new ContactBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));

		populateDTO(bean, request);

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    ContactBean bean = new ContactBean();   
	    long id = DataUtility.getLong(request.getParameter("id"));

	    if (id > 0) {
	        ContactModel model = new ContactModel();
	        bean = model.findByPK(id);  
	    }

	    ServletUtility.setBean(bean, request);  

	    ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));

		ContactModel model = new ContactModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			ContactBean bean = (ContactBean) populateBean(request);
			long pk = model.add(bean);
			ServletUtility.setBean(bean, request);
			ServletUtility.setSuccessMessage("Contact added successfully", request);
		}
		else if (OP_RESET.equalsIgnoreCase(op)) {
		    ServletUtility.redirect(ORSView.CONTACT_CTL, request, response);
		    return;
		}
		else if(OP_UPDATE.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.CONTACT_CTL, request, response);
			return;
		}

		
		
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		return ORSView.CONTACT_VIEW;
	}

}
