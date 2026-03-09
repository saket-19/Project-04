<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.BannerListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.BannerBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Banner List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.BannerBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">Banner
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.BANNER_LIST_CTL%>" method="post">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			int nextPageSize = 0;
			if (request.getAttribute("nextListSize") != null) {
				nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
			}

			List<BannerBean> list = (List<BannerBean>) ServletUtility.getList(request);
			Iterator<BannerBean> it = list.iterator();
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<!-- 🔍 Search -->
			<table style="width: 100%">
				<tr>
					<td align="center"><label><b>Title :</b></label> <input
						type="text" name="title"
						value="<%=DataUtility.getStringData(bean.getTitle())%>">

						&emsp;&nbsp; <input type="submit" name="operation"
						value="<%=BannerListCtl.OP_SEARCH%>"> <input type="submit"
						name="operation" value="<%=BannerListCtl.OP_RESET%>"></td>
				</tr>
			</table>

			<br>

			<!-- 📋 Table -->
			<table border="1" style="width: 100%; border: groove;">

				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall"></th>
					<th width="5%">S.No</th>
					<th width="15%">Code</th>
					<th width="20%">Title</th>
					<th width="25%">Image</th>
					<th width="15%">Status</th>
					<th width="10%">Edit</th>
				</tr>

				<%
				while (it.hasNext()) {
					bean = it.next();
				%>

				<tr>
					<td align="center"><input type="checkbox" class="case"
						name="ids" value="<%=bean.getId()%>"></td>

					<td align="center"><%=index++%></td>

					<td align="center"><%=bean.getCode()%></td>

					<td align="center"><%=bean.getTitle()%></td>

					<td align="center"><%=bean.getImagePath()%></td>

					<td align="center"><%=bean.getStatus()%></td>

					<td align="center"><a
						href="<%=ORSView.BANNER_CTL%>?id=<%=bean.getId()%>">Edit</a></td>
				</tr>

				<%
				}
				%>

			</table>

			<br>

			<!-- ⏮ Navigation -->
			<table style="width: 100%">

				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=BannerListCtl.OP_PREVIOUS%>"
						<%=(pageNo > 1) ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=BannerListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=BannerListCtl.OP_DELETE%>"></td>
					<td align="right" style="width: 25%"><input type="submit"
						name="operation" value="<%=BannerListCtl.OP_NEXT%>"
						<%=(list.size() < 10) ? "disabled" : ""%>></td>
				</tr>

			</table>

		</form>

	</div>

</body>
</html>