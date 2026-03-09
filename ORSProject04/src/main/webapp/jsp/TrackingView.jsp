<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.TrackingCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Tracking</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

	<form action="<%=ORSView.TRACKING_CTL%>" method="post">

		<%@include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TrackingBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 align="center" style="margin-bottom: -15; color: navy">

				<%
				if (bean != null && bean.getId() > 0) {
				%>
				Update
				<%
				} else {
				%>
				Add
				<%
				}
				%>
				Tracking
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<h3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</h3>

				<h3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</h3>
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
					<th align="left">Tracking Number<span style="color: red">*</span></th>
					<td align="center"><input type="text" name="number"
						placeholder="Enter Tracking Number"
						value="<%=DataUtility.getStringData(bean.getNumber())%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("number", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="left">Current Location<span style="color: red">*</span></th>
					<td align="center"><input type="text" name="currentLocation"
						placeholder="Enter Current Location"
						value="<%=DataUtility.getStringData(bean.getCurrentLocation())%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("currentLocation", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="left">Status<span style="color: red">*</span></th>
					<td align="center"><select name="status" style="width: 170px;">
							<option value="">--Select Status--</option>

							<option value="In Transit"
								<%="In Transit".equals(bean.getStatus()) ? "selected" : ""%>>
								In Transit</option>

							<option value="Delivered"
								<%="Delivered".equals(bean.getStatus()) ? "selected" : ""%>>
								Delivered</option>

							<option value="Pending"
								<%="Pending".equals(bean.getStatus()) ? "selected" : ""%>>
								Pending</option>

							<option value="Cancelled"
								<%="Cancelled".equals(bean.getStatus()) ? "selected" : ""%>>
								Cancelled</option>

					</select></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("status", request)%>
					</font></td>
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
						name="operation" value="<%=TrackingCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=TrackingCtl.OP_CANCEL%>">
					</td>
					<%
					} else {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=TrackingCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=TrackingCtl.OP_RESET%>">
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