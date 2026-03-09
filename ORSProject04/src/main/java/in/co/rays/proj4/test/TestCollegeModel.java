package in.co.rays.proj4.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Timestamp;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;

public class TestCollegeModel {
	public static void main(String[] args) throws ApplicationException, DuplicateRecordException {
		testAdd();

	}

	public static void testAdd() throws ApplicationException, DuplicateRecordException {
		CollegeBean bean = new CollegeBean();
		bean.setName("saket");
		bean.setPhoneNo("2346576");
		bean.setCity("indore");
		bean.setState("Madhya Pradesh");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new java.sql.Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		CollegeModel model = new CollegeModel();

		try {
			model.add(bean);
			System.out.println("College data added succesfully");

		} catch (ApplicationException e) {
			e.printStackTrace();

		}
	}

	public static void testDelete() {
		try {
			CollegeBean bean = new CollegeBean();
			bean.setId(1l);
			CollegeModel model = new CollegeModel();
			model.delete(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testUpdate() throws ApplicationException, DuplicateRecordException {
		try {
			CollegeBean bean = new CollegeBean();
			bean.setCity("Bhopal");
			CollegeModel model = new CollegeModel();
			model.update(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
