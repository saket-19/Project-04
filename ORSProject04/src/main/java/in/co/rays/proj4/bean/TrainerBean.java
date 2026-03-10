package in.co.rays.proj4.bean;

public class TrainerBean extends BaseBean {

	    private long id;
	    private String code;            // UNIQUE
	    private String name;
	    private String specialization;
	    private String contactNumber;

	    // Default Constructor
	    public TrainerBean() {
	    }

	    // Getter and Setter for ID
	    public long getId() {
	        return id;
	    }

	    public void setId(long id) {
	        this.id = id;
	    }

	    // Getter and Setter for Code (Unique)
	    public String getCode() {
	        return code;
	    }

	    public void setCode(String code) {
	        this.code = code;
	    }

	    // Getter and Setter for Name
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    // Getter and Setter for Specialization
	    public String getSpecialization() {
	        return specialization;
	    }

	    public void setSpecialization(String specialization) {
	        this.specialization = specialization;
	    }

	    // Getter and Setter for Contact Number
	    public String getContactNumber() {
	        return contactNumber;
	    }

	    public void setContactNumber(String contactNumber) {
	        this.contactNumber = contactNumber;
	    }

	    // 🔹 Key for dropdown / list
	    @Override
	    public String getKey() {
	        return id + "";
	    }

	    // 🔹 Value for dropdown / list
	    @Override
	    public String getValue() {
	        return name;
	    }
	}


