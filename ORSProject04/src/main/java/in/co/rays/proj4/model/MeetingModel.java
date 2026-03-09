package in.co.rays.proj4.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.MeetingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;
public class MeetingModel {
	public Integer nextPK() throws DatabaseException {
	    Connection conn = null;
	    int pk = 0;

	    try {
	        conn = JDBCDataSource.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_meeting");
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            pk = rs.getInt(1);
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	        throw new DatabaseException("Exception : Exception in getting PK");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return pk + 1;
	}

	// ================== ADD ==================
	public long add(MeetingBean bean) throws ApplicationException, DuplicateRecordException {

	    Connection conn = null;
	    int pk = 0;

	    MeetingBean existBean = findByCode(bean.getCode());
	    if (existBean != null) {
	        throw new DuplicateRecordException("Code already exists");
	    }

	    try {
	        conn = JDBCDataSource.getConnection();
	        pk = nextPK();

	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement(
	                "insert into st_meeting values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	        pstmt.setLong(1, pk);
	        pstmt.setString(2, bean.getCode());
	        pstmt.setString(3, bean.getTitle());

	        if (bean.getTime() != null) {
	            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(bean.getTime()));
	        } else {
	            pstmt.setTimestamp(4, null);
	        }

	        pstmt.setString(5, bean.getLocation());
	        pstmt.setString(6, bean.getStatus());

	        // Audit Fields
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
	            throw new ApplicationException("Exception : add rollback " + ex.getMessage());
	        }
	        throw new ApplicationException("Exception : Exception in add Meeting");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return pk;
	}

	// ================== UPDATE ==================
	public void update(MeetingBean bean) throws ApplicationException, DuplicateRecordException {

	    Connection conn = null;

	    MeetingBean existBean = findByCode(bean.getCode());
	    if (existBean != null && !(existBean.getId() == bean.getId())) {
	        throw new DuplicateRecordException("Code already exists");
	    }

	    try {
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement(
	                "update st_meeting set code=?, title=?, time=?, location=?, status=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?");

	        pstmt.setString(1, bean.getCode());
	        pstmt.setString(2, bean.getTitle());

	        if (bean.getTime() != null) {
	            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(bean.getTime()));
	        } else {
	            pstmt.setTimestamp(3, null);
	        }

	        pstmt.setString(4, bean.getLocation());
	        pstmt.setString(5, bean.getStatus());

	        // Audit Fields
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
	            throw new ApplicationException("Exception : update rollback " + ex.getMessage());
	        }
	        throw new ApplicationException("Exception : Exception in update Meeting");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	}

	// ================== DELETE ==================
	public void delete(MeetingBean bean) throws ApplicationException {

	    Connection conn = null;

	    try {
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement("delete from st_meeting where id=?");
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
	        throw new ApplicationException("Exception : Exception in delete Meeting");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	}

	// ================== FIND BY PK ==================
	public MeetingBean findByPK(long pk) throws ApplicationException {

	    MeetingBean bean = null;
	    Connection conn = null;

	    try {
	        conn = JDBCDataSource.getConnection();

	        PreparedStatement pstmt = conn.prepareStatement("select * from st_meeting where id=?");
	        pstmt.setLong(1, pk);

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            bean = new MeetingBean();

	            bean.setId(rs.getLong(1));
	            bean.setCode(rs.getString(2));
	            bean.setTitle(rs.getString(3));

	            java.sql.Timestamp ts = rs.getTimestamp(4);
	            if (ts != null) {
	                bean.setTime(ts.toLocalDateTime());
	            }

	            bean.setLocation(rs.getString(5));
	            bean.setStatus(rs.getString(6));

	            // Audit Fields
	            bean.setCreatedBy(rs.getString(7));
	            bean.setModifiedBy(rs.getString(8));
	            bean.setCreatedDatetime(rs.getTimestamp(9));
	            bean.setModifiedDatetime(rs.getTimestamp(10));
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	        throw new ApplicationException("Exception : Exception in getting Meeting by PK");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return bean;
	}

	// ================== FIND BY CODE ==================
	public MeetingBean findByCode(String code) throws ApplicationException {

	    MeetingBean bean = null;
	    Connection conn = null;

	    try {
	        conn = JDBCDataSource.getConnection();

	        PreparedStatement pstmt = conn.prepareStatement("select * from st_meeting where code=?");
	        pstmt.setString(1, code);

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            bean = new MeetingBean();

	            bean.setId(rs.getLong(1));
	            bean.setCode(rs.getString(2));
	            bean.setTitle(rs.getString(3));

	            java.sql.Timestamp ts = rs.getTimestamp(4);
	            if (ts != null) {
	                bean.setTime(ts.toLocalDateTime());
	            }

	            bean.setLocation(rs.getString(5));
	            bean.setStatus(rs.getString(6));

	            // Audit Fields
	            bean.setCreatedBy(rs.getString(7));
	            bean.setModifiedBy(rs.getString(8));
	            bean.setCreatedDatetime(rs.getTimestamp(9));
	            bean.setModifiedDatetime(rs.getTimestamp(10));
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	        throw new ApplicationException("Exception : Exception in getting Meeting by Code");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return bean;
	}
	 public List<MeetingBean> search(MeetingBean bean, int pageNo, int pageSize) throws ApplicationException {
		 List<MeetingBean> list = new ArrayList<MeetingBean>();
	        Connection conn = null;
	        StringBuffer sql = new StringBuffer("SELECT * FROM st_meeting WHERE 1=1");

	        try {
	            if (bean != null) {
	                if (bean.getId() > 0) {
	                    sql.append(" AND id = " + bean.getId());
	                }
	                if (bean.getTitle() != null && bean.getTitle().trim().length() > 0) {
	                    sql.append(" AND title LIKE '" + bean.getTitle() + "%'");
	                }
	                if (bean.getCode() != null && bean.getCode().trim().length() > 0) {
	                    sql.append(" AND code LIKE '" + bean.getCode() + "%'");
	                }
	                if (bean.getLocation() != null && bean.getLocation().trim().length() > 0) {
	                    sql.append(" AND location LIKE '" + bean.getLocation() + "%'");
	                }
	                if (bean.getStatus() != null && bean.getStatus().trim().length() > 0) {
	                    sql.append(" AND status LIKE '" + bean.getStatus() + "%'");
	                }
	            }

	            // Pagination
	            if (pageSize > 0) {
	                int startIndex = (pageNo - 1) * pageSize;
	                sql.append(" LIMIT " + startIndex + ", " + pageSize);
	            }

	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                MeetingBean resultBean = new MeetingBean();
	                resultBean.setId(rs.getLong("id"));
	                resultBean.setCode(rs.getString("code"));
	                resultBean.setTitle(rs.getString("title"));

	                java.sql.Timestamp ts = rs.getTimestamp("time");
	                if (ts != null) {
	                    resultBean.setTime(ts.toLocalDateTime());
	                }

	                resultBean.setLocation(rs.getString("location"));
	                resultBean.setStatus(rs.getString("status"));
	                resultBean.setCreatedBy(rs.getString("created_by"));
	                resultBean.setModifiedBy(rs.getString("modified_by"));
	                resultBean.setCreatedDatetime(rs.getTimestamp("created_datetime"));
	                resultBean.setModifiedDatetime(rs.getTimestamp("modified_datetime"));

	                list.add(resultBean);
	            }

	            rs.close();
	            pstmt.close();
	        } catch (Exception e) {
	            throw new ApplicationException("Exception in MeetingModel search: " + e.getMessage());
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return list;
	    }
	
}
