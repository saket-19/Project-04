package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.SubjectModel;

public class TestSubjectModel {
	public static SubjectModel model=new SubjectModel();
	
	public static void main(String[] args) throws Exception {
		//testAdd();
        //testFindByName();
		//testFindByPk();
        //testUpdate();
		testSearch();
		//testList();
	}
	public static void testAdd() throws DuplicateRecordException {

		try {
			SubjectBean bean = new SubjectBean();
			// bean.setId(1);
			bean.setName("css");
			bean.setCourseId(1);
			bean.setCourseName("ccssn");
			bean.setDescription("programingL");
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

			long pk = model.add(bean);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testFindByName() {
		try {
			SubjectBean bean = new SubjectBean();
			bean = model.findByName("css");
			if (bean == null) {
				System.out.println("test findBy Name fail");
			}

			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getModifiedDatetime());

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	public static void testFindByPk() {
		try {
			SubjectBean bean = new SubjectBean();
			long pk = 1L;
			bean = model.FindByPK(pk);
			if (bean == null) {
				System.out.println("test findbypk fail");
			}
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedDatetime());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	public static void testUpdate() {
		try {
			SubjectBean bean = model.FindByPK(1L);
			bean.setName("java");
			// bean.setDescription("commerce");
			model.update(bean);
			System.out.println("update succ");

			/*
			 * SubjectBean updateBean=model.testFindByPK(2L); if(!
			 * "ram".equals(updateBean.getSubjectName())){
			 * System.out.println("test update fail"); }
			 */

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (DuplicateRecordException e) {
			e.printStackTrace();
		}
	}
	public static void testSearch() throws DatabaseException {
		try {
			SubjectBean bean = new SubjectBean();
			//bean.setSubjectName("Java");
		    // bean.setId(2L);
			//bean.setCourseId(2L);
			bean.setCourseName("Mca");
			List list = new ArrayList();
			list = model.search(bean,1,10);

			Iterator it = list.iterator();
			while (it.hasNext()) {
				bean = (SubjectBean) it.next();
				System.out.println(bean.getId());
				System.out.println(bean.getName());
				System.out.println(bean.getCourseId());
				System.out.println(bean.getCourseName());
				System.out.println(bean.getDescription());
				System.out.println(bean.getCreatedBy());
				System.out.println(bean.getModifiedBy());
				System.out.println(bean.getCreatedDatetime());
				System.out.println(bean.getModifiedDatetime());
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	public static void testList() throws Exception {
		try {
			SubjectBean bean = new SubjectBean();
			List list = new ArrayList();
			list = model.list(1, 10);
			if (list.size() < 0) {
				System.out.println("test list fail");
			}
			Iterator it = list.iterator();
			while (it.hasNext()) {
				bean = (SubjectBean) it.next();
				System.out.println(bean.getId());
				System.out.println(bean.getName());
				System.out.println(bean.getDescription());
				System.out.println(bean.getCourseId());
				System.out.println(bean.getCourseName());
				System.out.println(bean.getCreatedBy());
				System.out.println(bean.getModifiedBy());
				System.out.println(bean.getCreatedDatetime());
				System.out.println(bean.getModifiedDatetime());

			}

		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	

}
