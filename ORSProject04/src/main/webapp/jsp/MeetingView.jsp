<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.MeetingCtl"%>
<%@page import="in.co.rays.proj4.bean.MeetingBean"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
<title>Add Meeting</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>

	<form action="<%=ORSView.MEETING_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.MeetingBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 align="center" style="color: navy">
				<%
				if (bean != null && bean.getId() > 0) {
				%>
				Update Meeting
				<%
				} else {
				%>
				Add Meeting
				<%
				}
				%>
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</H3>
				<H3 align="center">
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
				</H3>
			</div>

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
					<th align="left">Code<span style="color: red">*</span></th>
					<td><input type="text" name="code" placeholder="Enter Code"
						value="<%=DataUtility.getStringData(bean.getCode())%>"></td>
					<td style="position: fixed;"><font color="red"><%=ServletUtility.getErrorMessage("code", request)%></font>
					</td>
				</tr>

				<tr>
					<th align="left">Title<span style="color: red">*</span></th>
					<td><input type="text" name="title" placeholder="Enter Title"
						value="<%=DataUtility.getStringData(bean.getTitle())%>"></td>
					<td style="position: fixed;"><font color="red"><%=ServletUtility.getErrorMessage("title", request)%></font>
					</td>
				</tr>

				<tr>
					<th align="left">Time<span style="color: red">*</span></th>
					<td><input type="datetime-local" name="time"
						value="<%=bean.getTime() != null ? bean.getTime().toString().replace(" ", "T") : ""%>">
					</td>
					<td style="position: fixed;"><font color="red"><%=ServletUtility.getErrorMessage("time", request)%></font>
					</td>
				</tr>

				<tr>
					<th align="left">Location<span style="color: red">*</span></th>
					<td><input type="text" name="location"
						placeholder="Enter Location"
						value="<%=DataUtility.getStringData(bean.getLocation())%>"></td>
					<td style="position: fixed;"><font color="red"><%=ServletUtility.getErrorMessage("location", request)%></font>
					</td>
				</tr>

				<tr>
					<th align="left">Status<span style="color: red">*</span></th>
					<td><select name="status">
							<option value="Scheduled"
								<%=("Scheduled".equals(bean.getStatus())) ? "selected" : ""%>>Scheduled</option>
							<option value="Completed"
								<%=("Completed".equals(bean.getStatus())) ? "selected" : ""%>>Completed</option>
							<option value="Cancelled"
								<%=("Cancelled".equals(bean.getStatus())) ? "selected" : ""%>>Cancelled</option>
					</select></td>
					<td style="position: fixed;"><font color="red"><%=ServletUtility.getErrorMessage("status", request)%></font>
					</td>
				</tr>

				<tr>
					<th></th>
					<td></td>
				</tr>

				<tr>
					<th></th>

					<%
					if (bean != null && bean.getId() > 0) {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=MeetingCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=MeetingCtl.OP_CANCEL%>">
					</td>
					<%
					} else {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=MeetingCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=MeetingCtl.OP_RESET%>">
					</td>
					<%
					}
					%>

				</tr>

			</table>

		</div>

	</form>

</body>
</html>