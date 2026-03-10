package in.co.rays.proj4.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.TrainerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class TrainerModel {
	    // 🔹 Next PK
	    public Integer nextPK() throws DatabaseException {

	        Connection conn = null;
	        int pk = 0;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_TRAINER");
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

	    // 🔹 Add
	    public long add(TrainerBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;
	        int pk = 0;

	        TrainerBean duplicate = findByCode(bean.getCode());
	        if (duplicate != null) {
	            throw new DuplicateRecordException("Code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();

	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "INSERT INTO ST_TRAINER VALUES(?,?,?,?,?,?,?,?,?)");

	            pstmt.setInt(1, pk);
	            pstmt.setString(2, bean.getCode());
	            pstmt.setString(3, bean.getName());
	            pstmt.setString(4, bean.getSpecialization());
	            pstmt.setString(5, bean.getContactNumber());
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
	                throw new ApplicationException("Rollback Exception " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception in add Trainer");

	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return pk;
	    }

	    // 🔹 Delete
	    public void delete(TrainerBean bean) throws ApplicationException {

	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "DELETE FROM ST_TRAINER WHERE ID=?");

	            pstmt.setLong(1, bean.getId());
	            pstmt.executeUpdate();

	            conn.commit();
	            pstmt.close();

	        } catch (Exception e) {

	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Delete rollback Exception " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception in delete Trainer");

	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    // 🔹 Find by Code
	    public TrainerBean findByCode(String code) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_TRAINER WHERE CODE=?");

	        TrainerBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();

	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, code);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {

	                bean = new TrainerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setSpecialization(rs.getString(4));
	                bean.setContactNumber(rs.getString(5));
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	                bean.setModifiedDatetime(rs.getTimestamp(9));
	            }
	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in findByCode");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    // 🔹 Find by PK
	    public TrainerBean findByPK(long pk) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_TRAINER WHERE ID=?");

	        Connection conn = null;
	        TrainerBean bean = null;

	        try {
	            conn = JDBCDataSource.getConnection();

	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setLong(1, pk);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {

	                bean = new TrainerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setSpecialization(rs.getString(4));
	                bean.setContactNumber(rs.getString(5));
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

	    // 🔹 Update
	    public void update(TrainerBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;

	        TrainerBean exist = findByCode(bean.getCode());
	        if (exist != null && exist.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	            		"UPDATE ST_TRAINER SET CODE=?, NAME=?, SPECIALIZATION=?, CONTACTNUMBER=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?");

	            pstmt.setString(1, bean.getCode());
	            pstmt.setString(2, bean.getName());
	            pstmt.setString(3, bean.getSpecialization());
	            pstmt.setString(4, bean.getContactNumber());
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
	                throw new ApplicationException("Update rollback Exception " + ex.getMessage());
	            }

	            throw new ApplicationException("Exception in update Trainer");

	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    // 🔹 Search
	    public List search(TrainerBean bean, int pageNo, int pageSize)
	            throws DatabaseException, ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_TRAINER WHERE 1=1");

	        if (bean != null) {

	            if (bean.getId() > 0) {
	                sql.append(" AND ID=" + bean.getId());
	            }

	            if (bean.getCode() != null && bean.getCode().length() > 0) {
	                sql.append(" AND CODE LIKE '" + bean.getCode() + "%'");
	            }

	            if (bean.getName() != null && bean.getName().length() > 0) {
	                sql.append(" AND NAME LIKE '" + bean.getName() + "%'");
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

	                bean = new TrainerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setSpecialization(rs.getString(4));
	                bean.setContactNumber(rs.getString(5));
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

	    public List list() throws ApplicationException, DatabaseException {
	        return search(null, 0, 0);
	    }
	}


