package in.co.rays.proj4.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.NotificationBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class NotificationModel {
	    public Integer nextPK() throws DatabaseException {

	        Connection conn = null;
	        int pk = 0;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_notification");
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
	     * Add Notification
	     */
	    public long add(NotificationBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;
	        int pk = 0;

	        // Duplicate check (code must be unique)
	        NotificationBean existBean = findByCode(bean.getCode());
	        if (existBean != null) {
	            throw new DuplicateRecordException("Notification code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();

	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "INSERT INTO st_notification VALUES(?,?,?,?,?,?,?,?,?,?)");

	            pstmt.setLong(1, pk);
	            pstmt.setString(2, bean.getCode());
	            pstmt.setString(3, bean.getMessage());
	            pstmt.setString(4, bean.getSentTo());

	            // LocalDateTime → Timestamp
	            if (bean.getSentTime() != null) {
	                pstmt.setTimestamp(5, Timestamp.valueOf(bean.getSentTime()));
	            } else {
	                pstmt.setTimestamp(5, null);
	            }

	            pstmt.setString(6, bean.getStatus());
	            pstmt.setString(7, bean.getCreatedBy());
	            pstmt.setString(8, bean.getModifiedBy());
	            pstmt.setTimestamp(9, bean.getCreatedDatetime());
	            pstmt.setTimestamp(10, bean.getModifiedDatetime());

	            pstmt.executeUpdate();
	            conn.commit();
	            pstmt.close();

	        } catch (Exception e) {

	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Exception : Add rollback " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception : Exception in add Notification");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return pk;
	    }

	    /**
	     * Delete
	     */
	    public void delete(NotificationBean bean) throws ApplicationException {

	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_notification WHERE ID=?");
	            pstmt.setLong(1, bean.getId());
	            pstmt.executeUpdate();

	            conn.commit();
	            pstmt.close();

	        } catch (Exception e) {

	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Delete rollback exception " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception in delete notification");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    /**
	     * Find by Code
	     */
	    public NotificationBean findByCode(String code) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_notification WHERE CODE=?");
	        NotificationBean bean = null;

	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, code);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new NotificationBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setMessage(rs.getString(3));
	                bean.setSentTo(rs.getString(4));

	                Timestamp ts = rs.getTimestamp(5);
	                if (ts != null) {
	                    bean.setSentTime(ts.toLocalDateTime());
	                }

	                bean.setStatus(rs.getString(6));
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDatetime(rs.getTimestamp(9));
	                bean.setModifiedDatetime(rs.getTimestamp(10));
	            }

	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in getting notification by code");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    /**
	     * Find by PK
	     */
	    public NotificationBean findByPK(long pk) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_notification WHERE ID=?");
	        NotificationBean bean = null;

	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setLong(1, pk);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new NotificationBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setMessage(rs.getString(3));
	                bean.setSentTo(rs.getString(4));

	                Timestamp ts = rs.getTimestamp(5);
	                if (ts != null) {
	                    bean.setSentTime(ts.toLocalDateTime());
	                }

	                bean.setStatus(rs.getString(6));
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDatetime(rs.getTimestamp(9));
	                bean.setModifiedDatetime(rs.getTimestamp(10));
	            }

	            rs.close();
	            pstmt.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in getting notification by PK");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    /**
	     * Update
	     */
	    public void update(NotificationBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;

	        NotificationBean existBean = findByCode(bean.getCode());
	        if (existBean != null && existBean.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Notification already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "UPDATE st_notification SET CODE=?,MESSAGE=?,SENT_TO=?,SENT_TIME=?,STATUS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

	            pstmt.setString(1, bean.getCode());
	            pstmt.setString(2, bean.getMessage());
	            pstmt.setString(3, bean.getSentTo());

	            if (bean.getSentTime() != null) {
	                pstmt.setTimestamp(4, Timestamp.valueOf(bean.getSentTime()));
	            } else {
	                pstmt.setTimestamp(4, null);
	            }

	            pstmt.setString(5, bean.getStatus());
	            pstmt.setString(6, bean.getCreatedBy());
	            pstmt.setString(7, bean.getModifiedBy());
	            pstmt.setTimestamp(8, bean.getCreatedDatetime());
	            pstmt.setTimestamp(9, bean.getModifiedDatetime());
	            pstmt.setLong(10, bean.getId());

	            pstmt.executeUpdate();
	            conn.commit();
	            pstmt.close();

	        } catch (Exception e) {

	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Update rollback exception " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception in updating notification");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    /**
	     * Search
	     */
	    public List search(NotificationBean bean, int pageNo, int pageSize)
	            throws ApplicationException, DatabaseException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_notification WHERE 1=1");

	        if (bean != null) {

	            if (bean.getId() > 0) {
	                sql.append(" AND ID = " + bean.getId());
	            }

	            if (bean.getCode() != null && bean.getCode().length() > 0) {
	                sql.append(" AND CODE LIKE '" + bean.getCode() + "%'");
	            }

	            if (bean.getStatus() != null && bean.getStatus().length() > 0) {
	                sql.append(" AND STATUS LIKE '" + bean.getStatus() + "%'");
	            }
	        }

	        if (pageSize > 0) {
	            pageNo = (pageNo - 1) * pageSize;
	            sql.append(" LIMIT " + pageNo + "," + pageSize);
	        }

	        ArrayList list = new ArrayList();
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new NotificationBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setMessage(rs.getString(3));
	                bean.setSentTo(rs.getString(4));

	                Timestamp ts = rs.getTimestamp(5);
	                if (ts != null) {
	                    bean.setSentTime(ts.toLocalDateTime());
	                }

	                bean.setStatus(rs.getString(6));
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDatetime(rs.getTimestamp(9));
	                bean.setModifiedDatetime(rs.getTimestamp(10));

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

	    public List search(NotificationBean bean) throws ApplicationException, DatabaseException {
	        return search(bean, 0, 0);
	    }

	    public List list() throws ApplicationException, DatabaseException {
	        return search(null, 0, 0);
	    }
	}


