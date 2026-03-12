<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.bean.CourseBean"%>
<%@page import="in.co.rays.proj4.bean.SubjectBean"%>
<%@page import="in.co.rays.proj4.bean.TimeTableBean"%>
<%@page import="in.co.rays.proj4.controller.TimeTableCtl"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
<title>Add Timetable</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png">

</head>

<body>

	<form action="<%=ORSView.TIMETABLE_CTL%>" method="POST">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TimeTableBean"
			scope="request" />

		<%
		List<CourseBean> courseList = (List<CourseBean>) request.getAttribute("courseList");

		List<SubjectBean> subjectList = (List<SubjectBean>) request.getAttribute("subjectList");
		%>

		<div align="center">

			<h1 style="color: navy">

				<%
				if (bean != null && bean.getId() > 0) {
				%>

				Update Timetable

				<%
				} else {
				%>

				Add Timetable

				<%
				}
				%>

			</h1>

			<h3 style="color: green">
				<%=ServletUtility.getSuccessMessage(request)%>
			</h3>

			<h3 style="color: red">
				<%=ServletUtility.getErrorMessage(request)%>
			</h3>


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
					<th align="left">Course *</th>

					<td><%=HTMLUtility.getList("courseId", String.valueOf(bean.getCourseId()), courseList)%></td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("courseId", request)%>
					</font></td>

				</tr>


				<tr>
					<th align="left">Subject *</th>

					<td><%=HTMLUtility.getList("subjectId", String.valueOf(bean.getSubjectId()), subjectList)%></td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("subjectId", request)%>
					</font></td>

				</tr>


				<tr>
					<th align="left">Semester *</th>

					<td>
						<%
						HashMap<String, String> map = new HashMap<String, String>();

						map.put("1", "1");
						map.put("2", "2");
						map.put("3", "3");
						map.put("4", "4");
						map.put("5", "5");
						map.put("6", "6");
						map.put("7", "7");
						map.put("8", "8");
						%> <%=HTMLUtility.getList("semester", bean.getSemester(), map)%>

					</td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("semester", request)%>
					</font></td>

				</tr>


				<tr>
					<th align="left">Exam Date *</th>

					<td><input type="text" id="udate" name="examDate"
						placeholder="DD/MM/YYYY"
						value="<%=DataUtility.getDateString(bean.getExamDate())%>">

					</td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("examDate", request)%>
					</font></td>

				</tr>


				<tr>
					<th align="left">Exam Time *</th>

					<td>
						<%
						LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();

						map1.put("08:00 AM to 11:00 AM", "08:00 AM to 11:00 AM");
						map1.put("12:00 PM to 03:00 PM", "12:00 PM to 03:00 PM");
						map1.put("04:00 PM to 07:00 PM", "04:00 PM to 07:00 PM");
						%> <%=HTMLUtility.getList("examTime", bean.getExamTime(), map1)%>

					</td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("examTime", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Description *</th>

					<td><textarea name="description" rows="3" style="width: 170px">

<%=DataUtility.getStringData(bean.getDescription())%>

</textarea></td>

					<td><font color="red"> <%=ServletUtility.getErrorMessage("description", request)%>
					</font></td>

				</tr>


				<tr>

					<th></th>

					<td>
						<%
						if (bean != null && bean.getId() > 0) {
						%> <input type="submit" name="operation"
						value="<%=TimeTableCtl.OP_UPDATE%>"> <input type="submit"
						name="operation" value="<%=TimeTableCtl.OP_CANCEL%>"> <%
 } else {
 %> <input type="submit" name="operation"
						value="<%=TimeTableCtl.OP_SAVE%>"> <input type="submit"
						name="operation" value="<%=TimeTableCtl.OP_RESET%>"> <%
 }
 %>

					</td>

				</tr>

			</table>

		</div>

	</form>

</body>
</html>