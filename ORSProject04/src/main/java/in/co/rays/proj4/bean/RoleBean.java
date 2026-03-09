package in.co.rays.proj4.bean;

public class RoleBean extends BaseBean {
	private String name;
	private String description;

	public static final int ADMIN = 1;
	public static final int STUDENT = 2;
	public static final int COLLEGE = 3;
	public static final int KIOSK = 4;
	public static final int FACULTY = 5;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static int getAdmin() {
		return ADMIN;
	}

	public static int getStudent() {
		return STUDENT;
	}

	public static int getCollege() {
		return COLLEGE;
	}

	public static int getKiosk() {
		return KIOSK;
	}

	public static int getFaculty() {
		return FACULTY;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return id + "";
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return name;
	}

}
