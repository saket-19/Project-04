package in.co.rays.proj4.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.BannerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class BannerModel {
	    // Next PK
	    public Integer nextPK() throws DatabaseException {

	        Connection conn = null;
	        int pk = 0;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM st_banner");
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

	    // Add
	    public long add(BannerBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;
	        int pk = 0;

	        BannerBean existBean = findByCode(bean.getCode());
	        if (existBean != null) {
	            throw new DuplicateRecordException("Banner code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "INSERT INTO st_banner VALUES(?,?,?,?,?,?,?,?,?)");

	            pstmt.setLong(1, pk);
	            pstmt.setString(2, bean.getCode());
	            pstmt.setString(3, bean.getTitle());
	            pstmt.setString(4, bean.getImagePath());
	            pstmt.setString(5, bean.getStatus());
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
	                throw new ApplicationException("Exception : add rollback " + ex.getMessage());
	            }
	            throw new ApplicationException("Exception : Exception in add Banner");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return pk;
	    }

	    // Delete
	    public void delete(BannerBean bean) throws ApplicationException {

	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_banner WHERE ID=?");
	            pstmt.setLong(1, bean.getId());
	            pstmt.executeUpdate();

	            conn.commit();
	            pstmt.close();

	        } catch (Exception e) {
	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException("Exception : delete rollback " + ex.getMessage());
	            }
	            throw new ApplicationException("Exception in delete Banner");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    // Find by Code
	    public BannerBean findByCode(String code) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_banner WHERE CODE=?");
	        BannerBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, code);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new BannerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setTitle(rs.getString(3));
	                bean.setImagePath(rs.getString(4));
	                bean.setStatus(rs.getString(5));
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	                bean.setModifiedDatetime(rs.getTimestamp(9));
	            }

	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in getting Banner by code");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    // Find by PK
	    public BannerBean findByPK(long pk) throws ApplicationException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_banner WHERE ID=?");
	        BannerBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setLong(1, pk);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                bean = new BannerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setTitle(rs.getString(3));
	                bean.setImagePath(rs.getString(4));
	                bean.setStatus(rs.getString(5));
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	                bean.setModifiedDatetime(rs.getTimestamp(9));
	            }

	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in getting Banner by PK");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return bean;
	    }

	    // Update
	    public void update(BannerBean bean) throws ApplicationException, DuplicateRecordException {

	        Connection conn = null;

	        BannerBean existBean = findByCode(bean.getCode());
	        if (existBean != null && existBean.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Banner code already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(
	                    "UPDATE st_banner SET CODE=?, TITLE=?, IMAGE_PATH=?, STATUS=?, CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? WHERE ID=?");

	            pstmt.setString(1, bean.getCode());
	            pstmt.setString(2, bean.getTitle());
	            pstmt.setString(3, bean.getImagePath());
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
	                throw new ApplicationException("Exception : update rollback " + ex.getMessage());
	            }
	            throw new ApplicationException("Exception in updating Banner");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	    }

	    // Search
	    public List search(BannerBean bean, int pageNo, int pageSize)
	            throws ApplicationException, DatabaseException {

	        StringBuffer sql = new StringBuffer("SELECT * FROM st_banner WHERE 1=1");

	        if (bean != null) {
	            if (bean.getId() > 0) {
	                sql.append(" AND id=" + bean.getId());
	            }
	            if (bean.getCode() != null && bean.getCode().length() > 0) {
	                sql.append(" AND code like '" + bean.getCode() + "%'");
	            }
	            if (bean.getTitle() != null && bean.getTitle().length() > 0) {
	                sql.append(" AND title like '" + bean.getTitle() + "%'");
	            }
	            if (bean.getStatus() != null && bean.getStatus().length() > 0) {
	                sql.append(" AND status like '" + bean.getStatus() + "%'");
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
	                bean = new BannerBean();

	                bean.setId(rs.getLong(1));
	                bean.setCode(rs.getString(2));
	                bean.setTitle(rs.getString(3));
	                bean.setImagePath(rs.getString(4));
	                bean.setStatus(rs.getString(5));
	                bean.setCreatedBy(rs.getString(6));
	                bean.setModifiedBy(rs.getString(7));
	                bean.setCreatedDatetime(rs.getTimestamp(8));
	                bean.setModifiedDatetime(rs.getTimestamp(9));

	                list.add(bean);
	            }

	            rs.close();

	        } catch (Exception e) {
	            throw new ApplicationException("Exception in search Banner");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return list;
	    }

	    // List
	    public List list() throws ApplicationException, DatabaseException {
	        return search(null, 0, 0);
	    }
	}


