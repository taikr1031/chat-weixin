<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" ng-app="springChat">
<head>
<meta charset="utf-8" />
<title>Spring WebSocket Chat</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="/lib/flat-ui/dist/css/vendor/bootstrap.min.css" rel="stylesheet" />
<link href="/lib/flat-ui/dist/css/flat-ui.css" rel="stylesheet" />
<link href="/lib/angularjs-toaster/toaster.css" rel="stylesheet" />
<link href="/css/chat.css" rel="stylesheet" />
</head>
<body>

  <div class="container" ng-controller="ChatController">
    <toaster-container></toaster-container>
    <input type="hidden" th:value="${username}" id="username" ng-init="initUsername()">
    <div class="row">
      <nav class="navbar navbar-inverse navbar-embossed" role="navigation">
        <div class="collapse navbar-collapse" id="navbar-collapse-01">
          <h1>WebSocket在线聊天室</h1>
        </div>
        <!-- /.navbar-collapse -->
      </nav>
      <!-- /navbar -->
    </div>
    <div class="row">
      <div class="col-xs-4">
        <h4>在线聊天室 [{{participants.length}}]</h4>
        <div class="share">
          <ul ng-repeat="participant in participants">
            <li>
              <span class="input-icon fui-new" ng-show="participant.typing"></span>
              <span class="input-icon fui-user" ng-show="!participant.typing"></span>
              <span ng-bind="participant.username"></span>
            </li>
          </ul>
        </div>
      </div>
      <div class="col-xs-8 chat-box">
        <h4>消息</h4>
        <div ng-repeat="message in messages">
          <small print-message=""></small>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="form-group">
        <p>请输入消息内容，按回车键发送</p>
        <input id="newMessageInput" type="text" class="form-control"
          placeholder="消息内容"
          ng-model="newMessage"
          ng-keyup="$event.keyCode == 13 ? sendMessage() : startTyping()" />
      </div>
    </div>
  </div>
  <!-- /.container -->

  <!-- 3rd party -->
  <script src="lib/angular/angular.min.js"></script>
  <script src="lib/angular-animate/angular-animate.min.js"></script>
  <script src="lib/angularjs-toaster/toaster.js"></script>
  <script src="lib/angularjs-scroll-glue/src/scrollglue.js"></script>
  <script src="lib/sockjs/sockjs.min.js"></script>
  <script src="lib/stomp/lib/stomp.min.js"></script>

  <!-- App -->
  <script src="js/app.js"></script>
  <script src="js/controllers.js"></script>
  <script src="js/services.js"></script>
  <script src="js/directives.js"></script>
</body>
</html>