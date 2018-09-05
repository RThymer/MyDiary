// pages/contentList.js
const util = require('../../utils/util.js')

Page({
  /**
   * 页面的初始数据
   */
  data: {
    content_list: [{
      diaryId: 1,
      ownerName: 'me',
      dateTime: util.formatTime(new Date()),
      textContent: 'Created by me;',
      imgPath: '../pics/01.jpg',
    }],
    selected_id: [],
    loaded: 0,
  },

  single_content_tap: function(e) {
    console.log(e.currentTarget);
    wx.setStorage({
      key: "one_diary",
      data: e.currentTarget
    })
    wx.navigateTo({
      url: '../content/content?id=' + e.currentTarget.id,
    })
  },

  checkboxChange: function(e) {
    var _this = this;
    var id_list = _this.data.selected_id;
    var exist = false;
    var loc = 0;
    for (let i = 0; i < id_list.length; i++) {
      if (id_list[i] == e.target.id) {
        exist = true;
        loc = i;
      }
    }
    if (exist) {
      id_list.splice(loc, 1);
    } else {
      id_list.push(e.target.id);
    }
    console.log("Selected_id: " + _this.data.selected_id);
  },

  new_tap: function() {
    wx.navigateTo({
      url: '../content/content?id=0'
    })
  },

  delete_tap: function(e) {
    console.log(e);
    var _this = this;
    var id_list = _this.data.selected_id;
    if (id_list.length == 0) {
      var toastMsg = '并没有选择删除的对象哟！';
      wx.showToast({
        title: toastMsg,
        icon: 'none',
        duration: 3000
      });
    } else {
      wx.showModal({
        title: '提示',
        content: '确定删除id为' + id_list[0] + '等' + id_list.length + '项吗？',
        success: function(sm) {
          if (sm.confirm) {
            if (id_list.length == 1) {
              wx.request({
                url: 'http://192.168.50.177:8080/diary/admin/removediary',
                data: {
                  "diaryId": id_list[0]
                },
                method: 'GET',
                success: function(res) {
                  console.log(res);
                  var list = _this.data.content_list;
                  for (let i = 0; i < list.length; i++) {
                    if (list[i].diaryId == id_list[0]) {
                      list.splice(i, 1);
                      break;
                    }
                  }
                  _this.setData({
                    content_list: list,
                  });
                }
              });
            } else {
              wx.request({
                url: 'http://192.168.50.177:8080/diary/admin/removediaries',
                data: {
                  "diaryIdList": id_list
                },
                method: 'POST',
                header: {
                  'Content-Type': 'application/json'
                },
                success: function(res) {
                  console.log(res);
                  var list = _this.data.content_list;

                  for (let i = 0; i < id_list.length; i++) {
                    var list_length = list.length;
                    for (let j = 0; j < list_length; j++) {
                      if (list[j].diaryId == id_list[i]) {
                        list.splice(j, 1);
                        break;
                      }
                    }
                  }
                  id_list.splice(0, id_list.length);
                  _this.setData({
                    content_list: list,
                  });
                }
              });
            }
            wx.showLoading({
              title: '执行中',
            })
            setTimeout(function() {
              var toastMsg = '成功了';
              wx.showToast({
                title: toastMsg,
                icon: '',
                duration: 2000
              });
            }, 3000)
            setTimeout(function() {
              wx.hideLoading()
            }, 1000)
          }
          _this.onLoad()
        }
      });
    }
  },

  sync_tap: function() {
    var _this = this;
    var id_list = _this.data.selected_id;
    if (id_list.length < 1) {
      wx.showToast({
        title: "不选择日志条目是不能同步的哦 :( ",
        icon: 'none',
        duration: 3000
      });
    }else if (id_list.length > 1) {
      wx.showToast({
        title: "请不要选择多个日志条目来同步",
        icon: 'none',
        duration: 3000
      });
    } else {
      wx.request({
        url: 'http://192.168.50.177:8080/diary/postweibo/api?diary_id=' + id_list[0],
        method: 'POST',
        success: function(res) {
          console.log(res);
          wx.showToast({
            title: "同步请求已提交 :)",
            icon: 'none',
            duration: 3000
          });
        }
      });
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var _this = this;
    wx.request({
      url: 'http://192.168.50.177:8080/diary/admin/listdiary',
      method: 'GET',
      data: {},
      success: function(res) {
        var list = res.data.diaryList;
        console.log("获取到的日志列表： ");
        console.log(list);
        if (list == null) {
          var toastMsg = '获取数据失败了:' + res.data.errMsg;
          wx.showToast({
            title: toastMsg,
            icon: '',
            duration: 3000
          });
        } else {
          var cur_list = _this.data.content_list;
          if (_this.data.loaded == 0) {
            _this.setData({
              content_list: list,
            });
          }
        };
        if (_this.data.loaded == 0) {
          for (let i = 0; i < list.length; i++) {
            wx.getFileInfo({
              filePath: list[i].imgPath,
              success(res) {
                console.log("成功获取的： ");
                console.log(res);
              },
              fail(res) {
                console.log("获取失败,回调： ");
                console.log(res);
                console.log('获取失败的图片路径： ' + list[i].imgPath);
                var path_split = list[i].imgPath.split('/');
                var file_name = path_split[path_split.length - 1];
                wx.downloadFile({
                  url: 'http://192.168.50.177:8080/diary/download?filename=' + file_name,
                  success: function(res) {
                    if (res.statusCode == 200) {
                      console.log(res.tempFilePath)
                      list[i].imgPath = res.tempFilePath
                      _this.setData({
                        content_list: list,
                      })
                    }
                  }
                })
              },
            })
          }
          _this.setData({
            loaded: 1,
          });
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    var _this = this;
    var need_refresh = wx.getStorageSync('need_refresh');
    if(need_refresh == 1) {
      var refresh_diary = wx.getStorageSync('refresh_diary');
      var diary_id = refresh_diary.diaryId;
      var diary_list = _this.data.content_list;
      for (let index = 0; index < diary_list.length; i++) {
        if (diary_list[index].diaryId == diary_id) {
          diary_list[index].ownerName = refresh_diary.ownerName;
          diary_list[index].dateTime = refresh_diary.dateTime;
          diary_list[index].textContent = refresh_diary.textContent;
          diary_list[index].imgPath = refresh_diary.imgPath;
          _this.setData({
            content_list: diary_list,
          });
          wx.setStorage({
            key: "need_refresh",
            data: 0
          })
          break;
        }
      }
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function(e) {
    console.log("Refreshed triggered")
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})