package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.StudentModel;

public class TestStudentModel {
	public static void main(String[] args) {
		 testAdd();
		// testDelete();
		// testUpdate();
		//testFindByPK();
		//testFindByEmailId();
		
	}

	public static void testAdd() {

		try {
			StudentBean bean = new StudentBean();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			bean.setFirstName("Uday");
			bean.setLastName("Dabi");
			bean.setDob(sdf.parse("2005-30-10"));
			bean.setEmail("uday@gmail.com");
			bean.setMobileNo("9589359987");
			bean.setCollegeId(2);
			bean.setCollegeName("Davv");
			bean.setCreatedBy("root");
			bean.setModifiedBy("root");
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));

			StudentModel model = new StudentModel();

			long pk = model.add(bean);
			System.out.println("Record Sussessfully insert");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testDelete() {

		try {
			StudentBean bean = new StudentBean();
			StudentModel model = new StudentModel();
			long pk = 2;

			bean.setId(pk);
			model.delete(bean);
			System.out.println("deleted");
			StudentBean deletebean = model.findByPK(pk);
			if (deletebean != null) {
				System.out.println("Test Delete fail");
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static void testUpdate() {

		try {
			StudentModel model = new StudentModel();
			StudentBean bean = model.findByPK(1);

			bean.setFirstName("ram");
			bean.setLastName("sharma");
			bean.setCollegeId(5L);
			bean.setId(1);
			model.update(bean);
			System.out.println("Test Update succ");

		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (DuplicateRecordException e) {
			e.printStackTrace();
		}
	}

	public static void testFindByPK() {
		try {
			StudentBean bean = new StudentBean();
			StudentModel model = new StudentModel();
			Long pk = 1L;
			bean = model.findByPK(pk);
			if (bean == null) {
				System.out.println("Test Find By PK fail");
			}
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getDob());
			System.out.println(bean.getMobileNo());
			System.out.println(bean.getEmail());
			System.out.println(bean.getCollegeId());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}public static void testFindByEmailId() {
        try {
            StudentBean bean = new StudentBean();
            StudentModel model=new StudentModel();
            bean = model.findByEmailId("saket@gmail.com");
            if (bean == null) {
                System.out.println("Test Find By EmailId fail");
            }
            System.out.println(bean.getId());
            System.out.println(bean.getFirstName());
            System.out.println(bean.getLastName());
            System.out.println(bean.getDob());
            System.out.println(bean.getMobileNo());
            System.out.println(bean.getEmail());
            System.out.println(bean.getCollegeId());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }
	
 
	
}
