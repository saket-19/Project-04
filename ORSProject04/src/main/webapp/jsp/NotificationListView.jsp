<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.bean.NotificationBean"%>
<%@page import="in.co.rays.proj4.controller.NotificationListCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>

<jsp:useBean id="bean" class="in.co.rays.proj4.bean.NotificationBean"
	scope="request"></jsp:useBean>

<html>
<head>
<title>Notification List</title>
</head>
<body>

	<form action="<%=ORSView.NOTIFICATION_LIST_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<center>

			<h1 style="color: navy;">Notification List</h1>

			<!-- Success Message -->
			<h3 style="color: green;">
				<%=ServletUtility.getSuccessMessage(request)%>
			</h3>

			<!-- Error Message -->
			<h3 style="color: red;">
				<%=ServletUtility.getErrorMessage(request)%>
			</h3>

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			List<NotificationBean> list = (List<NotificationBean>) ServletUtility.getList(request);

			Iterator<NotificationBean> it = null;
			if (list != null) {
				it = list.iterator();
			}

			int nextPageSize = 0;
			if (request.getAttribute("nextListSize") != null) {
				nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
			}
			%>

			<!-- Search Panel -->
			<table>
				<tr>
					<td>Code :</td>
					<td><input type="text" name="code"
						value="<%=DataUtility.getStringData(bean.getCode())%>"
						placeholder="Enter Code"></td>

					<td>Sent To :</td>
					<td><input type="text" name="sentTo"
						value="<%=DataUtility.getStringData(bean.getSentTo())%>"
						placeholder="Enter Receiver"></td>

					<td><input type="submit" name="operation"
						value="<%=NotificationListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=NotificationListCtl.OP_RESET%>"></td>
				</tr>
			</table>

			<br>

			<!-- List Table -->
			<table border="1" width="100%" cellpadding="5">
				<tr style="background-color: lightgray;">
					<th>Select</th>
					<th>S.No</th>
					<th>Code</th>
					<th>Message</th>
					<th>Sent To</th>
					<th>Sent Time</th>
					<th>Status</th>
					<th>Edit</th>
				</tr>

				<%
				if (it != null) {
					while (it.hasNext()) {
						NotificationBean nbean = it.next();
				%>

				<tr align="center">

					<td><input type="checkbox" name="ids"
						value="<%=nbean.getId()%>"></td>

					<td><%=index++%></td>

					<td><%=DataUtility.getStringData(nbean.getCode())%></td>
					<td><%=DataUtility.getStringData(nbean.getMessage())%></td>
					<td><%=DataUtility.getStringData(nbean.getSentTo())%></td>
					<td><%=nbean.getSentTime()%></td>
					<td><%=DataUtility.getStringData(nbean.getStatus())%></td>
					<td><a
						href="<%=ORSView.NOTIFICATION_CTL%>?id=<%=nbean.getId()%>">Edit</a>
				</tr>

				<%
				}
				}
				%>

			</table>

			<br>

			<!-- Pagination -->
			<table width="100%">
				<tr>

					<td><input type="submit" name="operation"
						value="<%=NotificationListCtl.OP_PREVIOUS%>"
						<%=(pageNo == 1) ? "disabled" : ""%>></td>

					<td align="center"><input type="submit" name="operation"
						value="<%=NotificationListCtl.OP_NEW%>"> <input
						type="submit" name="operation"
						value="<%=NotificationListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						value="<%=NotificationListCtl.OP_NEXT%>"
						<%=(nextPageSize == 1) ? "disabled" : ""%>></td>

				</tr>
			</table>

			<!-- Hidden Fields -->
			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

		</center>

	</form>

</body>
</html>