var IP = '10.68.19.114';
var PORT = '8080';
var SITE = 'http://' + IP + ':' + PORT;

angular.module('chat', ['ionic', 'chat.controllers', 'chat.chatController',
      'chat.messageController', 'chat.settingController', 'chat.routes',
      'chat.services', 'chat.dateService', 'chat.messageService', 'chat.settingService', 'chat.chatService',
      'chat.directives', 'monospaced.elastic'
    ])

    .run(function ($ionicPlatform, $http, messageService, dateService) {
      var url = 'http://' + IP + ':' + PORT + '/chat/queryChat.json';
      if (ionic.Platform.isAndroid()) {
        url = "/android_asset/www/";
      }

      $ionicPlatform.ready(function () {
        if (window.cordova && window.cordova.plugins.Keyboard) {
          cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        }
        if (window.StatusBar) {
          StatusBar.styleDefault();
        }
      });
    });