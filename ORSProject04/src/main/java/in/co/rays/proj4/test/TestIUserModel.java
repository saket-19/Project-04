package in.co.rays.proj4.test;                    

import in.co.rays.proj4.model.UserModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;

public class TestIUserModel {
	public static UserModel model = new UserModel();

	public static void main(String[] args) throws Exception {

	//	testAdd();
		//testAuthenticate();
		testSearch();
	    //testDelete();
		//testUpdate();
		//testSearch();

	}
	public static void testAdd() {
		try {
			UserBean bean = new UserBean();

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			bean.setId(100);
			bean.setFirstName("Kapil");
			bean.setLastName("Malviya");
			bean.setLogin("3www0@gmail.com");
			bean.setPassword("kapil@123");
			bean.setDob(sdf.parse("05-07-1997"));
			bean.setRoleId(1L);
			bean.setGender("Male");
			bean.setMobileNo("9407411301");
		

			UserModel model = new UserModel();

			long pk = model.add(bean);
			UserBean addedbean = model.findByPk(pk);
			System.out.println("Test add succ");

			if (addedbean == null) {
				System.out.println("Test add fail");
			}

			System.out.println("record insert");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		public static void testAuthenticate() {
			try {
				UserBean bean = new UserBean();
				UserModel model = new UserModel();
				bean.setLogin("kmalviya30@gmail.com");
				bean.setPassword("kapil@123");
				bean = model.authenticate(bean.getLogin(), bean.getPassword());
				if (bean != null) {
					System.out.println("Successfully login");
				} else {
					System.out.println("Invalid login Id & password");
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
		
		public static void testDelete() throws ApplicationException{
			
			UserBean bean = new UserBean();
			
			bean.setId(1);
			
			model.delete(bean);
			
			System.out.println("record deleted");
			
		}
		
		public static void testUpdate(){
			try{
				UserBean bean=model.findByPk(0);
				bean.setFirstName("riyu");
				bean.setGender("male");
				model.update(bean);
				System.out.println("Record updated successfully");
				
				//UserBean updatedbean=model.findByPk(0);
				
				//if(!"ajay".equals(updatedbean.getFirstName())){
				//	System.out.println("Test Update fill");
				//}
				
			}catch(ApplicationException e){
				e.printStackTrace();
			}catch(DuplicateRecordException e){
				e.printStackTrace();
			}
		}
		public static void testSearch(){
			try{
				UserBean bean=new UserBean();
				List list=new ArrayList();

				list=model.search(bean, 0, 0);
				if(list.size() < 0){
					System.out.println("test Search fill");
				}
				Iterator it = list.iterator();
				
				while(it.hasNext()){
					bean=(UserBean)it.next();
					System.out.println(bean.getId());
					System.out.println(bean.getFirstName());
					System.out.println(bean.getLastName());
				}
				
			}catch(ApplicationException e){
				e.printStackTrace();
			}
		}
			

		

	
	
}
	


