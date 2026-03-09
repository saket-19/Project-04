package in.co.rays.proj4.bean;

public class LockerBean extends BaseBean {

    private long id;
    private String number;
    private String type;
    private Double annualFee;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAnnualFee() {
		return annualFee;
	}
	public void setAnnualFee(Double annualFee) {
		this.annualFee = annualFee;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
    
    

}
