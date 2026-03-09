<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.TrainingCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
    <title>Training</title>
</head>
<body>

<form action="<%=ORSView.TRAINING_CTL%>" method="post">

    <%@ include file="Header.jsp"%>

    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.TrainingBean" scope="request"></jsp:useBean>

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
            Training
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

            <!-- Name -->
            <tr>
                <th align="left">Name<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="name" placeholder="Enter Name"
                        value="<%=DataUtility.getStringData(bean.getName())%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("name", request)%></font>
                </td>
            </tr>

            <!-- Trainer Name -->
            <tr>
                <th align="left">Trainer Name<span style="color:red">*</span></th>
                <td>
                    <input type="text" name="trainerName" placeholder="Enter Trainer Name"
                        value="<%=DataUtility.getStringData(bean.getTrainerName())%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("trainerName", request)%></font>
                </td>
            </tr>

            <!-- Training Date (FIXED) -->
            <tr>
                <th align="left">Training Date<span style="color:red">*</span></th>
                <td>
                    <input type="date" name="date"
                        value="<%=bean.getDate() != null ? bean.getDate().toString() : ""%>">
                </td>
                <td>
                    <font color="red"><%=ServletUtility.getErrorMessage("date", request)%></font>
                </td>
            </tr>

            <!-- Buttons -->
            <tr>
                <th></th>
                <td colspan="2">

                    <%
                        if (bean != null && bean.getId() > 0) {
                    %>
                        <input type="submit" name="operation" value="<%=TrainingCtl.OP_UPDATE%>">
                        <input type="submit" name="operation" value="<%=TrainingCtl.OP_CANCEL%>">
                    <%
                        } else {
                    %>
                        <input type="submit" name="operation" value="<%=TrainingCtl.OP_SAVE%>">
                        <input type="submit" name="operation" value="<%=TrainingCtl.OP_RESET%>">
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