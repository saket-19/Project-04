package in.co.rays.proj4.bean;

import java.time.LocalDateTime;

public class NotificationBean extends BaseBean {
	    private long id;            // Primary Key
	    private String code;        // UNIQUE
	    private String message;
	    private String sentTo;
	    private LocalDateTime sentTime;
	    private String status;

	    // Getter & Setter

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

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public String getSentTo() {
	        return sentTo;
	    }

	    public void setSentTo(String sentTo) {
	        this.sentTo = sentTo;
	    }

	    public LocalDateTime getSentTime() {
	        return sentTime;
	    }

	    public void setSentTime(LocalDateTime sentTime) {
	        this.sentTime = sentTime;
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
