angular.module('chat.chatService', [])
    .factory('queryChatFactory', function($http, $q) {
      return {
        getOwnChatList: function() {
          var url = SITE + '/chat/queryChat.json';
          var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行
          $http.get(url).success(function (data, status, headers, config) {
            deferred.resolve(data);
          }).error(function (data, status, headers, config) {
            deferred.reject(data);
          });
          return deferred.promise;
        }
      }
    })

    .factory('chatService', ['$http', '$q', 'localStorageService', 'dateService',
      function ($http, $q, localStorageService, dateService) {
        return {
          init: function (chats) {
            var i = 0;
            var length = 0;
            var date = null;
            var chatDate = null;
            if (chats) {
              length = chats.length;
              for (; i < length; i++) {
                chatDate = dateService.getChatDate(chats[i]);
                if (!chatDate) {
                  return null;
                }
                date = new Date(chatDate.year, chatDate.month,
                    chatDate.day, chatDate.hour, chatDate.minute,
                    chatDate.second);
                chats[i].timeFrome1970 = date.getTime();
              }
            }
          },

          getOwnChatList: function() {
            var promiseChats = chatService.queryChat(); // 同步调用，获得承诺接口
            promiseChats.then(function(data) { // 调用承诺API获取数据 .resolve
              chatData = data.chatList;
            }, function(data) { // 处理错误 .reject
              console.log('queryChat error!');
            });
            console.log('queryChatFactory...');
            console.log(chatData);
            return chatData;
          },

          getUserId: function () {
            var url = SITE + '/user/getUserId.json';
            var userId;
            $.ajax({
              async: false,
              type: 'GET',
              url: url,
              dataType: 'json',
              success: function (data) {
                if(data.stringList != null) {
                  userId = data.stringList[0];
                }
              }
            });
            return userId;
          },

          updateChat: function (chatId, friendId) {
            var url = SITE + '/chat/update/' + chatId + '/' + friendId + '.json';
            $http.get(url).then(function (res) {
              console.log('chat update success');
            });
          },

          deleteChatId: function (id) {
            var chatId = localStorageService.get("chatID");
            var length = 0;
            var i = 0;
            if (!chatId) {
              return null;
            }
            length = chatId.length;
            for (; i < length; i++) {
              if (chatId[i].id === id) {
                chatId.splice(i, 1);
                break;
              }
            }
            localStorageService.update("chatID", chatId);
          },
          clearChat: function (chat) {
            var id = 0;
            if (chat) {
              id = chat.id;
              localStorageService.clear("chat_" + id);
            }
          }
        };
      }
    ]);