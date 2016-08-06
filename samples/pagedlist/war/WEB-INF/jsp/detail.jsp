<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/i21" prefix="i21" %>

<html>
<head>
  <title>Paged List - Detail</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <c:set var="css"><i21:theme code="css"/></c:set>
  <link rel="stylesheet" href="<c:url value="${css}"/>" type="text/css">
</head>

<body>
	<h1>Detail</h1>
	<strong><fmt:message key="code"/>:</strong> <c:out value="${country.code}"/><br>
	<strong><fmt:message key="name"/>:</strong> <c:out value="${country.name}"/><br>
      <c:set var="linkimg"><i21:theme code="img-back"/></c:set>
      <div align="center">
	  <a href="javascript:history.go(-1)"><img src="<c:url value="${linkimg}"/>"></a>
      </div>
</body>
</html>