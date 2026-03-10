<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.bean.TransportBean"%>

<html>
<head>
<title>Add Transport</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TransportBean"
		scope="request"></jsp:useBean>

	<center>

		<h1 style="color: #000080;">Add Transport</h1>

		<h3>
			<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
		</h3>

		<h3>
			<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
		</h3>

		<form action="<%=ORSView.TRANSPORT_CTL%>" method="post">

			<input type="hidden" name="id"
				value="<%=DataUtility.getStringData(bean.getId())%>">

			<table>

				<tr>
					<td><b>Vehicle Number</b><font color="red">*</font></td>
					<td><input type="text" name="vehicleNumber" size="30"
						placeholder="Enter Vehicle Number"
						value="<%=DataUtility.getStringData(bean.getVehicleNumber())%>">
					</td>
				</tr>

				<tr>
					<td><b>Driver Name</b><font color="red">*</font></td>
					<td><input type="text" name="driverName" size="30"
						placeholder="Enter Driver Name"
						value="<%=DataUtility.getStringData(bean.getDriverName())%>">
					</td>
				</tr>

				<tr>
					<td><b>Vehicle Type</b><font color="red">*</font></td>
					<td><input type="text" name="vehicleType" size="30"
						placeholder="Enter Vehicle Type"
						value="<%=DataUtility.getStringData(bean.getVehicleType())%>">
					</td>
				</tr>

				<tr>
					<td><b>Transport Status</b><font color="red">*</font></td>
					<td><input type="text" name="transportStatus" size="30"
						placeholder="Enter Transport Status"
						value="<%=DataUtility.getStringData(bean.getTransportStatus())%>">
					</td>
				</tr>

				<tr>
					<td></td>
					<td><input type="submit" name="operation" value="Save">
						<input type="submit" name="operation" value="Reset"></td>
				</tr>

			</table>

		</form>

	</center>

</body>
</html>