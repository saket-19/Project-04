<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.ComplaintCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
<title>Complaint</title>
</head>
<body>

	<form action="<%=ORSView.COMPLAINT_CTL%>" method="post">
		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.ComplaintBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 style="color: navy">
				<%
				if (bean != null && bean.getId() > 0) {
				%>
				Update Complaint
				<%
				} else {
				%>
				Add Complaint
				<%
				}
				%>
			</h1>

			<!-- Messages -->
			<h3 style="color: green;">
				<%=ServletUtility.getSuccessMessage(request)%>
			</h3>

			<h3 style="color: red;">
				<%=ServletUtility.getErrorMessage(request)%>
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

				<!-- Title -->
				<tr>
					<th align="left">Title<span style="color: red">*</span></th>
					<td><input type="text" name="title" placeholder="Enter Title"
						value="<%=DataUtility.getStringData(bean.getTitle())%>"></td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("title", request)%>
					</font></td>
				</tr>

				<!-- Description -->
				<tr>
					<th align="left">Description<span style="color: red">*</span></th>
					<td><textarea name="description" rows="3"
							style="width: 170px;">
<%=DataUtility.getStringData(bean.getDescription())%>
</textarea></td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("description", request)%>
					</font></td>
				</tr>

				<!-- Type -->
				<tr>
					<th align="left">Type<span style="color: red">*</span></th>
					<td>
						<%
						LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
						typeMap.put("Technical", "Technical");
						typeMap.put("Service", "Service");
						typeMap.put("Other", "Other");

						String typeList = HTMLUtility.getList("type", bean.getType(), typeMap);
						%> <%=typeList%>
					</td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("type", request)%>
					</font></td>
				</tr>

				<!-- Raised By -->
				<tr>
					<th align="left">Raised By<span style="color: red">*</span></th>
					<td><input type="text" name="raisedBy"
						value="<%=DataUtility.getStringData(bean.getRaisedBy())%>">
					</td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("raisedBy", request)%>
					</font></td>
				</tr>

				<!-- Assigned To -->
				<tr>
					<th align="left">Assigned To</th>
					<td><input type="text" name="assignedTo"
						value="<%=DataUtility.getStringData(bean.getAssignedTo())%>">
					</td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("assignedTo", request)%>
					</font></td>
				</tr>

				<!-- Priority -->
				<tr>
					<th align="left">Priority<span style="color: red">*</span></th>
					<td>
						<%
						LinkedHashMap<String, String> pMap = new LinkedHashMap<>();
						pMap.put("Low", "Low");
						pMap.put("Medium", "Medium");
						pMap.put("High", "High");

						String pList = HTMLUtility.getList("priority", bean.getPriority(), pMap);
						%> <%=pList%>
					</td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("priority", request)%>
					</font></td>
				</tr>

				<!-- Status -->
				<tr>
					<th align="left">Status<span style="color: red">*</span></th>
					<td>
						<%
						LinkedHashMap<String, String> sMap = new LinkedHashMap<>();
						sMap.put("Open", "Open");
						sMap.put("In Progress", "In Progress");
						sMap.put("Closed", "Closed");

						String sList = HTMLUtility.getList("status", bean.getStatus(), sMap);
						%> <%=sList%>
					</td>
					<td><font color="red"> <%=ServletUtility.getErrorMessage("status", request)%>
					</font></td>
				</tr>

				<!-- Buttons -->
				<tr>
					<th></th>

					<%
					if (bean != null && bean.getId() > 0) {
					%>
					<td><input type="submit" name="operation"
						value="<%=ComplaintCtl.OP_UPDATE%>"> <input type="submit"
						name="operation" value="<%=ComplaintCtl.OP_CANCEL%>"></td>
					<%
					} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=ComplaintCtl.OP_SAVE%>"> <input type="submit"
						name="operation" value="<%=ComplaintCtl.OP_RESET%>"></td>
					<%
					}
					%>

				</tr>

			</table>
		</div>
	</form>

</body>
</html>