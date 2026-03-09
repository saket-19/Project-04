package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.co.rays.proj4.bean.OfferBean;
import in.co.rays.proj4.utility.JDBCDataSource;

public class OfferModel {
	public void add(OfferBean bean) throws SQLException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_offer values(?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, bean.getOfferId());
			pstmt.setString(2, bean.getOfferCode());
			pstmt.setFloat(3,bean.getDiscountAmount());
			pstmt.setDate(4,bean.getExpiryDate());
			pstmt.setString(5,bean.getOfferStatus());
		    pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			
		} catch (Exception e) {
			conn.rollback();	
	}
	}
	
}

