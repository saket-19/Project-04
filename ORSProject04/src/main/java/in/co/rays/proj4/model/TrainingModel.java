package in.co.rays.proj4.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import in.co.rays.proj4.bean.TrainingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;


public class TrainingModel {
	    public Integer nextPK() throws DatabaseException {
	        Connection conn = null;
	        int pk = 0;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_training");
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

	    public long add(TrainingBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;
	        int pk = 0;

	        TrainingBean exist = findByCode(bean.getCode());
	        if (exist != null) {
	            throw new DuplicateRecordException("Training Code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "INSERT INTO st_training VALUES(?,?,?,?,?,?,?,?,?)");

	            pstmt.setLong(1, pk);
	            pstmt.setString(2, bean.getCode());
	            pstmt.setString(3, bean.getName());
	            pstmt.setString(4, bean.getTrainerName());
	            pstmt.setDate(5, java.sql.Date.valueOf(bean.getDate()));
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
	                throw new ApplicationException("Add rollback exception " + ex.getMessage());
	            }
	            throw new ApplicationException("Exception in add training");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return pk;
	    }

	    public void delete(TrainingBean bean) throws ApplicationException {

	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_training WHERE ID=?");
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
	            throw new ApplicationException("Exception in delete training");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    public TrainingBean findByCode(String code) throws ApplicationException {

	        String sql = "SELECT * FROM st_training WHERE CODE=?";
	        TrainingBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, code);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new TrainingBean();
	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setTrainerName(rs.getString(4));
	                bean.setDate(rs.getDate(5).toLocalDate());
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	            }
	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in findByCode");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    public TrainingBean findByPK(long pk) throws ApplicationException {

	        String sql = "SELECT * FROM st_training WHERE ID=?";
	        TrainingBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setLong(1, pk);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new TrainingBean();
	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setTrainerName(rs.getString(4));
	                bean.setDate(rs.getDate(5).toLocalDate());
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	            }
	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in findByPK");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    public void update(TrainingBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;

	        TrainingBean exist = findByCode(bean.getCode());
	        if (exist != null && exist.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Training already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	            	    "UPDATE st_training SET CODE=?, NAME=?, TRAINER_NAME=?, DATE=?, CREATED_BY=?, MODIFIED_BY=?, MODIFIED_DATETIME=? WHERE ID=?");

	            	pstmt.setString(1, bean.getCode());
	            	pstmt.setString(2, bean.getName());
	            	pstmt.setString(3, bean.getTrainerName());
	            	pstmt.setDate(4, java.sql.Date.valueOf(bean.getDate()));
	            	pstmt.setString(5, bean.getCreatedBy());
	            	pstmt.setString(6, bean.getModifiedBy());
	            	pstmt.setTimestamp(7, bean.getModifiedDatetime());
	            	pstmt.setLong(8, bean.getId());

	        } catch (Exception e) {
	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Update rollback exception " + ex.getMessage());
	            }
	            throw new ApplicationException("Exception in update training");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    public List<TrainingBean> search(TrainingBean bean, int pageNo, int pageSize)
	            throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_training WHERE 1=1");

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

	        List<TrainingBean> list = new ArrayList<>();
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new TrainingBean();
	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setName(rs.getString(3));
	                bean.setTrainerName(rs.getString(4));
	                bean.setDate(rs.getDate(5).toLocalDate());
	                list.add(bean);
	            }
	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in search");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return list;
	    }

	    public List<TrainingBean> list() throws ApplicationException {
	        return search(null, 0, 0);
	    }
	}


