<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.BannerCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
    <title>Banner</title>
</head>
<body>

<form action="<%=ORSView.BANNER_CTL%>" method="post">

    <%@ include file="Header.jsp"%>

    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.BannerBean" scope="request"></jsp:useBean>

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
            Banner
        </h1>

        <!-- Success & Error Messages -->
        <h3><font color="green"><%=ServletUtility.getSuccessMessage(request)%></font></h3>
        <h3><font color="red"><%=ServletUtility.getErrorMessage(request)%></font></h3>

        <!-- Hidden Fields -->
        <input type="hidden" name="id" value="<%=bean.getId()%>">
        <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
        <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
        <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
        <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

        <table>

            <!-- Code -->
            <tr>
                <th align="left">Code<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="code" placeholder="Enter Code"
                        value="<%=DataUtility.getStringData(bean.getCode())%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("code", request)%></font>
                </td>
            </tr>

            <!-- Title -->
            <tr>
                <th align="left">Title<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="title" placeholder="Enter Title"
                        value="<%=DataUtility.getStringData(bean.getTitle())%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("title", request)%></font>
                </td>
            </tr>

            <!-- Image Path -->
            <tr>
                <th align="left">Image Path<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="imagePath" placeholder="Enter Image Path"
                        value="<%=DataUtility.getStringData(bean.getImagePath())%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("imagePath", request)%></font>
                </td>
            </tr>

            <!-- Status Dropdown -->
            <tr>
                <th align="left">Status<span style="color:red">*</span></th>
                <td>
                    <%
                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                        map.put("Active", "Active");
                        map.put("Inactive", "Inactive");

                        String htmlList = HTMLUtility.getList("status", bean.getStatus(), map);
                    %>
                    <%=htmlList%>
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("status", request)%></font>
                </td>
            </tr>

            <!-- Buttons -->
            <tr>
                <th></th>
                <td colspan="2">

                    <%
                        if (bean != null && bean.getId() > 0) {
                    %>
                        <input type="submit" name="operation" value="<%=BannerCtl.OP_UPDATE%>">
                        <input type="submit" name="operation" value="<%=BannerCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                        <input type="submit" name="operation" value="<%=BannerCtl.OP_SAVE%>">
                        <input type="submit" name="operation" value="<%=BannerCtl.OP_RESET%>">
                    <%
                        }
                    %>

                </td>
            </tr>

        </table>

    </div>

</form>

</body>
</html>