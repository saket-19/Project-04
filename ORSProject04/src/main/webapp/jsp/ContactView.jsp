<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="in.co.rays.proj4.bean.ContactBean"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

</head>

<body>

	<%
	ContactBean bean = (ContactBean) request.getAttribute("bean");

	if (bean == null) {
		bean = new ContactBean();
	}
	%>
	<center>
		
			<h1 style="color: navy;">Add Contact</h1>
			<h3 align="center">
				<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>

			<h3 align="center">
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>


			<form action="<%=ORSView.CONTACT_CTL%>" method="post">


					<table>

						<tr>
							<th align="left">Name<span style="color: red">*</span></th>
							<td align="center"><input type="text" name="name"
								placeholder="Enter Contact Name"
								value="<%=DataUtility.getStringData(bean.getName())%>"></td>
							<td><font color="red"> <%=ServletUtility.getErrorMessage("name", request)%>
							</font></td>
						</tr>

						<tr>
							<th align="left">City<span style="color: red">*</span></th>
							<td align="center"><textarea
									style="width: 170px; resize: none;" name="city" rows="3"
									placeholder="Enter city"><%=DataUtility.getStringData(bean.getCity())%></textarea>
							</td>
							<td><font color="red"> <%=ServletUtility.getErrorMessage("city", request)%>
							</font></td>
						</tr>

						<tr>
							<th align="left">Date of Birth<span style="color: red">*</span></th>
							<td align="center"><input type="date" name="dob"
								value="<%=DataUtility.getDateString(bean.getDob())%>"></td>
							<td><font color="red"> <%=ServletUtility.getErrorMessage("dob", request)%>
							</font></td>
						</tr>

						<tr>
							<th align="left">Mobile<span style="color: red">*</span></th>
							<td align="center"><input type="text" name="mobile"
								placeholder="Enter your mobile no"
								value="<%=DataUtility.getStringData(bean.getMobile())%>"></td>
							<td><font color="red"> <%=ServletUtility.getErrorMessage("mobile", request)%>
							</font></td>
						</tr>

						<tr>
							<td></td>
							<td align="center"><input type="submit" name="operation"
								value="Submit"> <input type="reset" value="Reset"></td>
						</tr>

					</table>

				</form>
		</center>
</body>
</html>
