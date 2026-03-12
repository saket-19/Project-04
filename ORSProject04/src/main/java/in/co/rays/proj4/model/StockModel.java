package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.StockBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.utility.JDBCDataSource;

public class StockModel {

	public Integer nextPK() throws ApplicationException {

		int pk = 0;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT MAX(stock_id) FROM st_stock");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting PK");
		}

		return pk + 1;
	}

	public long add(StockBean bean) throws ApplicationException {

		int pk = nextPK();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement("INSERT INTO st_stock VALUES(?,?,?,?,?,?,?,?)");

			ps.setLong(1, pk);
			ps.setString(2, bean.getStockName());
			ps.setDouble(3, bean.getPrice());
			ps.setInt(4, bean.getQuantity());
			ps.setString(5, bean.getCreatedBy());
			ps.setString(6, bean.getModifiedBy());
			ps.setTimestamp(7, bean.getCreatedDatetime());
			ps.setTimestamp(8, bean.getModifiedDatetime());

			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Exception in Add Stock");
		}

		return pk;
	}

	public void update(StockBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement(
					"UPDATE st_stock SET stock_name=?, price=?, quantity=?, modified_by=?, modified_datetime=? WHERE stock_id=?");

			ps.setString(1, bean.getStockName());
			ps.setDouble(2, bean.getPrice());
			ps.setInt(3, bean.getQuantity());
			ps.setString(4, bean.getModifiedBy());
			ps.setTimestamp(5, bean.getModifiedDatetime());
			ps.setLong(6, bean.getStockId());

			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Exception in update Stock");
		}
	}

	public void delete(StockBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement("DELETE FROM st_stock WHERE stock_id=?");

			ps.setLong(1, bean.getStockId());
			ps.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException("Exception in delete Stock");
		}
	}

	public StockBean findByPK(long pk) throws ApplicationException {

		StockBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM st_stock WHERE stock_id=?");

			ps.setLong(1, pk);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				bean = new StockBean();

				bean.setStockId(rs.getLong(1));
				bean.setStockName(rs.getString(2));
				bean.setPrice(rs.getDouble(3));
				bean.setQuantity(rs.getInt(4));

			}

		} catch (Exception e) {
			throw new ApplicationException("Exception in findByPK Stock");
		}

		return bean;
	}

	public List search(StockBean bean, int pageNo, int pageSize) throws ApplicationException {

		List list = new ArrayList();
		Connection conn = null;

		StringBuffer sql = new StringBuffer("SELECT * FROM st_stock WHERE 1=1");

		if (bean != null) {

			if (bean.getStockId() > 0) {
				sql.append(" AND id = " + bean.getStockId());
			}

			if (bean.getStockName() != null && bean.getStockName().length() > 0) {
				sql.append(" AND stock_name LIKE '" + bean.getStockName() + "%'");
			}
		}

		// Pagination
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT " + pageNo + "," + pageSize);
		}

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				StockBean bean1 = new StockBean();

				bean1.setStockId(rs.getLong("id"));
				bean1.setStockName(rs.getString("stock_name"));
				bean1.setPrice(rs.getDouble("price"));
				bean1.setQuantity(rs.getInt("quantity"));

				list.add(bean1);
			}

			rs.close();

		} catch (Exception e) {

			e.printStackTrace();
			throw new ApplicationException("Exception in search Stock");

		} finally {

			JDBCDataSource.closeConnection(conn);

		}

		return list;
	}
	public List search(StockBean bean) throws ApplicationException {

	    return search(bean, 0, 0);
	}

}