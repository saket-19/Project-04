```jsp
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.StockCtl"%>
<%@page import="in.co.rays.proj4.utility.DataUtility"%>
<%@page import="in.co.rays.proj4.utility.ServletUtility"%>

<html>
<head>
<title>Add Stock</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

	<form action="<%=ORSView.STOCK_CTL%>" method="POST">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.StockBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 align="center" style="margin-bottom: -15; color: navy">

				<%
				if (bean != null && bean.getStockId() > 0) {
				%>
				Update
				<%
				} else {
				%>
				Add
				<%
				}
				%>

				Stock

			</h1>

			<div style="height: 15px; margin-bottom: 12px">

				<h3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</h3>

				<h3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</h3>

			</div>

			<input type="hidden" name="id" value="<%=bean.getStockId()%>">
			<input type="hidden" name="createdBy"
				value="<%=bean.getCreatedBy()%>"> <input type="hidden"
				name="modifiedBy" value="<%=bean.getModifiedBy()%>"> <input
				type="hidden" name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>

				<tr>

					<th align="left">Stock Name<span style="color: red">*</span></th>

					<td><input type="text" name="stockName"
						placeholder="Enter Stock Name"
						value="<%=DataUtility.getStringData(bean.getStockName())%>">
					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("stockName", request)%>
					</font></td>

				</tr>

				<tr>

					<th align="left">Price<span style="color: red">*</span></th>

					<td><input type="text" name="price" placeholder="Enter Price"
						value="<%=bean.getPrice() > 0 ? bean.getPrice() : ""%>">
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("price", request)%>
					</font></td>

				</tr>

				<tr>

					<th align="left">Quantity<span style="color: red">*</span></th>

					<td><input type="text" name="quantity"
						placeholder="Enter Quantity"
						value="<%=bean.getQuantity() != null && bean.getQuantity() > 0 ? bean.getQuantity() : ""%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("quantity", request)%>
					</font></td>

				</tr>

				<tr>
					<th></th>
					<td></td>
				</tr>

				<tr>

					<th></th>

					<%
					if (bean != null && bean.getStockId() > 0) {
					%>

					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=StockCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=StockCtl.OP_CANCEL%>">

						<%
						} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=StockCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=StockCtl.OP_RESET%>">

						<%
						}
						%></td>

				</tr>

			</table>

		</div>

	</form>

</body>
</html>
```
