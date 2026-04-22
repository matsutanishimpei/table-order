<%@ page import="database.impl.*" %>
<%@ page import="java.util.*" %>
<%
    UserDAOImpl uDao = new UserDAOImpl();
    ProductDAOImpl pDao = new ProductDAOImpl();
    CategoryDAOImpl cDao = new CategoryDAOImpl();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DB Test</title>
</head>
<body>
    <h1>DB Data Test</h1>
    <ul>
        <li>Users: <%= uDao.findAll().size() %></li>
        <li>Products: <%= pDao.findAll().size() %></li>
        <li>Categories: <%= cDao.findAll().size() %></li>
    </ul>
</body>
</html>
