package in.co.rays.proj4.bean;

import java.util.Date;

public class LeadBean extends BaseBean {

    private Date dob;
    private String contactName;
    private String mobile;
    private String status;

    // Getter and Setter for dob
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    // Getter and Setter for contactName
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    // Getter and Setter for mobile
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Override getKey() method
    @Override
    public String getKey() {
        return id + "";
    }

    // Override getValue() method
    @Override
    public String getValue() {
        return contactName;
    }
}