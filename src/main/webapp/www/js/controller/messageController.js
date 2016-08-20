angular.module('chat.messageController', [])

    .controller('messageCtrl',
      function ($rootScope, $scope, $stateParams, $ionicScrollDelegate, $ionicActionSheet, $timeout, $ionicLoading,$ionicHistory, $state, messageService, settingService) {
        var viewScroll = $ionicScrollDelegate.$getByHandle('messageDetailsScroll');
        var beginDate;
        // 聊天界面中录音按钮开启（down）和停止（up）事件绑定
        $('#voiceBtn').bind('touchstart', function () {
          beginDate = new Date();
          wxjs.run(function () {
            wx.startRecord();
          });
        }).bind('touchend', function () {
          var endDate = new Date();
          wxjs.run(function () {
            wx.stopRecord({
              success: function (res) {
                var localId = res.localId;
                $scope.localId = localId;
                $scope.model.msg = localId;
                var intervalNum = Math.round((endDate.getTime() - beginDate.getTime()) / 1000);
                sendVoice(localId, intervalNum);
              }
            });
          });
        });

        // 页面载入事件
        $scope.$on("$ionicView.beforeEnter", function () {
          $scope.isInputText = true;
          $scope.model.msg = "";

          var chatIndex = parseInt($stateParams.chatIndex);
          var userIds = $stateParams.chatId.split('-');
          var friendId = (userIds[0] == $stateParams.ownId) ? userIds[1] : userIds[0];

          $scope.model = {
            chatId: $stateParams.chatId,
            ownId: $stateParams.ownId,
            ownName: $stateParams.ownName,
            friendId: friendId
          };
          var promiseChat = messageService.queryMessage($stateParams.chatId); // 同步调用，获得承诺接口
          promiseChat.then(function(data) { // 调用承诺API获取数据 .resolve
            $scope.messages = data.messageList;
          }, function(data) { // 处理错误 .reject
            console.log('queryMessage error!');
          });
          settingService.getUserById(chatIndex, friendId, beforeEnterFn)
        });

        var beforeEnterFn = function(chatIndex, friend) {
          $scope.model.friendName = friend.name;
          $scope.model.friendPic = friend.picture.thumbnail;
          console.log($scope.friend);
          // 将该聊天信息的未读条数清除和未读状态
          if($rootScope.chatList[chatIndex].auserId == $scope.model.friendId) {
            $rootScope.chatList[chatIndex].auserNoReadNum = 0;
            $rootScope.chatList[chatIndex].auserShowHints = false;
          } else {
            $rootScope.chatList[chatIndex].buserNoReadNum = 0;
            $rootScope.chatList[chatIndex].buserShowHints = false;
          }
          // 设置聊天对象的openid
          if($rootScope.chatList[chatIndex].auserId == $scope.model.friendId) {
            $scope.model.ownPic = $rootScope.chatList[chatIndex].buserPic;
            $scope.model.friendCode = $rootScope.chatList[chatIndex].auserCode;
          } else {
            $scope.model.ownPic = $rootScope.chatList[chatIndex].auserPic;
            $scope.model.friendCode = $rootScope.chatList[chatIndex].buserCode;
          }
          $scope.messageNum = 8;
          $timeout(function () {
            viewScroll.scrollBottom();
          }, 0);
          connect();
        };

        $scope.backChat = function() {
          //$ionicHistory.goBack(-1);
          window.history.go(-1);
        };

        $scope.doRefresh = function () {
          $scope.messageNum += 5;
          $timeout(function () {
            // duplicate
            var promiseChat = messageService.queryMessage($stateParams.chatId); // 同步调用，获得承诺接口
            promiseChat.then(function(data) { // 调用承诺API获取数据 .resolve
              $scope.messages = data.messageList;
            }, function(data) { // 处理错误 .reject
              console.log('queryMessage error!');
            });

            $scope.$broadcast('scroll.refreshComplete');
          }, 1);
        };

        /*  WEBSOCKET   */
        var ws = null;
        // 连接到spring的websocket
        var connect = function () {
          ws = new WebSocket('ws://' + IP + ':' + PORT + '/ws');
          ws.onopen = function () {
            setConnected(true);
            log('Info: connection opened.');
          };
          ws.onmessage = function (event) {
            log('Received: ' + event.data);
            if(event.data == '0') {
              return;
            }
            var param = event.data;
            var ownId = ($scope.model.chatId.split('-')[0] == $scope.model.ownId) ? $scope.model.chatId.split('-')[1] : $scope.model.chatId.split('-')[0];
            var msg, pic, type, mediaId;
            if (param.indexOf(MESSAGE_SPACE) != -1) {
              msg = param.split(MESSAGE_SPACE)[0];
              pic = param.split(MESSAGE_SPACE)[2];
              type = param.split(MESSAGE_SPACE)[3];
            } else {
              msg = param;
              pic = '';
            }
            if(type == 'IMAGE') {
              var downloadId = down(msg);
              mediaId = msg;
              msg = downloadId;
            }
            var data = generateMessage(msg, ownId, pic, mediaId, type);
            $scope.messages.push(data);
            $timeout(function () {
              viewScroll.scrollBottom();
            }, 0);
          };
          ws.onclose = function (event) {
            setConnected(false);
            log('Info: connection closed.');
            log(event);
          };
        };

        var disconnect = function () {
          if (ws != null) {
            ws.close();
            ws = null;
          }
          setConnected(false);
        };
        /*  WEBSOCKET   */

        // 用户发送微信消息后，同时通过websocket发给服务器，服务器通过websocket给收信人推送一条消息，收信人的ws.onmessage事件回调函数将该消息自动显示在聊天界面最下方，
        var sendWsMessage = function (message) {
          if (ws != null) {
            log('sendWsMessage: ' + message);
            ws.send(message);
          } else {
            alert('connection not established, please connect.');
          }
        };

        var log = function (message) {
          console.log(message);
        };

        var setConnected = function (connected) {
        };

        /* TEXT */
        $scope.sendText = function () {
          sendWsMessage($scope.model.msg + MESSAGE_SPACE + $scope.model.friendId + MESSAGE_SPACE + $scope.model.ownPic + MESSAGE_SPACE + 'TEXT');
          messageService.sendWxMessage($scope.model.chatId, $scope.model.ownId, $scope.model.friendCode, $scope.model.ownPic, $scope.model.msg, 'TEXT');
          var data = generateMessage($scope.model.msg, $scope.model.ownId, $scope.model.ownPic, null, 'TEXT');
          $scope.messages.push(data);
          $scope.model.msg = '';
          viewScroll.scrollBottom();
        };
        /* TEXT */

        /* LOCATION*/
        var location = {
          latitude: 0, longitude: 0, speed: 0, accuracy: 0
        };

        var getLocation = function () {
          wxjs.run(function () {
            wx.getLocation({
              type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
              success: function (res) {
                location.latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                location.longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                location.speed = res.speed; // 速度，以米/每秒计
                location.accuracy = res.accuracy; // 位置精度
                openLocation();
              },
              cancel: function () {
                alert('用户拒绝授权获取地理位置');
              }
            });
          })
        };

        var openLocation = function () {
          alert('东经：' + parseFloat(location.longitude) + '， 北纬：' + parseFloat(location.latitude));
          wx.openLocation({
            latitude: parseFloat(location.latitude), // 纬度，浮点数，范围为90 ~ -90
            longitude: parseFloat(location.longitude), // 经度，浮点数，范围为180 ~ -180。
            name: '楚烟', // 位置名
            address: '武汉市硚口区1号', // 地址详情说明
            scale: 16, // 地图缩放级别,整形值,范围从1~28。默认为最大
            infoUrl: 'http://www.whcyit.com/whcyit/index.xhtml' // 在查看位置界面底部显示的超链接,可点击跳转
          });
        };
        /* LOCATION*/

        /* IMAGE */
        var images = {
          localId: [],
          serverId: [],
          downloadId: []
        };
        var uploadedCount = 0;

        var chooseImage = function () {
          images.localId.length = 0;
          images.serverId.length = 0;
          images.downloadId.length = 0;
          uploadedCount = 0;

          wxjs.run(function () {
            wx.chooseImage({
              count: 2, // 默认9
              sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
              sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
              success: function (res) {
                images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                uploadImages(images.localId);
              }
            });
          });
        };

        var uploadImages = function (localIds) {
          $('body,html').animate({scrollTop: 0}, 500);
          $ionicLoading.show({
            template: '图片上传中  ...'
          });
          var localIdsClone = localIds.slice();
          var localId = localIdsClone.pop();
          wx.uploadImage({
            localId: localId,
            isShowProgressTips: 0,
            success: function (res) {
              $ionicLoading.show({
                template: '图片上传中 ' + (uploadedCount + 1) + '/' + localIds.length + ' ...'
              });
              images.serverId.push(res.serverId);
              uploadedCount++;
              if (localIdsClone.length > 0) {
                uploadImages(localIdsClone);
              } else {
                images.downloadId = downloadImage(images.serverId);
                $timeout(function () {
                  for (var i = 0; i < images.serverId.length; i++) {
                    messageService.sendWxMessage($scope.model.chatId, $scope.model.ownId, $scope.model.friendCode, $scope.model.ownPic, images.serverId[i], 'IMAGE');
                    sendWsMessage(images.serverId[i] + MESSAGE_SPACE + $scope.model.friendId + MESSAGE_SPACE + $scope.model.ownPic + MESSAGE_SPACE + 'IMAGE');
                    var data = generateMessage(images.downloadId[i], $scope.model.ownId, $scope.model.ownPic, images.serverId[i], 'IMAGE');
                    $scope.messages.push(data);
                    $scope.model.msg = '';
                    //attachNewMessage(images.serverId[i], images.downloadId[i], 'IMAGE');
                    $timeout(function () {
                      //document.getElementById(downloadId).src = downloadId;
                      viewScroll.scrollBottom();
                    }, 0);
                  }
                }, 100);
              }
              $ionicLoading.hide();
            }, fail: function (res) {
              $ionicLoading.hide();
              alert('uploadImages error: ' + JSON.stringify(res));
            }
          })
        };

        var downloadImage = function (serverIds) {
          var serverIdsClone = serverIds.slice();
          var serverId = serverIdsClone.pop();

          return images.downloadId;
        };

        var down = function(serverId) {
          wx.downloadImage({
            serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImages接口获得
            isShowProgressTips: 0, // 默认为1，显示进度提示
            success: function (res) {
              return res.localId; // 返回图片下载后的本地ID
            }, fail: function (res) {
              alert('downloadImage error: ' + JSON.stringify(res));
            }
          });
        };

        //var sendWsImages = function() {
        //  var serverIds;
        //  if (images.serverId.length > 0)
        //    serverIds = images.serverId.join("◆");
        //  sendWsMessage(serverIds + MESSAGE_SPACE + $scope.model.friendId + MESSAGE_SPACE + $scope.model.ownPic);
        //  var data = generateMessage(serverIds, $scope.model.ownId, $scope.model.ownPic, 'IMAGE');
        //  $scope.messages.push(data);
        //  messageService.sendWxMessage($scope.model.chatId, $scope.model.ownId, $scope.model.friendCode, $scope.model.ownPic, serverIds, 'IMAGE');
        //  $scope.model.msg = '';
        //  viewScroll.scrollBottom();
        //};

        //var sendWxImages = function (serverId) {
        //  messageService.sendWxMessage($scope.model.friendCode, serverId);
          //var data = {};
          //data.content = downloadId;
          //data.userId = $scope.model.ownId;
          //data.time = new Date();
          //data.type = 'IMAGE';
          //data.mediaId = serverId;
          //$scope.messages.push(data);
          //$scope.model.msg = '';
          //$timeout(function () {
          //  document.getElementById(downloadId).src = downloadId;
          //  viewScroll.scrollBottom();
          //}, 0);
        //};

        //var attachNewMessage = function(serverId, downloadId, type) {
        //  var data = {};
        //  data.content = downloadId;
        //  data.userId = $scope.model.ownId;
        //  data.time = new Date();
        //  data.type = type;
        //  data.mediaId = serverId;
        //  $scope.messages.push(data);
        //  $scope.model.msg = '';
        //};

        var generateMessage = function (msg, ownId, pic, mediaId, type) {
          var data = {};
          data.content = msg;
          data.userId = ownId;
          data.time = new Date();
          data.type = type;
          data.pic = pic;
          data.mediaId = mediaId;
          return data;
        };

        $scope.previewImage = function (downloadId) {
          wxjs.run(function () {
            wx.previewImage({
              current: downloadId,
              urls: images.downloadId
            });
          });
        };
        /* IMAGE */

        /* VOICE */
        $scope.palyAudio = function (mediaId) {
          wxjs.run(function () {
            wx.playVoice({
              localId: mediaId// 需要播放的音频的本地ID，由stopRecord接口获得
            });
          });
        };

        sendVoice = function (mediaId, intervalNum) {
          var data = {};
          data.content = ' ' + intervalNum + '秒';
          data.userId = $scope.model.ownId;
          data.time = new Date();
          data.type = 'VOICE';
          data.mediaId = mediaId;
          $scope.messages.push(data);
          messageService.sendVoice($scope.model.friendCode, mediaId);
          $scope.model.msg = '';
          viewScroll.scrollBottom();
        };
        /* VOICE */

        $scope.toggleInput = function (isInputText) {
          $scope.isInputText = !isInputText;
        };

        // 点击“加号”展开其他功能菜单
        $scope.show = function () {
          // Show the action sheet
          var hideSheet = $ionicActionSheet.show({
            buttons: [
              {text: '<i class="ion-ios-camera icon-button icon-action" ></i>    <span class="tab-action"></span>     <i class="text-action">照片</i> '},
              {text: '<i class="ion-social-instagram icon-button icon-action" ></i>   <span class="tab-action"></span>        <i class="text-width">小视频</i> '},
              {text: '<i class="ion-ios-videocam icon-button icon-action" ></i>   <span class="tab-action"></span>        <i class="text-width">视频聊天</i> '},
              {text: '<i class="ion-ios-location icon-button icon-action" ></i>    <span class="tab-action"></span>        <i class="text-width">位置</i> '},
              {text: '<i class="ion-ios-eye icon-button icon-action" ></i>    <span class="tab-action"></span>        <i class="text-width">收藏</i> '},
              //{ text: '<i class="ion-more icon-button icon-action" ></i>               <span class="tab-action"></span>        <i class="text-width">More</i> ' },
            ],
            //destructiveText: 'Delete',
            //titleText: 'Modify your album',
            //cssClass: 'social-actionsheet',
            //cancelText: 'Cancel',
            //cancel: function() {
            //},
            buttonClicked: function (index) {
              if (index == '0') {
                chooseImage();
              } else if (index == '3') {
                getLocation();
              }
              return true;
            }
          });
          // For example's sake, hide the sheet after two seconds
          //me.$timeout(function() {
          //  hideSheet();
          //}, 2000);
        };

        window.addEventListener("native.keyboardshow", function (e) {
          viewScroll.scrollBottom();
        });
      }
    );
