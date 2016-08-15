angular.module('chat.settingController', [])
    .controller('settingCtrl', function ($rootScope, $scope, $state, $timeout, settingService) {
      $scope.$on("$ionicView.beforeEnter", function () {
        var data = settingService.getAllUser();
        $timeout(function() {
          console.log(data);
          $scope.friends = data;
        }, 0);
      });
      $scope.loginChat = function (name, password) {
        settingService.loginChat(name, password);
        $rootScope.loginName = name;
        $state.go('tab.chat', {"userName": name});
      }
    });