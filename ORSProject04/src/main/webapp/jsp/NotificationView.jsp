<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.NotificationCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
<title>Notification</title>
</head>
<body>

	<form action="<%=ORSView.NOTIFICATION_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.NotificationBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 style="color: navy">
				<%
				if (bean != null) {
				%>
				Update
				<%
				} else {
				%>
				Add
				<%
				}
				%>
				Notification
			</h1>

			<!-- Messages -->
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>

			<!-- Hidden Fields -->
			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>

				<tr>
					<th align="left">Code *</th>
					<td><input type="text" name="code"
						value="<%=DataUtility.getStringData(bean.getCode())%>"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("code", request)%></font></td>
				</tr>

				<tr>
					<th align="left">Message *</th>
					<td><textarea name="message" rows="3">
<%=DataUtility.getStringData(bean.getMessage())%>
</textarea></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("message", request)%></font></td>
				</tr>

				<tr>
					<th align="left">Sent To *</th>
					<td><input type="text" name="sentTo"
						value="<%=DataUtility.getStringData(bean.getSentTo())%>">
					</td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("sentTo", request)%></font></td>
				</tr>

				<tr>
					<th align="left">Sent Time</th>
					<td><input type="datetime-local" name="sentTime"
						value="<%=(bean.getSentTime() != null) ? bean.getSentTime().toString().replace(" ", "T") : ""%>">
					</td>
				</tr>

				<tr>
					<th align="left">Status *</th>
					<td>
						<%
						LinkedHashMap<String, String> map = new LinkedHashMap();
						map.put("Active", "Active");
						map.put("Inactive", "Inactive");
						String htmlList = HTMLUtility.getList("status", bean.getStatus(), map);
						%> <%=htmlList%>
					</td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("status", request)%></font></td>
				</tr>

				<tr>
					<th></th>
					<td>
						<%
						if (bean.getId() > 0) {
						%> <input type="submit" name="operation"
						value="<%=NotificationCtl.OP_UPDATE%>"> <input
						type="submit" name="operation"
						value="<%=NotificationCtl.OP_CANCEL%>"> <%
 } else {
 %> <input type="submit" name="operation"
						value="<%=NotificationCtl.OP_SAVE%>"> <input type="submit"
						name="operation" value="<%=NotificationCtl.OP_RESET%>"> <%
 }
 %>
					</td>
				</tr>

			</table>

		</div>

	</form>

</body>
</html>