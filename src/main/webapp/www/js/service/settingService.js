angular.module('chat.settingService', [])
    .factory('settingService', function ($http) {
        return {
          getAllUser: function () {
            var url = SITE + '/user/getAllUser.json';
            return $http.get(url).then(function (res) {
              return res.data;
            });
          },

          getUserById: function (chatIndex, userId, fn) {
            var url = SITE + '/user/getUserById/' + userId;
            return $http.get(url).then(function(res) {
              fn(chatIndex, res.data.user);
              //return res.data;
            });
          },

          loginChat: function (name, password, fn) {
            var url = SITE + '/login/login/' + name + '/' + password;
            $http.get(url).then(function (res) {
              fn(res.data.user);
            });
          },

          isLoginSession: function (ownOpenId) {
            var url = SITE + '/login/isLoginSession/' + ownOpenId;
            console.log('setFriendSessionInfo: ' + url);
            $http.get(url).then(function (response) {
              if (response.data == false) {
                return response.data;

              }
            });
          }
        };
      }
    );