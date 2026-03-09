<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.LeadListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.LeadBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Lead List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>

<%@include file="Header.jsp"%>

<jsp:useBean id="bean" class="in.co.rays.proj4.bean.LeadBean" scope="request"></jsp:useBean>

<div align="center">

    <h1 align="center" style="color: navy;">Lead List</h1>

    <h3><font color="red"><%=ServletUtility.getErrorMessage(request)%></font></h3>
    <h3><font color="green"><%=ServletUtility.getSuccessMessage(request)%></font></h3>

    <form action="<%=ORSView.LEAD_LIST_CTL%>" method="post">

        <%
            int pageNo = ServletUtility.getPageNo(request);
            int pageSize = ServletUtility.getPageSize(request);
            int index = ((pageNo - 1) * pageSize) + 1;

            int nextPageSize = 0;
            if (request.getAttribute("nextListSize") != null) {
                nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
            }

            List list = ServletUtility.getList(request);
            Iterator it = list.iterator();
        %>

        <input type="hidden" name="pageNo" value="<%=pageNo%>">
        <input type="hidden" name="pageSize" value="<%=pageSize%>">

        <!-- Search Section -->
        <table width="100%">
            <tr>
                <td align="center">
                    <b>Contact Name :</b>
                    <input type="text" name="contactName" value="<%=DataUtility.getStringData(bean.getContactName())%>">

                    &emsp;

                    <b>Mobile :</b>
                    <input type="text" name="mobile" value="<%=DataUtility.getStringData(bean.getMobile())%>">

                    &emsp;

                    <b>Status :</b>
                    <input type="text" name="status" value="<%=DataUtility.getStringData(bean.getStatus())%>">

                    &emsp;

                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_SEARCH%>">
                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_RESET%>">
                </td>
            </tr>
        </table>

        <br>

        <!-- Table -->
        <table border="1" width="100%" style="border: groove;">
            <tr style="background-color: lightgray;">
                <th><input type="checkbox" id="selectall"></th>
                <th>S.No</th>
                <th>Contact Name</th>
                <th>Mobile</th>
                <th>Status</th>
                <th>DOB</th>
                <th>Edit</th>
            </tr>

            <%
                while (it.hasNext()) {
                    LeadBean lbean = (LeadBean) it.next();
            %>

            <tr>
                <td align="center">
                    <input type="checkbox" name="ids" value="<%=lbean.getId()%>">
                </td>
                <td align="center"><%=index++%></td>
                <td align="center"><%=lbean.getContactName()%></td>
                <td align="center"><%=lbean.getMobile()%></td>
                <td align="center"><%=lbean.getStatus()%></td>
                <td align="center"><%=DataUtility.getDateString(lbean.getDob())%></td>
                <td align="center">
                    <a href="LeadCtl?id=<%=lbean.getId()%>&operation=View">Edit</a>
                </td>
            </tr>

            <%
                }
            %>
        </table>

        <br>

        <!-- Buttons -->
        <table width="100%">
            <tr>
                <td>
                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_PREVIOUS%>" <%= (pageNo > 1) ? "" : "disabled" %>>
                </td>

                <td align="center">
                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_NEW%>">
                </td>

                <td align="center">
                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_DELETE%>">
                </td>

                <td align="right">
                    <input type="submit" name="operation" value="<%=LeadListCtl.OP_NEXT%>" <%= (nextPageSize > 0) ? "" : "disabled" %>>
                </td>
            </tr>
        </table>

    </form>

</div>

</body>
</html>