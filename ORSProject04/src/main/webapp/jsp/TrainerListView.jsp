<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.HTMLUtility"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.TrainerListCtl"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>
<%@page import="in.co.rays.proj4.bean.TrainerBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
    <title>Trainer List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>

    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.TrainerBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Trainer List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3>
                <font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
            </h3>
            <h3>
                <font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
            </h3>
        </div>

        <form action="<%=ORSView.TRAINER_LIST_CTL%>" method="post">

            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;

                int nextPageSize = 0;
                if (request.getAttribute("nextListSize") != null) {
                    nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
                }

                @SuppressWarnings("unchecked")
                List<TrainerBean> list = (List<TrainerBean>) ServletUtility.getList(request);
                Iterator<TrainerBean> it = list.iterator();
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <!-- 🔍 Search Section -->
            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Name :</b></label>
                        <input type="text" name="name" value="<%=DataUtility.getStringData(bean.getName())%>">

                        &emsp;&nbsp;

                        <label><b>Specialization :</b></label>
                        <input type="text" name="specialization" value="<%=DataUtility.getStringData(bean.getSpecialization())%>">

                        &emsp;&nbsp;

                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_SEARCH%>">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>

            <br>

            <!-- 📋 Table -->
            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="15%">Code</th>
                    <th width="20%">Name</th>
                    <th width="20%">Specialization</th>
                    <th width="20%">Contact Number</th>
                    <th width="10%">Edit</th>
                </tr>

                <%
                    if (list != null && list.size() > 0) {
                        while (it.hasNext()) {
                            bean = (TrainerBean) it.next();
                %>
                <tr>
                    <td align="center">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>">
                    </td>

                    <td align="center"><%=index++%></td>

                    <td align="center"><%=bean.getCode()%></td>

                    <td align="center" style="text-transform: capitalize;">
                        <%=bean.getName()%>
                    </td>

                    <td align="center"><%=bean.getSpecialization()%></td>

                    <td align="center"><%=bean.getContactNumber()%></td>

                    <td align="center">
                        <a href="TrainerCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </table>

            <!-- 🔽 Pagination -->
            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_PREVIOUS%>"
                            <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>

                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_NEW%>">
                    </td>

                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_DELETE%>">
                    </td>

                    <td align="right" style="width: 25%">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_NEXT%>"
                            <%=nextPageSize != 0 ? "" : "disabled"%>>
                    </td>
                </tr>
            </table>

            <%
                if (list == null || list.size() == 0) {
            %>
            <table>
                <tr>
                    <td align="right">
                        <input type="submit" name="operation" value="<%=TrainerListCtl.OP_BACK%>">
                    </td>
                </tr>
            </table>
            <%
                }
            %>

        </form>
    </div>
</body>
</html>