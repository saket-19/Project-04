package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TimeTableBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourseModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.model.TimeTableModel;
import in.co.rays.proj4.utility.DataUtility;
import in.co.rays.proj4.utility.DataValidator;
import in.co.rays.proj4.utility.PropertyReader;
import in.co.rays.proj4.utility.ServletUtility;

@WebServlet(name = "TimeTableCtl", urlPatterns = { "/ctl/TimeTableCtl" })
public class TimeTableCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(TimeTableCtl.class);

    /**
     * Preload dropdown values
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.debug("TimeTableCtl preload started");

        SubjectModel subjectModel = new SubjectModel();
        CourseModel courseModel = new CourseModel();

        try {

            List<?> subjectList = subjectModel.list();
            List<?> courseList = courseModel.list();

            request.setAttribute("subjectList", subjectList);
            request.setAttribute("courseList", courseList);

        } catch (ApplicationException e) {
            log.error(e);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        log.debug("TimeTableCtl preload ended");
    }

    /**
     * Validate form input
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("TimeTableCtl validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("courseId"))) {
            request.setAttribute("courseId",
                    PropertyReader.getValue("error.require", "Course"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("subjectId"))) {
            request.setAttribute("subjectId",
                    PropertyReader.getValue("error.require", "Subject"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("semester"))) {
            request.setAttribute("semester",
                    PropertyReader.getValue("error.require", "Semester"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examDate"))) {

            request.setAttribute("examDate",
                    PropertyReader.getValue("error.require", "Exam Date"));
            pass = false;

        } else if (!DataValidator.isDate(request.getParameter("examDate"))) {

            request.setAttribute("examDate",
                    PropertyReader.getValue("error.date", "Exam Date"));
            pass = false;

        } else if (DataValidator.isSunday(request.getParameter("examDate"))) {

            request.setAttribute("examDate",
                    "Exam should not be on Sunday");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examTime"))) {
            request.setAttribute("examTime",
                    PropertyReader.getValue("error.require", "Exam Time"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description",
                    PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }

        log.debug("TimeTableCtl validate ended");

        return pass;
    }

    /**
     * Populate Bean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("TimeTableCtl populateBean started");

        TimeTableBean bean = new TimeTableBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
        bean.setSemester(DataUtility.getString(request.getParameter("semester")));
        bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
        bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));

        populateDTO(bean, request);

        log.debug("TimeTableCtl populateBean ended");

        return bean;
    }

    /**
     * Load record for edit
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TimeTableCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));

        TimeTableModel model = new TimeTableModel();

        if (id > 0) {
            try {

                TimeTableBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);

            } catch (ApplicationException e) {

                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("TimeTableCtl doGet ended");
    }

    /**
     * Save / Update
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("TimeTableCtl doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));

        TimeTableModel model = new TimeTableModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            TimeTableBean bean = (TimeTableBean) populateBean(request);

            try {

                model.add(bean);

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Timetable added successfully", request);

            } catch (DuplicateRecordException e) {

                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Timetable already exists", request);

            } catch (ApplicationException e) {

                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            TimeTableBean bean = (TimeTableBean) populateBean(request);

            try {

                model.update(bean);

                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Timetable updated successfully", request);

            } catch (DuplicateRecordException e) {

                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Timetable already exists", request);

            } catch (ApplicationException e) {

                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            //ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("TimeTableCtl doPost ended");
    }

    /**
     * Return View
     */
    @Override
    protected String getView() {
        return ORSView.TIMETABLE_VIEW;
    }
}