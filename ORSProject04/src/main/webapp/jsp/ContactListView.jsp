<%@page import="in.co.rays.proj4.utility.DataValidator"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.ContactListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.ContactBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Contact List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<%@include file="Header.jsp"%>
	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.ContactBean"
		scope="request"></jsp:useBean>

	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;">Contact
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.CONTACT_LIST_CTL%>" method="post">
			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;
			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			@SuppressWarnings("unchecked")
			List<ContactBean> contactList = (List<ContactBean>) request.getAttribute("contactList");

			@SuppressWarnings("unchecked")
			List<ContactBean> list = (List<ContactBean>) ServletUtility.getList(request);
			Iterator<ContactBean> it = list.iterator();

			if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">
				<tr>
					<td align="center">Name : <input type="text" name="name"
						value="<%=ServletUtility.getParameter("name", request)%>">

						&emsp; City : <input type="text" name="city"
						value="<%=ServletUtility.getParameter("city", request)%>">

						&emsp; <input type="submit" name="operation"
						value="<%=ContactListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation"
						value="<%=ContactListCtl.OP_RESET%>">

					</td>
				</tr>
			</table>

			<br>

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall" /></th>
					<th width="5%">S.No</th>
					<th width="25%">Name</th>
					<th width="15%">City</th>
					<th width="45%">Dob</th>
					<th width="45%">Mobile</th>
					<th width="5%">Edit</th>
				</tr>

				<%
				while (it.hasNext()) {
					bean = (ContactBean) it.next();
				%>
				<tr>
					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getId()%>"></td>
					<td style="text-align: center;"><%=index++%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getName()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getCity()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getDob()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getMobile()%></td>

					<td style="text-align: center;">
						<a href="<%=ORSView.CONTACT_CTL%>?id=<%=bean.getId()%>">Edit</a></tr>
				<%
				}
				%>
			</table>

			<table style="width: 100%">
				<tr>
					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=ContactListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=ContactListCtl.OP_NEW%>"></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=ContactListCtl.OP_DELETE%>"></td>
					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=ContactListCtl.OP_NEXT%>"
						<%=nextPageSize != 0 ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
			}
			if (list.size() == 0) {
			%>
			<table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=ContactListCtl.OP_BACK%>"></td>
				</tr>
			</table>
			<%
			}
			%>
		</form>
	</div>
</body>
</html>