package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.TrainingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.TrainingModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet("/TrainingListCtl")
public class TrainingListCtl extends BaseCtl {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pageNo = 1;
        int pageSize = 10;

        TrainingBean bean = new TrainingBean();
        TrainingModel model = new TrainingModel();

        try {
            List list = model.search(bean, pageNo, pageSize);

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            ServletUtility.handleException(e, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? 10 : pageSize;

        String op = DataUtility.getString(request.getParameter("operation"));

        TrainingBean bean = new TrainingBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setTrainerName(DataUtility.getString(request.getParameter("trainerName")));

        TrainingModel model = new TrainingModel();

        if (OP_SEARCH.equalsIgnoreCase(op)) {
            pageNo = 1;

        } else if (OP_NEXT.equalsIgnoreCase(op)) {
            pageNo++;

        } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
            pageNo--;

        } else if (OP_NEW.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TRAINING_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TRAINING_LIST_CTL, request, response);
            return;
        }

        try {
            List list = model.search(bean, pageNo, pageSize);
            List nextList = model.search(bean, pageNo + 1, pageSize);

            request.setAttribute("nextListSize", nextList.size());

            ServletUtility.setList(list, request);
            ServletUtility.setBean(bean, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);

            if (list.size() == 0) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            ServletUtility.forward(getView(), request, response);

        } catch (ApplicationException e) {
            ServletUtility.handleException(e, request, response);
        }
    }

    @Override
    protected String getView() {
        return ORSView.TRAINING_LIST_VIEW;
    }
}