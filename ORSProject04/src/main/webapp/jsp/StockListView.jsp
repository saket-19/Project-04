<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.StockListCtl"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.bean.StockBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Stock List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

	<%@include file="Header.jsp"%>

	<div align="center">

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.StockBean"
			scope="request"></jsp:useBean>

		<h1 align="center" style="margin-bottom: -15; color: navy;">Stock
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">

			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>

			<h3>
				<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>

		</div>

		<form action="<%=ORSView.STOCK_LIST_CTL%>" method="POST">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);

			int index = ((pageNo - 1) * pageSize) + 1;

			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			List<StockBean> list = (List<StockBean>) ServletUtility.getList(request);

			Iterator<StockBean> it = list.iterator();

			if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">

				<tr>

					<td align="center"><label><b>Stock Name :</b></label> <input
						type="text" name="stockName" placeholder="Enter Stock Name"
						value="<%=ServletUtility.getParameter("stockName", request)%>">

						<input type="submit" name="operation"
						value="<%=StockListCtl.OP_SEARCH%>"> <input type="submit"
						name="operation" value="<%=StockListCtl.OP_RESET%>"></td>

				</tr>

			</table>

			<br>

			<table border="1" style="width: 100%; border: groove;">

				<tr>

					<th width="5%"><input type="checkbox" id="selectall">
					</th>

					<th width="5%">S.No</th>

					<th width="30%">Stock Name</th>

					<th width="20%">Price</th>

					<th width="20%">Quantity</th>

					<th width="10%">Edit</th>

				</tr>

				<%
				while (it.hasNext()) {

					bean = it.next();
				%>

				<tr>

					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getStockId()%>"></td>

					<td style="text-align: center;"><%=index++%></td>

					<td style="text-align: center;"><%=bean.getStockName()%></td>

					<td style="text-align: center;"><%=bean.getPrice()%></td>

					<td style="text-align: center;"><%=bean.getQuantity()%></td>

					<td style="text-align: center;"><a
						href="StockCtl?id=<%=bean.getStockId()%>">Edit</a></td>

				</tr>

				<%
				}
				%>

			</table>

			<table style="width: 100%">

				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=StockListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=StockListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=StockListCtl.OP_DELETE%>"></td>

					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=StockListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>

				</tr>

			</table>

			<%
			}

			if (list.size() == 0) {
			%>

			<table>

				<tr>

					<td align="right"><input type="submit" name="operation"
						value="<%=StockListCtl.OP_BACK%>"></td>

				</tr>

			</table>

			<%
			}
			%>

		</form>

	</div>

</body>
</html>