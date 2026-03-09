package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.TrackingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class TrackingModel {

    public Integer nextPK() throws DatabaseException {
        Connection conn = null;
        int pk = 0;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_tracking");
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

    public long add(TrackingBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        TrackingBean duplicate = findByNumber(bean.getNumber());
        if (duplicate != null) {
            throw new DuplicateRecordException("Tracking Number already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO st_tracking VALUES(?,?,?,?,?,?,?,?)");

            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getNumber());
            pstmt.setString(3, bean.getCurrentLocation());
            pstmt.setString(4, bean.getStatus());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());
            pstmt.setTimestamp(8, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Add Rollback " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add Tracking");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    public void delete(TrackingBean bean) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM st_tracking WHERE ID=?");

            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();

            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Delete Rollback Exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in delete Tracking");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public TrackingBean findByNumber(String number)
            throws ApplicationException {

        StringBuffer sql = new StringBuffer(
                "SELECT * FROM st_tracking WHERE NUMBER=?");

        TrackingBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new TrackingBean();
                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setCurrentLocation(rs.getString(3));
                bean.setStatus(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));
            }
            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Tracking by Number");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public TrackingBean findByPK(long pk)
            throws ApplicationException {

        StringBuffer sql = new StringBuffer(
                "SELECT * FROM st_tracking WHERE ID=?");

        TrackingBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new TrackingBean();
                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setCurrentLocation(rs.getString(3));
                bean.setStatus(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));
            }
            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Tracking by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    public void update(TrackingBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        TrackingBean existBean = findByNumber(bean.getNumber());
        if (existBean != null && existBean.getId() != bean.getId()) {
            throw new DuplicateRecordException("Tracking Number already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_tracking SET NUMBER=?, CURRENT_LOCATION=?, STATUS=?, " +
                    "CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? " +
                    "WHERE ID=?");

            pstmt.setString(1, bean.getNumber());
            pstmt.setString(2, bean.getCurrentLocation());
            pstmt.setString(3, bean.getStatus());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDatetime());
            pstmt.setTimestamp(7, bean.getModifiedDatetime());
            pstmt.setLong(8, bean.getId());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update Rollback Exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Tracking");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public List search(TrackingBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        StringBuffer sql = new StringBuffer(
                "SELECT * FROM st_tracking WHERE 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND ID=" + bean.getId());
            }
            if (bean.getNumber() != null && bean.getNumber().length() > 0) {
                sql.append(" AND NUMBER LIKE '" + bean.getNumber() + "%'");
            }
            if (bean.getCurrentLocation() != null && bean.getCurrentLocation().length() > 0) {
                sql.append(" AND CURRENT_LOCATION LIKE '" + bean.getCurrentLocation() + "%'");
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
                bean = new TrackingBean();
                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setCurrentLocation(rs.getString(3));
                bean.setStatus(rs.getString(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
                bean.setModifiedDatetime(rs.getTimestamp(8));

                list.add(bean);
            }
            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in search Tracking");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }
}
