package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.bean.ContactBean;
import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class ContactModel {
	public Integer nextPk() {
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_contact");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;

	}

	public Integer add(ContactBean bean) {
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_contact values(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getCity());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getMobile());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void delete(ContactBean bean) {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_contact where id=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void Update(ContactBean bean) {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("update st_contact set name = ?,city = ?,dob = ? ,mobile = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getCity());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getMobile());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		} finally {

			JDBCDataSource.closeConnection(conn);

		}
	}

	public List search(ContactBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(ContactBean bean, int pageNo, int PageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM ST_Contact WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			if (bean.getCity() != null && bean.getCity().length() > 0) {
				sql.append(" AND city like '" + bean.getCity() + "%'");
			}
			if (bean.getDob() != null) {
				sql.append(" AND dob like'" + bean.getDob() + "%'");
			}
			if (bean.getMobile() != null && bean.getMobile().length() > 0) {
				sql.append(" AND mobile like '" + bean.getMobile() + "%'");
			}

		}
		// if page size is greater than zero then apply pagination
		if (PageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * PageSize;
			sql.append(" Limit " + pageNo + "," + PageSize);

		}
		ArrayList list = new ArrayList();

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ContactBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCity(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobile(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search college");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public List<ContactBean> list() throws ApplicationException, DatabaseException {
		return search(null, 0, 0);
	}

	public List list(int pageNo, int pageSize) throws Exception {

		List list = new ArrayList();

		StringBuffer sql = new StringBuffer("select * from st_contact");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + " ," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();
			ContactBean bean;
			while (rs.next()) {
				bean = new ContactBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCity(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setMobile(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

				list.add(bean);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();

			throw new ApplicationException("Exception : Exception in getting lidt " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public ContactBean findByPK(long id) {
		Connection conn = null;
		ContactBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_contact WHERE id=?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = new ContactBean();

				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCity(rs.getString("city"));
				bean.setDob(rs.getDate("dob"));
				bean.setMobile(rs.getString("mobile"));

			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

}
