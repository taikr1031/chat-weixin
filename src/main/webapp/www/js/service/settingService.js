angular.module('chat.settingService', [])
    .factory('settingService', function ($http) {
        return {
          getAllUser: function () {
            var url = SITE + '/user/getAllUser.json';
            return $http.get(url).then(function (response) {
              return response.data;
            });
          },

          loginChat: function (name, password) {
            var url = SITE + '/login/login/' + name + '/' + password;
            $http.get(url).then(function (response) {
              console.log(response.data.user.name + '-=' + response.data.user.code + ' loginChat success!');
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