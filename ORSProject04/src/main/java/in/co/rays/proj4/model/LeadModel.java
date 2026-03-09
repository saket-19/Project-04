package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.LeadBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class LeadModel {

    // -------------------- Next PK --------------------
    public Integer nextPK() throws DatabaseException {
        int pk = 0;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_lead");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            throw new DatabaseException("Exception in nextPK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk + 1;
    }

    // -------------------- Add --------------------
    public long add(LeadBean bean) throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        LeadBean existBean = findByName(bean.getContactName());
        if (existBean != null) {
            throw new DuplicateRecordException("Lead already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO st_lead (ID, CONTACT_NAME, MOBILE, DOB, STATUS, CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?)"
            );

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getContactName());
            pstmt.setString(3, bean.getMobile());

            if (bean.getDob() != null) {
                pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            pstmt.setString(5, bean.getStatus());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDatetime());
            pstmt.setTimestamp(9, bean.getModifiedDatetime());

            // 🔥 IMPORTANT
            pstmt.executeUpdate();
            conn.commit();

            pstmt.close();

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception " + ex.getMessage());
            }

            e.printStackTrace(); // 🔥 ADD THIS
            throw new ApplicationException("Exception in add Lead " + e.getMessage());

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    // -------------------- Delete --------------------
    public void delete(LeadBean bean) throws ApplicationException {

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_lead WHERE ID=?");
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
            throw new ApplicationException("Exception in delete Lead");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // -------------------- Find By Name --------------------
    public LeadBean findByName(String name) throws ApplicationException {

        String sql = "SELECT * FROM st_lead WHERE CONTACT_NAME=?";
        LeadBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = new LeadBean();
                bean.setId(rs.getLong(1));
                bean.setDob(rs.getDate(2));
                bean.setContactName(rs.getString(3));
                bean.setMobile(rs.getString(4));
                bean.setStatus(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }

            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in findByName");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    // -------------------- Find By PK --------------------
    public LeadBean findByPK(long pk) throws ApplicationException {

        String sql = "SELECT * FROM st_lead WHERE ID=?";
        LeadBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pk);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = new LeadBean();
                bean.setId(rs.getLong(1));
                bean.setDob(rs.getDate(2));
                bean.setContactName(rs.getString(3));
                bean.setMobile(rs.getString(4));
                bean.setStatus(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }

            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in findByPK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    // -------------------- Update --------------------
    public void update(LeadBean bean) throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        LeadBean existBean = findByName(bean.getContactName());
        if (existBean != null && existBean.getId() != bean.getId()) {
            throw new DuplicateRecordException("Lead already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_lead SET CONTACT_NAME=?, MOBILE=?,DOB=? STATUS=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?");

            
            pstmt.setString(1, bean.getContactName());
            pstmt.setString(2, bean.getMobile());
            pstmt.setDate(3, new Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getStatus());
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
                throw new ApplicationException("Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in update Lead");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // -------------------- Search --------------------
    public List search(LeadBean bean, int pageNo, int pageSize)
            throws ApplicationException, DatabaseException {

        StringBuffer sql = new StringBuffer("SELECT * FROM st_lead WHERE 1=1");

        if (bean != null) {

            if (bean.getId() > 0) {
                sql.append(" AND ID=" + bean.getId());
            }

            if (bean.getContactName() != null && bean.getContactName().length() > 0) {
                sql.append(" AND CONTACT_NAME like '" + bean.getContactName() + "%'");
            }

            if (bean.getMobile() != null && bean.getMobile().length() > 0) {
                sql.append(" AND MOBILE like '" + bean.getMobile() + "%'");
            }

            if (bean.getStatus() != null && bean.getStatus().length() > 0) {
                sql.append(" AND STATUS like '" + bean.getStatus() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        List list = new ArrayList();
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new LeadBean();

                bean.setId(rs.getLong(1));
                bean.setContactName(rs.getString(2));
                bean.setMobile(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setStatus(rs.getString(5));
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

    // -------------------- List --------------------
    public List list() throws ApplicationException, DatabaseException {
        return search(null, 0, 0);
    }
}