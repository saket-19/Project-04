<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.proj4.controller.LeadCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
    <title>Lead</title>
</head>
<body>

<form action="<%=ORSView.LEAD_CTL%>" method="post">

    <%@ include file="Header.jsp"%>

    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.LeadBean" scope="request"></jsp:useBean>

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
            Lead
        </h1>

        <!-- Success & Error -->
        <h3 style="color: green">
            <%=ServletUtility.getSuccessMessage(request)%>
        </h3>

        <h3 style="color: red">
            <%=ServletUtility.getErrorMessage(request)%>
        </h3>

        <!-- Hidden Fields (Audit) -->
        <input type="hidden" name="id" value="<%=bean.getId()%>">
        <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
        <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
       

        <table>

            <!-- Contact Name -->
            <tr>
                <th align="left">Contact Name<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="contactName" placeholder="Enter Contact Name"
                        value="<%=DataUtility.getStringData(bean.getContactName())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("contactName", request)%>
                    </font>
                </td>
            </tr>

            <!-- Mobile -->
            <tr>
                <th align="left">Mobile<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="mobile" placeholder="Enter Mobile"
                        value="<%=DataUtility.getStringData(bean.getMobile())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("mobile", request)%>
                    </font>
                </td>
            </tr>

            <!-- DOB -->
            <tr>
                <th align="left">Date of Birth<span style="color:red">*</span></th>
                <td>
                    <input type="date" name="dob"
                        value="<%=DataUtility.getDateString(bean.getDob())%>">
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("dob", request)%>
                    </font>
                </td>
            </tr>

            <!-- Status Dropdown -->
            <tr>
                <th align="left">Status<span style="color:red">*</span></th>
                <td>
                    <%
                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

                        map.put("New", "New");
                        map.put("In Progress", "In Progress");
                        map.put("Converted", "Converted");
                        map.put("Closed", "Closed");

                        String htmlList = HTMLUtility.getList("status", bean.getStatus(), map);
                    %>

                    <%=htmlList%>
                </td>
                <td style="position: fixed;">
                    <font color="red">
                        <%=ServletUtility.getErrorMessage("status", request)%>
                    </font>
                </td>
            </tr>

            <!-- Buttons -->
            <tr>
                <th></th>
                <%
                    if (bean != null && bean.getId() > 0) {
                %>
                    <td>
                        <input type="submit" name="operation" value="<%=LeadCtl.OP_UPDATE%>">
                        <input type="submit" name="operation" value="<%=LeadCtl.OP_CANCEL%>">
                    </td>
                <%
                    } else {
                %>
                    <td>
                        <input type="submit" name="operation" value="<%=LeadCtl.OP_SAVE%>">
                        <input type="submit" name="operation" value="<%=LeadCtl.OP_RESET%>">
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