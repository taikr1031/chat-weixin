angular.module('chat.dateService', [])
    .factory('dateService', ['$filter', function ($filter) {
      return {
        handleChatDate: function (chats) {
          var i = 0,
              length = 0,
              chatDate = {},
              nowDate = {},
              weekArray = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
              diffWeekValue = 0;
          if (chats) {
            nowDate = this.getNowDate();
            length = chats.length;
            for (i = 0; i < length; i++) {
              chatDate = this.getChatDate(chats[i]);
              if (!chatDate) {
                return null;
              }
              if (nowDate.year - chatDate.year > 0) {
                chats[i].lastMessage.time = chatDate.year + "";
                continue;
              }
              if (nowDate.month - chatDate.month >= 0 ||
                  nowDate.day - chatDate.day > nowDate.week) {
                chats[i].lastMessage.time = chatDate.month +
                    "月" + chatDate.day + "日";
                continue;
              }
              if (nowDate.day - chatDate.day <= nowDate.week &&
                  nowDate.day - chatDate.day > 1) {
                diffWeekValue = nowDate.week - (nowDate.day - chatDate.day);
                chats[i].lastMessage.time = weekArray[diffWeekValue];
                continue;
              }
              if (nowDate.day - chatDate.day === 1) {
                chats[i].lastMessage.time = "昨天";
                continue;
              }
              if (nowDate.day - chatDate.day === 0) {
                chats[i].lastMessage.time = chatDate.hour + ":" + chatDate.minute;
                continue;
              }
            }
            // console.log(chats);
            // return chats;
          } else {
            console.log("chats is null");
            return null;
          }

        },
        getNowDate: function () {
          var nowDate = {};
          var date = new Date();
          nowDate.year = date.getFullYear();
          nowDate.month = date.getMonth();
          nowDate.day = date.getDate();
          nowDate.week = date.getDay();
          nowDate.hour = date.getHours();
          nowDate.minute = date.getMinutes();
          nowDate.second = date.getSeconds();
          return nowDate;
        },
        getChatDate: function (chat) {
          var chatDate = {};
          var chatTime = "";
          //2015-10-12 15:34:55
          var reg = /(^\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
          var result = new Array();
          if (chat) {
            //chatTime = chat.originalTime;
            chatTime = $filter("date")(chat.originalTime, "yyyy-MM-dd HH:mm:ss");

            result = reg.exec(chatTime);
            if (!result) {
              console.log("result is null");
              return null;
            }
            chatDate.year = parseInt(result[1]);
            chatDate.month = parseInt(result[2]);
            chatDate.day = parseInt(result[3]);
            chatDate.hour = parseInt(result[4]);
            chatDate.minute = parseInt(result[5]);
            chatDate.second = parseInt(result[6]);
            // console.log(chatDate);
            return chatDate;
          } else {
            console.log("chat is null");
            return null;
          }
        }
      };
    }]);