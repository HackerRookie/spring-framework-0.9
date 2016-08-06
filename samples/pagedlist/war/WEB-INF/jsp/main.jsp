<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/i21" prefix="i21" %>

<html>
<head>
  <title>Paged List</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <c:set var="css"><i21:theme code="css"/></c:set>
  <link rel="stylesheet" href="<c:url value="${css}"/>" type="text/css">
  <script language="javascript">
  function go(country) {
    var url = "<c:url value="/detail.html"/>?code=" + country;
    window.location = url;
  }
  </script>
</head>

<body>
	<h1>Paged List</h1>
  <ul>
    <li><a href="<c:url value="/welcome.html"><c:param name="theme" value="theme"/></c:url>">theme</a></li>
    <li><a href="<c:url value="/welcome.html"><c:param name="theme" value="metal"/></c:url>">metal</a></li>
  </ul>
  <ul>
    <c:set var="linkimg"><i21:theme code="img-fr"/></c:set>
    <li><a href="<c:url value="/welcome.html"><c:param name="locale" value="fr_FR"/></c:url>"><img src="<c:url value="${linkimg}"/>"></a></li>
    <c:set var="linkimg"><i21:theme code="img-de"/></c:set>
    <li><a href="<c:url value="/welcome.html"><c:param name="locale" value="de_GE"/></c:url>"><img src="<c:url value="${linkimg}"/>"></a></li>
    <c:set var="linkimg"><i21:theme code="img-en"/></c:set>
    <li><a href="<c:url value="/welcome.html"><c:param name="locale" value="en_US"/></c:url>"><img src="<c:url value="${linkimg}"/>"></a></li>
  </ul>
  <form action="<c:url value="/welcome.html"/>" method="POST">
	<table>
	  <tr>
	    <th><a href="<c:url value="/welcome.html"><c:param name="sort.property" value="name"/></c:url>"><fmt:message key="name"/></a></th>
	    <th><a href="<c:url value="/welcome.html"><c:param name="sort.property" value="code"/></c:url>"><fmt:message key="code"/></a></th>
	  </tr>
	  <tr>
	    <td><fmt:message key="filter"/><br>
	      <input type="text" name="filter.name" value="<c:out value="${countries.filter.name}"/>"
	      onchange="document.forms[0].submit()">
	    </td>
	    <td><fmt:message key="filter"/><br>
          <select name="filter.code" onchange="document.forms[0].submit()">
            <option></option>
            <c:forTokens items="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,W,X,Y,Z" delims="," var="crtcode">
              <option 
                <c:if test="${countries.filter.code == crtcode}">selected </c:if>
                value="<c:out value="${crtcode}"/>"><c:out value="${crtcode}"/>
              </option>
            </c:forTokens>
           </select>
	    </td>
	  </tr>
	  <c:forEach items="${countries.pageList}" var="cntry">
	    <tr onclick="go('<c:out value="${cntry.code}"/>')" 
	      onmouseover="this.style.cursor = 'hand';this.style.backgroundColor = '#dfdfff'" 
	      onmouseout="this.style.backgroundColor = ''">
	      <td><c:out value="${cntry.name}"/></td>
	      <td><c:out value="${cntry.code}"/></td>
	    </tr>
	  </c:forEach>
	</table>

	<hr>

    <div class="pagernav">
      <c:if test="${countries.nrOfPages > 1}">
          <a href="<c:url value="/welcome.html"><c:param name="page" value="0"/></c:url>">1</a>
          &nbsp;...&nbsp;
          <c:forEach begin="${countries.firstLinkedPage}" end="${countries.lastLinkedPage}" var="crtpg">
            <c:choose>
              <c:when test="${crtpg == countries.page}">
                <strong><c:out value="${crtpg + 1}"/></strong>
              </c:when>
              <c:otherwise>
                <a href="<c:url value="/welcome.html"><c:param name="page" value="${crtpg}"/></c:url>"><c:out value="${crtpg + 1}"/></a>
              </c:otherwise>
            </c:choose>
          </c:forEach>
          &nbsp;...&nbsp;
          <a href="<c:url value="/welcome.html"><c:param name="page" value="${countries.nrOfPages - 1}"/></c:url>"><c:out value="${countries.nrOfPages}"/></a>
	    <fmt:message key="psize" var="ps"/>
      </c:if>
    </div>
    <div class="psize">
        <select name="pageSize" onchange="document.forms[0].submit()">
          <c:forTokens items="5,10,15,20,30,40,80" delims="," var="crtps">
            <option 
              <c:if test="${countries.pageSize == crtps}">selected </c:if>
              value="<c:out value="${crtps}"/>"><c:out value="${crtps} ${ps}"/>
            </option>
          </c:forTokens>
        </select>
    </div>

    <hr>
    
    <div class="pager">
      <table>
        <tr>
          <td>
            <fmt:message key="pg.pages">
              <fmt:param value="${countries.page + 1}"/>
              <fmt:param value="${countries.nrOfPages}"/>
            </fmt:message>
          </td>
          <td align="center">
            <fmt:formatDate value="${countries.refreshDate}" type="both" dateStyle="short" timeStyle="medium"/>
          </td>
          <td align="right">
            <fmt:message key="pg.records">
              <fmt:param value="${countries.firstElementOnPage + 1}"/>
              <fmt:param value="${countries.lastElementOnPage + 1}"/>
              <fmt:param value="${countries.nrOfElements}"/>
            </fmt:message>
          </td>
        </tr>
      </table>
    </div>
    
    <hr>
    
    <fmt:message key="sort.crt"/>
    <c:out value="${countries.sort.property}"/>
    <c:choose>
      <c:when test="${countries.sort.ascending}"><fmt:message key="ascending"/></c:when>
      <c:otherwise><fmt:message key="descending"/></c:otherwise>
    </c:choose>

  </form>
</body>
</html>