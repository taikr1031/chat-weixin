<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" ng-app="springChat">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>WebSocket聊天室</title>
  <link href="/lib/flat-ui/dist/css/vendor/bootstrap.min.css" rel="stylesheet"/>
  <link href="/lib/flat-ui/dist/css/flat-ui.css" rel="stylesheet"/>
  <link href="/css/chat.css" rel="stylesheet"/>
</head>
<body>
<div class="container">
  <div class="login">
    <div class="login-screen">
      <div class="login-form">
        <p>
          欢迎来到在线聊天室
        </p>
        <form method="post" action="/login">
          <div class="form-group">
            <input type="text" value="zm  " class="form-control login-field" placeholder="请输入您的姓名" name="username"/>
            <label class="login-field-icon fui-user" for="login-name">姓名</label>
          </div>
          <input class="btn btn-primary btn-lg btn-block" type="submit" value="进入聊天室"/>
        </form>
      </div>
    </div>
  </div>
</div>
</body>
</html>