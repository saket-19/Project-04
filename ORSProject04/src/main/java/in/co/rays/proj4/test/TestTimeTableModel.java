package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.protobuf.TextFormat.ParseException;

import in.co.rays.proj4.bean.TimeTableBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TimeTableModel;

public class TestTimeTableModel {
	public static TimeTableModel model= new TimeTableModel();
	public static void main(String[] args) {
		testAdd();
		
	}
	public static void testAdd() {
		try {
			TimeTableBean bean = new TimeTableBean();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			//bean.setId(1);
			
			bean.setSemester("2");
			bean.setDescription("hello");
			bean.setExamDate(sdf.parse("22/09/2021"));
			bean.setExamTime("10 am to 1 pm");
			bean.setCourseId(1);
			bean.setCourseName("m.com");
			bean.setSubjectId(1);
			bean.setSubjectName("Account");
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
			long pk=model.add(bean);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void testdelete() {
		try {
			TimeTableBean bean = new TimeTableBean();
			long pk=1L;
			
			bean.setId(pk);
			 model.delete(bean);
			 
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testupdate() throws ParseException, DuplicateRecordException, Exception {
		try {
			TimeTableBean bean = new TimeTableBean();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			bean.setId(1L);
			
			bean.setCourseId(6);
			bean.setCourseName("mca");
			bean.setSubjectId(9);
			bean.setSubjectName("java");
			bean.setExamTime("1 to 4 pm");
			bean.setExamDate(sdf.parse("22/08/2021"));
			model.update(bean);;
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testfindBypk() {
		try {
			TimeTableBean bean = new TimeTableBean();
			
			bean=model.findByPK(2);
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getSubjectId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getSemester());
			System.out.println(bean.getExamDate());
			System.out.println(bean.getExamTime());
			
			
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
	public static void testlist() throws Exception {
		try {
		TimeTableBean bean = new TimeTableBean();
		List list= new ArrayList();
		list = model.list(0,3);
		
		Iterator it = list.iterator();
		while(it.hasNext()) {
			bean = (TimeTableBean) it.next();
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getSubjectId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getSemester());
			System.out.println(bean.getExamDate());
			System.out.println(bean.getExamTime());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
		
		}
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
	public static void testsearch() throws ApplicationException {
		TimeTableBean bean = new TimeTableBean();
		List list = new ArrayList();
		bean.setSubjectName("css");
		list=model.search(bean,0,0);
		if(list.size() < 0) {
			System.out.println("test search fail");
		}
		
		Iterator it = list.iterator();
		while(it.hasNext()) {
			bean=(TimeTableBean) it.next();
			
			System.out.println(bean.getId());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getSubjectId());
			System.out.println(bean.getSubjectName());
			System.out.println(bean.getSemester());
			System.out.println(bean.getExamDate());
			System.out.println(bean.getExamTime());
			System.out.println(bean.getDescription());
		}
	}
}
