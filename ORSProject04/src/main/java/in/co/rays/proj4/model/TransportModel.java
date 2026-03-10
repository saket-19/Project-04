package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class TransportModel {

	/**
	 * Get Next PK
	 */
	public Integer nextPK() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_transport");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}

			rs.close();

		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

	/**
	 * Add Transport
	 */
	public long add(TransportBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		TransportBean duplicate = findByVehicleNumber(bean.getVehicleNumber());

		if (duplicate != null) {
			throw new DuplicateRecordException("Vehicle Number already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO st_transport VALUES(?,?,?,?,?,?,?,?,?)");

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getVehicleNumber());
			pstmt.setString(3, bean.getDriverName());
			pstmt.setString(4, bean.getVehicleType());
			pstmt.setString(5, bean.getTransportStatus());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Add Rollback Exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception : Exception in add Transport");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	/**
	 * Delete Transport
	 */
	public void delete(TransportBean bean) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_transport WHERE ID=?");

			pstmt.setLong(1, bean.getId());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete Rollback Exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in delete Transport");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find by Vehicle Number
	 */
	public TransportBean findByVehicleNumber(String vehicleNumber) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM st_transport WHERE VEHICLE_NUMBER=?");

		Connection conn = null;
		TransportBean bean = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, vehicleNumber);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				bean = new TransportBean();

				bean.setId(rs.getLong(1));
				bean.setVehicleNumber(rs.getString(2));
				bean.setDriverName(rs.getString(3));
				bean.setVehicleType(rs.getString(4));
				bean.setTransportStatus(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

			}

			rs.close();

		} catch (Exception e) {

			throw new ApplicationException("Exception in getting transport by vehicle number");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by PK
	 */
	public TransportBean findByPK(long pk) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM st_transport WHERE ID=?");

		Connection conn = null;
		TransportBean bean = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			pstmt.setLong(1, pk);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				bean = new TransportBean();

				bean.setId(rs.getLong(1));
				bean.setVehicleNumber(rs.getString(2));
				bean.setDriverName(rs.getString(3));
				bean.setVehicleType(rs.getString(4));
				bean.setTransportStatus(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

			}

			rs.close();

		} catch (Exception e) {

			throw new ApplicationException("Exception in getting transport by PK");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Update Transport
	 */
	public void update(TransportBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		TransportBean beanExist = findByVehicleNumber(bean.getVehicleNumber());

		if (beanExist != null && beanExist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Vehicle Number already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_transport SET VEHICLE_NUMBER=?,DRIVER_NAME=?,VEHICLE_TYPE=?,TRANSPORT_STATUS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setString(1, bean.getVehicleNumber());
			pstmt.setString(2, bean.getDriverName());
			pstmt.setString(3, bean.getVehicleType());
			pstmt.setString(4, bean.getTransportStatus());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Update Rollback Exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in updating Transport");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Search
	 */
	public List search(TransportBean bean, int pageNo, int pageSize)
			throws ApplicationException, DatabaseException {

		StringBuffer sql = new StringBuffer("SELECT * FROM st_transport WHERE 1=1");

		if (bean != null) {

			if (bean.getVehicleNumber() != null && bean.getVehicleNumber().length() > 0) {
				sql.append(" AND VEHICLE_NUMBER LIKE '" + bean.getVehicleNumber() + "%'");
			}

			if (bean.getDriverName() != null && bean.getDriverName().length() > 0) {
				sql.append(" AND DRIVER_NAME LIKE '" + bean.getDriverName() + "%'");
			}

			if (bean.getVehicleType() != null && bean.getVehicleType().length() > 0) {
				sql.append(" AND VEHICLE_TYPE LIKE '" + bean.getVehicleType() + "%'");
			}

			if (bean.getTransportStatus() != null && bean.getTransportStatus().length() > 0) {
				sql.append(" AND TRANSPORT_STATUS LIKE '" + bean.getTransportStatus() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				bean = new TransportBean();

				bean.setId(rs.getLong(1));
				bean.setVehicleNumber(rs.getString(2));
				bean.setDriverName(rs.getString(3));
				bean.setVehicleType(rs.getString(4));
				bean.setTransportStatus(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

				list.add(bean);
			}

			rs.close();

		} catch (Exception e) {

			throw new ApplicationException("Exception in search " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

}