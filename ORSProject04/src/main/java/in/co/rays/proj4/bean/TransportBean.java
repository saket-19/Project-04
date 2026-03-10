package in.co.rays.proj4.bean;

public class TransportBean extends BaseBean {

	private long transportId;
	private String vehicleNumber;
	private String driverName;
	private String vehicleType;
	private String transportStatus;

	public long getTransportId() {
		return transportId;
	}

	public void setTransportId(long transportId) {
		this.transportId = transportId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getTransportStatus() {
		return transportStatus;
	}

	public void setTransportStatus(String transportStatus) {
		this.transportStatus = transportStatus;
	}

	@Override
	public String getKey() {
		return String.valueOf(transportId);
	}

	@Override
	public String getValue() {
		return vehicleNumber;
	}
}