package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.ComplaintBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class ComplaintModel {
	public Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_complaint");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);

			}
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception in getting pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;

	}

	public long add(ComplaintBean bean) throws DuplicateRecordException, ApplicationException {
		Connection conn = null;
		int pk = 0;

		ComplaintBean duplicatecomplaintName = findByName(bean.getTitle());
		if (duplicatecomplaintName != null) {
			throw new DuplicateRecordException("complaint already exist");
		}
		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_complaint values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getType());
			pstmt.setString(5, bean.getRaisedBy());
			pstmt.setString(6, bean.getAssignedTo());
			pstmt.setString(7, bean.getPriority());
			pstmt.setString(8, bean.getStatus());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Excetion : add rollback Exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add course");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	// Find by PK
	public ComplaintBean findByPK(long id) throws ApplicationException {
		Connection conn = null;
		ComplaintBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from st_complaint where id=?");
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean = new ComplaintBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setType(rs.getString(4));
				bean.setRaisedBy(rs.getString(5));
				bean.setAssignedTo(rs.getString(6));
				bean.setPriority(rs.getString(7));
				bean.setStatus(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}

			rs.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in findByPK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	// Find by Name (Title)
	public ComplaintBean findByName(String title) {
		Connection conn = null;
		ComplaintBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from st_complaint where title=?");
			ps.setString(1, title);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean = new ComplaintBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setType(rs.getString(4));
				bean.setRaisedBy(rs.getString(5));
				bean.setAssignedTo(rs.getString(6));
				bean.setPriority(rs.getString(7));
				bean.setStatus(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	// Update
	public void update(ComplaintBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		ComplaintBean exist = findByName(bean.getTitle());
		if (exist != null && exist.getId() != bean.getId()) {
			throw new DuplicateRecordException("Complaint already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement ps = conn.prepareStatement(
					"update st_complaint set title=?,description=?,type=?,raised_by=?,assigned_to=?,priority=?,status=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			ps.setString(1, bean.getTitle());
			ps.setString(2, bean.getDescription());
			ps.setString(3, bean.getType());
			ps.setString(4, bean.getRaisedBy());
			ps.setString(5, bean.getAssignedTo());
			ps.setString(6, bean.getPriority());
			ps.setString(7, bean.getStatus());
			ps.setString(8, bean.getCreatedBy());
			ps.setString(9, bean.getModifiedBy());
			ps.setTimestamp(10, bean.getCreatedDatetime());
			ps.setTimestamp(11, bean.getModifiedDatetime());
			ps.setLong(12, bean.getId());

			ps.executeUpdate();
			conn.commit();
			ps.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
			}
			throw new ApplicationException("Exception in update");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	// Delete
	public void delete(ComplaintBean bean) throws ApplicationException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement ps = conn.prepareStatement("delete from st_complaint where id=?");
			ps.setLong(1, bean.getId());

			ps.executeUpdate();
			conn.commit();
			ps.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
			}
			throw new ApplicationException("Exception in delete");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	// Search
	public List<ComplaintBean> search(ComplaintBean bean, int pageNo, int pageSize) throws ApplicationException {

		List<ComplaintBean> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from st_complaint where 1=1");

		if (bean != null) {
			if (bean.getTitle() != null && bean.getTitle().length() > 0) {
				sql.append(" and title like '" + bean.getTitle() + "%'");
			}
			if (bean.getStatus() != null && bean.getStatus().length() > 0) {
				sql.append(" and status like '" + bean.getStatus() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintBean b = new ComplaintBean();

				b.setId(rs.getLong(1));
				b.setTitle(rs.getString(2));
				b.setDescription(rs.getString(3));
				b.setType(rs.getString(4));
				b.setRaisedBy(rs.getString(5));
				b.setAssignedTo(rs.getString(6));
				b.setPriority(rs.getString(7));
				b.setStatus(rs.getString(8));
				b.setCreatedBy(rs.getString(9));
				b.setModifiedBy(rs.getString(10));
				b.setCreatedDatetime(rs.getTimestamp(11));
				b.setModifiedDatetime(rs.getTimestamp(12));

				list.add(b);
			}

			rs.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	// List
	public List<ComplaintBean> list(int pageNo, int pageSize) throws ApplicationException {
		return search(null, pageNo, pageSize);
	}

}
