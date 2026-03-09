<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.bean.TrainingBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Training List</title>
</head>

<body>

	<h1 align="center">Training List</h1>

	<form action="<%=ORSView.TRAINING_LIST_CTL%>" method="post">

		<%
			List list = ServletUtility.getList(request);
			Iterator it = list.iterator();
		%>

		<table border="1" width="100%">
			<tr>
				<th>Select</th>
				<th>ID</th>
				<th>Code</th>
				<th>Name</th>
				<th>Trainer</th>
				<th>Date</th>
				<th>Edit</th>
			</tr>

			<%
				while (it.hasNext()) {
					TrainingBean bean = (TrainingBean) it.next();
			%>

			<tr>
				<td align="center">
					<input type="checkbox" name="ids" value="<%=bean.getId()%>">
				</td>

				<td align="center"><%=bean.getId()%></td>

				<td align="center"><%=bean.getCode()%></td>

				<td align="center"><%=bean.getName()%></td>

				<td align="center"><%=bean.getTrainerName()%></td>

				<td align="center"><%=bean.getDate()%></td>

				<td align="center">
					<a href="<%=ORSView.TRAINING_CTL%>?id=<%=bean.getId()%>">Edit</a>
				</td>
			</tr>

			<%
				}
			%>

		</table>

		<br>

		<table width="100%">
			<tr>
				<td>
					<input type="submit" name="operation" value="Previous">
				</td>

				<td align="center">
					<input type="submit" name="operation" value="New">
				</td>

				<td align="right">
					<input type="submit" name="operation" value="Next">
				</td>
			</tr>
		</table>

	</form>

</body>
</html>