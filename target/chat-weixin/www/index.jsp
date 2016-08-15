<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
  <title></title>
  <link href="css/ionic.app.css" rel="stylesheet">
  <script src="lib/ionic/js/jquery/jquery.min.js"></script>
  <!-- ionic/angularjs js -->
  <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
  <script src="/www/lib/ionic/js/ionic.bundle.js"></script>
  <!-- cordova script (this will be a 404 during development) -->
  <script src="cordova.js"></script>
  <!-- your app's js -->
  <script src="/www/js/app.js"></script>
  <script src="/www/js/controller/controllers.js"></script>
  <script src="/www/js/controller/chatController.js"></script>
  <script src="/www/js/controller/messageController.js"></script>
  <script src="/www/js/controller/settingController.js"></script>
  <script src="/www/js/route/routes.js"></script>
  <script src="/www/js/service/services.js"></script>
  <script src="/www/js/service/settingService.js"></script>
  <script src="/www/js/service/dateService.js"></script>
  <script src="/www/js/service/chatService.js"></script>
  <script src="/www/js/service/messageService.js"></script>
  <script src="/www/js/directive/directives.js"></script>
  <script src="/www/js/other/elastic.js"></script>
  <c:set var="ctx" value="${pageContext.request.contextPath}"/>
  <c:set var="userId" value="${sessionScope.SESSION_USERNAME.id}"/>
</head>

<body ng-app="chat">
  <%@ include file="/jsp/Wxjsapi.jsp" %>
  <ion-nav-view animation="slide-left-right">
  </ion-nav-view>
</body>

</html>
