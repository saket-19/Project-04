package in.co.rays.proj4.bean;

import java.time.LocalDateTime;

public class MeetingBean extends BaseBean {
	 private long id;
	    private String code;
	    private String title;
	    private LocalDateTime time;
	    private String location;
	    private String status;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public LocalDateTime getTime() {
			return time;
		}
		public void setTime(LocalDateTime time) {
			this.time = time;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getKey() {
			// TODO Auto-generated method stub
			return null;
		}
		public String getValue() {
			// TODO Auto-generated method stub
			return null;
		}
	    
	    


}
