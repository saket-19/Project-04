package in.co.rays.proj4.bean;

public class CourseBean extends BaseBean{
	private String name ;
	private String description;
	private String duration;
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
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getKey() {
		return id + "";
	}
	public String getValue() {
		return name;
	}
	
	

}
