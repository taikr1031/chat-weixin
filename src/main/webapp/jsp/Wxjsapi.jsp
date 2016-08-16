<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<%@ page import="com.zm.weixin.*" %>
<%@ page import="java.util.Map" %>
<%
  String appid = "wx791d897f45713b38";
  String secret = "ec1be18ba7c332f5ff260c08f3c93e2d";
  String token = "12345678901234567890123456789012";
  String url = "http://10.68.19.114:8080/www/index.jsp";
  Map signMap = Signature.getSign(appid, secret, token, url);
%>
<script type="text/javascript">
  var wxjs = {
    init: false,
    run: function (fn) {
      if (!wxjs.init) {
        if(!wxjs.jsApiList) {
          wxjs.jsApiList = [];
        }
        if(wxjs.hideMenu && wxjs.jsApiList.indexOf('hideOptionMenu')==-1) {
          wxjs.jsApiList.push('hideOptionMenu');
        }
        var config = {
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: '<%=appid%>', // 必填，公众号的唯一标识
          timestamp: <%=signMap.get("timestamp")%>, // 必填，生成签名的时间戳
          nonceStr: '<%=signMap.get("noncestr")%>', // 必填，生成签名的随机串
          signature: '<%=signMap.get("signature")%>',// 必填，签名，见附录1
          jsApiList: wxjs.jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        };
        wx.config(config);
        console.log(config);
        wx.ready(function () {
          if (wxjs.hideMenu) {
            wx.hideOptionMenu();
          }
          if (!("undefined" == typeof wxjsAfterReload)) {
            wxjsAfterReload();
          }
          wxjs.init = true;
          fn && fn();
        });

        wx.error(function (res) {
          alert('微信JSAPI初始化失败!出错原因:' + res.errMsg);
        });

      } else {
        fn && fn();
      }
    }
  }
</script>
