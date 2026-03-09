package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.LockerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class LockerModel {

    public Integer nextPK() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_locker");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            throw new DatabaseException("Exception in getting PK");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk + 1;
    }

    public long add(LockerBean bean) throws ApplicationException, DuplicateRecordException {

        Connection conn = null;
        int pk = 0;

        LockerBean duplicate = findByNumber(bean.getNumber());
        if (duplicate != null) {
            throw new DuplicateRecordException("Locker number already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO st_locker VALUES(?,?,?,?,?,?,?)");

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getNumber());
            pstmt.setString(3, bean.getType());
            pstmt.setDouble(4, bean.getAnnualFee());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception " + ex.getMessage());
            }

            throw new ApplicationException("Exception in adding locker");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    public void delete(LockerBean bean) throws ApplicationException {

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_locker WHERE ID=?");
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

            throw new ApplicationException("Exception in deleting locker");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    public LockerBean findByPK(long pk) throws ApplicationException {

        StringBuffer sql = new StringBuffer("SELECT * FROM st_locker WHERE ID=?");

        Connection conn = null;
        LockerBean bean = null;

        try {

            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new LockerBean();

                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
            }

            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting locker by PK");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    public LockerBean findByNumber(String number) throws ApplicationException {

        StringBuffer sql = new StringBuffer("SELECT * FROM st_locker WHERE NUMBER=?");

        Connection conn = null;
        LockerBean bean = null;

        try {

            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                bean = new LockerBean();

                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));
            }

            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting locker by number");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    public List search(LockerBean bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    public List search(LockerBean bean, int pageNo, int pageSize) throws ApplicationException {

        StringBuffer sql = new StringBuffer("SELECT * FROM st_locker WHERE 1=1");

        if (bean != null) {

            if (bean.getId() > 0) {
                sql.append(" AND id=" + bean.getId());
            }

            if (bean.getNumber() != null && bean.getNumber().length() > 0) {
                sql.append(" AND number like '" + bean.getNumber() + "%'");
            }

            if (bean.getType() != null && bean.getType().length() > 0) {
                sql.append(" AND type like '" + bean.getType() + "%'");
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

                bean = new LockerBean();

                bean.setId(rs.getLong(1));
                bean.setNumber(rs.getString(2));
                bean.setType(rs.getString(3));
                bean.setAnnualFee(rs.getDouble(4));
                bean.setCreatedBy(rs.getString(5));
                bean.setModifiedBy(rs.getString(6));
                bean.setCreatedDatetime(rs.getTimestamp(7));

                list.add(bean);
            }

            rs.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in locker search");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }

    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }
}