<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.TrainerCtl"%>

<html>
<head>
<title>Trainer</title>
</head>
<body>
	<form action="<%=ORSView.TRAINER_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TrainerBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 style="color: navy">
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
				Trainer
			</h1>

			<!-- Success & Error Messages -->
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

				<!-- Code -->
				<tr>
					<th align="left">Code<span style="color: red">*</span></th>
					<td><input type="text" name="code"
						placeholder="Enter Trainer Code"
						value="<%=DataUtility.getStringData(bean.getCode())%>"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("code", request)%></font>
					</td>
				</tr>

				<!-- Name -->
				<tr>
					<th align="left">Name<span style="color: red">*</span></th>
					<td><input type="text" name="name"
						placeholder="Enter Trainer Name"
						value="<%=DataUtility.getStringData(bean.getName())%>"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("name", request)%></font>
					</td>
				</tr>

				<!-- Specialization -->
				<tr>
					<th align="left">Specialization<span style="color: red">*</span></th>
					<td><input type="text" name="specialization"
						placeholder="Enter Specialization"
						value="<%=DataUtility.getStringData(bean.getSpecialization())%>">
					</td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("specialization", request)%></font>
					</td>
				</tr>

				<!-- Contact Number -->
				<tr>
					<th align="left">Contact No<span style="color: red">*</span></th>
					<td><input type="text" name="contactNumber"
						placeholder="Enter Contact Number"
						value="<%=DataUtility.getStringData(bean.getContactNumber())%>">
					</td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("contactNumber", request)%></font>
					</td>
				</tr>

				<tr>
					<td></td>
				</tr>

				<!-- Buttons -->
				<tr>
					<th></th>
					<%
					if (bean != null && bean.getId() > 0) {
					%>
					<td colspan="2"><input type="submit" name="operation"
						value="<%=TrainerCtl.OP_UPDATE%>"> <input type="submit"
						name="operation" value="<%=TrainerCtl.OP_CANCEL%>"></td>
					<%
					} else {
					%>
					<td colspan="2"><input type="submit" name="operation"
						value="<%=TrainerCtl.OP_SAVE%>"> <input type="submit"
						name="operation" value="<%=TrainerCtl.OP_RESET%>"></td>
					<%
					}
					%>
				</tr>

			</table>
		</div>
	</form>
</body>
</html>