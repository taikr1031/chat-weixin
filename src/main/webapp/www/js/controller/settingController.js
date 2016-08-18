angular.module('chat.settingController', [])
    .controller('settingCtrl', function ($rootScope, $scope, $state, $timeout, settingService) {
      $scope.$on("$ionicView.beforeEnter", function () {
        var data = settingService.getAllUser();
        $timeout(function() {
          $scope.friends = data;
        }, 0);
      });

      $scope.loginChat = function (name, password) {
        settingService.loginChat(name, password, setOwnInfoFn);
      };

      var setOwnInfoFn = function(user) {
        $rootScope.ownId = user.id;
        $rootScope.ownName = user.name;
        $state.go('tab.chat', {"ownId": user.id, "ownName": user.name});
      }

      $scope.getItemHeight = function(item, index) {
        //使索引项平均都有10px高，例如
        return (index % 2) === 0 ? 50 : 50;
      };
    });