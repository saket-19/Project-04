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

/**
 * TimetableCtl is a controller class to manage the addition, updating, and
 * validation of exam timetables.
 * <p>
 * It interacts with TimetableModel, SubjectModel, and CourseModel for data
 * operations and forwards the data to Timetable view.
 * </p>
 * <p>
 * URL pattern for this controller is "/ctl/TimetableCtl".
 * </p>
 * <p>
 * This controller includes preloading of subjects and courses, input validation,
 * and duplicate checking before adding or updating timetable records.
 * </p>
 * 
 * @author saket
 * @version 1.0
 */
@WebServlet(name = "TimetableCtl", urlPatterns = { "/ctl/TimetableCtl" })
public class TimetableCtl extends BaseCtl {
	
	private static Logger log = Logger.getLogger(TimetableCtl.class);

    /**
     * Preloads subjects and courses for dropdowns on the Timetable form.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
    	
    	log.debug("TimetableCtl Method preload started");
    	
        SubjectModel subjectModel = new SubjectModel();
        CourseModel courseModel = new CourseModel();

        try {
            List subjectList = subjectModel.list();
            request.setAttribute("subjectList", subjectList);

            List courseList = courseModel.list();
            request.setAttribute("courseList", courseList);

        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        
        log.debug("TimetableCtl Method preload ended");
    }

    /**
     * Validates the Timetable form inputs.
     * <p>
     * Checks for required fields, valid date, exam not on Sunday, and other business rules.
     * </p>
     * 
     * @param request HttpServletRequest object
     * @return boolean true if all fields are valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	
    	log.debug("TimetableCtl Method validate started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("semester"))) {
            request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examDate"))) {
            request.setAttribute("examDate", PropertyReader.getValue("error.require", "Date of Exam"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("examDate"))) {
            request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of Exam"));
            pass = false;
        } else if (DataValidator.isSunday(request.getParameter("examDate"))) {
            request.setAttribute("examDate", "Exam should not be on Sunday");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("examTime"))) {
            request.setAttribute("examTime", PropertyReader.getValue("error.require", "Exam Time"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("description"))) {
            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("courseId"))) {
            request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("subjectId"))) {
            request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
            pass = false;
        }

        log.debug("TimetableCtl Method validate ended");
        return pass;
    }

    /**
     * Populates a TimetableBean from request parameters.
     * 
     * @param request HttpServletRequest object
     * @return BaseBean populated with request data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("TimetableCtl Method populate started");

        TimeTableBean bean = new TimeTableBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setSemester(DataUtility.getString(request.getParameter("semester")));
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
        bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
        bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
        bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
        bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

        populateDTO(bean, request);
        
        log.debug("TimetableCtl Method populate ended");

        return bean;
    }

    /**
     * Handles HTTP GET request to populate the Timetable form for editing
     * a particular timetable record.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	log.debug("TimetableCtl Method doget started");
    	
        long id = DataUtility.getLong(request.getParameter("id"));

        TimeTableModel model = new TimeTableModel();

        if (id > 0) {
            try {
                TimeTableBean bean = model.findByPK(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
     
        ServletUtility.forward(getView(), request, response);
        
        log.debug("TimetableCtl Method doGEt ended");
    }

    /**
     * Handles HTTP POST request to save, update, cancel, or reset a timetable record.
     * <p>
     * Checks for duplicates before adding or updating.
     * </p>
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("TimetableCtl Method doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        TimeTableModel model = new TimeTableModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            TimeTableBean bean = (TimeTableBean) populateBean(request);

            try {
                TimeTableBean bean1 = model.checkByCourseName(bean.getCourseId(), bean.getExamDate());
                TimeTableBean bean2 = model.checkBySubjectName(bean.getCourseId(), bean.getSubjectId(), bean.getExamDate());
                TimeTableBean bean3 = model.checkBySemester(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(), bean.getExamDate());

               if (bean1 == null && bean2 == null && bean3 == null) {
                    long pk = model.add(bean);
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Timetable added successfully", request);
                } else {
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setErrorMessage("Timetable already exist!", request);
                }

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Timetable already exist!", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            TimeTableBean bean = (TimeTableBean) populateBean(request);

            try {
                TimeTableBean bean4 = model.checkByExamTime(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
                        bean.getExamDate(), bean.getExamTime(), bean.getDescription());

                if (id > 0 && bean4 == null) {
                    model.update(bean);
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setSuccessMessage("Timetable updated successfully", request);
                } else {
                    ServletUtility.setBean(bean, request);
                    ServletUtility.setErrorMessage("Timetable already exist!", request);
                }
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Timetable already exist!", request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        
        log.debug("TimetableCtl Method doPost ended");
    }

    /**
     * Returns the Timetable view page.
     * 
     * @return JSP view path for Timetable form
     */
    @Override
    protected String getView() {
        return ORSView.TIMETABLE_VIEW;
    }
}