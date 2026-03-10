<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.TransportListCtl"%>
<%@page import="in.co.rays.proj4.bean.TransportBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Transport List</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TransportBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 style="color: navy;">Transport List</h1>

		<h3>
			<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
		</h3>

		<h3>
			<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
		</h3>

		<form action="<%=ORSView.TRANSPORT_LIST_CTL%>" method="post">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);

			int index = ((pageNo - 1) * pageSize) + 1;

			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			List<TransportBean> list = (List<TransportBean>) ServletUtility.getList(request);

			Iterator<TransportBean> it = list.iterator();
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table width="100%">

				<tr>
					<td align="center"><label><b>Vehicle Number :</b></label> <input
						type="text" name="vehicleNumber"
						value="<%=DataUtility.getStringData(bean.getVehicleNumber())%>">

						<input type="submit" name="operation"
						value="<%=TransportListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=TransportListCtl.OP_RESET%>"></td>
				</tr>

			</table>

			<br>

			<table border="1" width="100%" style="border-collapse: collapse;">

				<tr style="background-color: #e1e6f1">

					<th><input type="checkbox" id="selectall"></th>
					<th>S.No</th>
					<th>Vehicle Number</th>
					<th>Driver Name</th>
					<th>Vehicle Type</th>
					<th>Status</th>
					<th>Edit</th>

				</tr>

				<%
				while (it.hasNext()) {

					bean = it.next();
				%>

				<tr>

					<td align="center"><input type="checkbox" class="case"
						name="ids" value="<%=bean.getId()%>"></td>

					<td align="center"><%=index++%></td>

					<td align="center"><%=bean.getVehicleNumber()%></td>

					<td align="center"><%=bean.getDriverName()%></td>

					<td align="center"><%=bean.getVehicleType()%></td>

					<td align="center"><%=bean.getTransportStatus()%></td>

					<td align="center"><a href="TransportCtl?id=<%=bean.getId()%>">Edit</a>
					</td>

				</tr>

				<%
				}
				%>

			</table>

			<br>

			<table width="100%">

				<tr>

					<td><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center"><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_NEW%>"></td>

					<td align="center"><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_NEXT%>"
						<%=nextPageSize != 0 ? "" : "disabled"%>></td>

				</tr>

			</table>

		</form>

	</div>

</body>
</html>