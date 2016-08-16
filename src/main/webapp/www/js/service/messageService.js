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

          sendText: function (chatId, ownId, openid, ownPic, msg, type) {
            var wxUrl = 'http://' + IP + ':' + PORT + '/wxServlet?type=TEXT&openid=' + openid + '&content=' + msg;
            return $http.get(wxUrl).then(function (res) {
              console.log('TEXT success');
              var saveUrl = SITE + '/message/save';
              var data = {
                chatId: chatId,
                userId: ownId,
                pic: ownPic,
                msg: msg,
                type: type
              };
              $http({
                method: 'POST',
                url: saveUrl,
                data: data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                transformRequest: function (obj) {
                  var str = [];
                  for (var p in obj) {
                    //str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    str.push(p + "=" + obj[p]);
                  }
                  return str.join("&");
                }
              }).then(function (res) {
                console.log(res.data);
              })
            });
          },

          sendImage: function (openid, msg) {
            var url = SITE + '/wxServlet?type=IMAGE&openid=' + openid + '&content=' + msg;
            return $http.get(url).then(function (res) {
              console.log('IMAGE success');
            });
          },

          sendVoice: function (openid, mediaId) {
            var url = SITE + '/wxServlet?type=VOICE&openid=' + openid + '&content=' + mediaId;
            return $http.get(url).then(function (res) {
              console.log('VOICE success');
            });
          }
        };
      }
    );