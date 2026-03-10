<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.bean.MeetingBean"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.proj4.controller.BaseCtl"%>

<html>
<head>
    <title>Meeting List</title>
</head>
<body>
   
    <jsp:useBean id="bean" class="in.co.rays.proj4.bean.MeetingBean" scope="request"/>
    
    <div align="center">
        <h1 style="color: navy;">Meeting List</h1>

        <div>
            <h3 style="color: red;"><%=ServletUtility.getErrorMessage(request)%></h3>
            <h3 style="color: green;"><%=ServletUtility.getSuccessMessage(request)%></h3>
        </div>

        <form action="<%=ORSView.MEETING_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize") != null ? 
                                                        request.getAttribute("nextListSize").toString() : "0");

                @SuppressWarnings("unchecked")
                List<MeetingBean> list = (List<MeetingBean>) ServletUtility.getList(request);
                Iterator<MeetingBean> it = list != null ? list.iterator() : null;
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table>
                <tr>
                    <td>
                        <label>Title:</label>
                        <input type="text" name="title" value="<%=bean.getTitle()!=null ? bean.getTitle() : "" %>"/>
                        &nbsp;&nbsp;
                        <label>Code:</label>
                        <input type="text" name="code" value="<%=bean.getCode()!=null ? bean.getCode() : "" %>"/>
                        &nbsp;&nbsp;
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_SEARCH%>"/>
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_RESET%>"/>
                    </td>
                </tr>
            </table>
            <br/>

            <%
                if (list != null && list.size() > 0) {
            %>
            <table border="1" style="width: 100%; border-collapse: collapse;">
                <tr style="background-color: #e1e6f1;">
                    <th><input type="checkbox" id="selectall"/></th>
                    <th>S.No</th>
                    <th>Code</th>
                    <th>Title</th>
                    <th>Time</th>
                    <th>Location</th>
                    <th>Status</th>
                    <th>Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (MeetingBean) it.next();
                %>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>"/>
                    </td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center;"><%=bean.getCode()%></td>
                    <td style="text-align: center;"><%=bean.getTitle()%></td>
                    <td style="text-align: center;">
                        <%=bean.getTime() != null ? bean.getTime().toString() : "-" %>
                    </td>
                    <td style="text-align: center;"><%=bean.getLocation()%></td>
                    <td style="text-align: center;"><%=bean.getStatus()%></td>
                    <td style="text-align: center;">
                        <a href="MeetingCtl?id=<%=bean.getId()%>">Edit</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

            <table style="width: 100%; margin-top: 10px;">
                <tr>
                    <td style="width: 25%;">
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>/>
                    </td>
                    <td style="width: 25%; text-align: center;">
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_NEW%>"/>
                    </td>
                    <td style="width: 25%; text-align: center;">
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_DELETE%>"/>
                    </td>
                    <td style="width: 25%; text-align: right;">
                        <input type="submit" name="operation" value="<%=BaseCtl.OP_NEXT%>" <%=nextPageSize != 0 ? "" : "disabled"%>/>
                    </td>
                </tr>
            </table>
            <%
                } else {
            %>
            <p>No records found</p>
            <%
                }
            %>

        </form>
    </div>

    <script>
        // Select/Deselect all checkboxes
        document.getElementById('selectall').onclick = function() {
            var checkboxes = document.getElementsByClassName('case');
            for(var i=0; i<checkboxes.length; i++) {
                checkboxes[i].checked = this.checked;
            }
        }
    </script>
</body>
</html>