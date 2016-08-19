angular.module('chat.messageService', [])
    .factory('messageService', function ($http, $q) {
        return {
          queryMessage: function (chatId) {
            var url = 'http://' + IP + ':' + PORT + '/message/queryMessage/' + chatId;
            var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
            $http({method: 'GET', url: url}).success(function (data, status, headers, config) {
              deferred.resolve(data);
            }).error(function (data, status, headers, config) {
              deferred.reject(data);
            });
            return deferred.promise;
          },

          sendWxMessage: function (chatId, ownId, openid, ownPic, msg, type) {
            //var msgs = msg.split('◆');
            //for(var i = 0; i < msgs.length; i++) {
              var wxUrl = SITE + '/wxServlet?type=' + type + '&openid=' + openid + '&content=' + msg;
              return $http.get(wxUrl).then(function (res) {
                var saveUrl = SITE + '/message/save';
                var data = {
                  chatId: chatId,
                  userId: ownId,
                  pic: ownPic,
                  content: msg,
                  type: type
                };
                $http({
                  method: 'POST',
                  url: saveUrl,
                  data: data
                }).then(function (res) {
                  console.log(res.data);
                })
              });
            //}
          },

          //sendWxImage: function (chatId, ownId, openid, ownPic, msg, type) {
          //  var url = SITE + '/wxServlet?type=IMAGE&openid=' + openid + '&content=' + msg;
          //  return $http.get(url).then(function (res) {
          //    console.log('IMAGE success');
          //  });
          //},
          //
          //sendVoice: function (openid, mediaId) {
          //  var url = SITE + '/wxServlet?type=VOICE&openid=' + openid + '&content=' + mediaId;
          //  return $http.get(url).then(function (res) {
          //    console.log('VOICE success');
          //  });
          //}
        };
      }
    );