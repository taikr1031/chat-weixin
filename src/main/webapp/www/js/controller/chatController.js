angular.module('chat.chatController', [])



    .controller('chatCtrl', function ($rootScope, $scope, $state, $stateParams, $ionicPopup, $timeout, localStorageService, chatService, chatFactory) {
      $scope.$on("$ionicView.beforeEnter", function () {
        console.log($scope.chats);
        $scope.userModel = {
          userName: $stateParams.userName
        };
        $rootScope.model = {
          userName: $scope.userModel.userName
        };

        var promiseChats = chatFactory.getOwnChatList(); // 同步调用，获得承诺接口
        promiseChats.then(function(data) { // 调用承诺API获取数据 .resolve
          $scope.chats = data.chatList;
          $rootScope.chatList = data.chatList;
        }, function(data) { // 处理错误 .reject
          console.log('queryChat error!');
        });
        var userId = chatService.getUserId();
        $scope.userModel.userId = userId;
        console.log('beforeEnter userId: ' + $scope.userModel.userId);

        $scope.popup = {
          isPopup: false,
          index: 0
        };
      });

      $scope.onSwipeLeft = function () {
        $state.go("tab.friends");
      };

      $scope.popupMessageOpthins = function (chat) {
        $scope.popup.index = $scope.chats.indexOf(chat);
        $scope.popup.optionsPopup = $ionicPopup.show({
          templateUrl: "templates/popup.html",
          scope: $scope
        });
        $scope.popup.isPopup = true;
      };

      // 好友列表中好友头像右上方未读信息条数提示
      $scope.markMessage = function () {
        var userId = chatService.getUserId();
        alert('markMessage ' + userId);
        var index = $scope.popup.index;
        var chat = $scope.chats[index];
        if (chat.showHints) {
          chat.showHints = false;
          chat.noReadMessages = 0;
        } else {
          chat.showHints = true;
          chat.noReadMessages = 1;
        }
        $scope.popup.optionsPopup.close();
        $scope.popup.isPopup = false;
        chatService.updateChat(chat);
      };

      // 好友列表页面中删除好友
      $scope.deleteMessage = function () {
        var index = $scope.popup.index;
        var message = $scope.chats[index];
        $scope.chats.splice(index, 1);
        $scope.popup.optionsPopup.close();
        $scope.popup.isPopup = false;
        chatService.deleteChatId(message.id);
        chatService.clearChat(message);
      };

      // 好友列表页面设置好友置顶
      $scope.topMessage = function () {
        var index = $scope.popup.index;
        var message = $scope.chats[index];
        if (message.isTop) {
          message.isTop = 0;
        } else {
          message.isTop = new Date().getTime();
        }
        $scope.popup.optionsPopup.close();
        $scope.popup.isPopup = false;
        chatService.updateChat(message);
      };

      $scope.toMessage = function (chatId, ownId, ownName, index) {
        var friendId = chatId.split('-')[0] == ownId ? chatId.split('-')[1] : chatId.split('-')[0];
        chatService.updateChat(chatId, friendId);
        $state.go("message", {
          "chatId": chatId,
          "ownId": ownId,
          "ownName": ownName,
          "chatIndex": index
        });
      };
    });
