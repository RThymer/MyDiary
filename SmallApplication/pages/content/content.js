// pages/content/content.js
const util = require('../../utils/util.js')

Page({
  /**
   * 页面的初始数据
   */
  data: {
    diaryId: null,
    ownerName: '',
    dateTime: '',
    textContent: '',
    imgPath: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.setStorage({
      key: "need_refresh",
      data: 0
    })
    if (options.id != 0) {
      var one_diary = wx.getStorageSync('one_diary');
      this.setData({
        diaryId: one_diary.id,
        ownerName: one_diary.dataset.owner,
        dateTime: one_diary.dataset.date,
        textContent: one_diary.dataset.text,
        imgPath: one_diary.dataset.image,
      });
    } else {
      this.setData({
        diaryId: null,
        ownerName: 'robot',
        dateTime: '',
        textContent: '',
        imgPath: '',
      });
    }
  },

  input_text: function(e) {
    this.setData({
      textContent: e.detail.value,
    });
  },

  picButtonTap: function(e) {
    var _this = this;
    console.log(e);
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: function(res) {
        console.log(res);
        _this.setData({
          imgPath: res.tempFilePaths[0],
        })
      },
    });
  },

  saveButtonTap: function(e) {
    var _this = this;
    var imgUrl = this.data.imgPath;
    console.log(e);
    console.log(imgUrl);
    wx.setStorage({
      key: "need_refresh",
      data: 1
    })
    wx.setStorage({
      key: "refresh_diary",
      data: _this.data
    })
    wx.uploadFile({
      url: 'http://192.168.50.177:8080/diary/upload',
      filePath: imgUrl,
      name: 'file',
      formData: {
        'user': 'test'
      },
      success: function(res) {
        console.log(res);
        var data = res.data;
        //do something
      }
    });

    var reqUrl = '';
    if (_this.data.diaryId == null) {
      reqUrl = 'http://192.168.50.177:8080/diary/admin/adddiary';
    } else {
      reqUrl = 'http://192.168.50.177:8080/diary/admin/modifydiary';
    }
    console.log(JSON.stringify(_this.data));
    wx.request({
      url: reqUrl,
      data: JSON.stringify({
        'diaryId': _this.data.diaryId,
        'ownerName': _this.data.ownerName,
        'dateTime': _this.data.dateTime,
        'textContent': _this.data.textContent,
        'imgPath': _this.data.imgPath
      }),
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      success: function(res) {
        console.log(res);
        var result = res.data.success;
        var toastContent = "操作成功！";
        if (result != true) {
          toastContent = "操作失败了:" + res.data.errMsg;
        }
        wx.showToast({
          title: toastContent,
          icon: '',
          duration: 3000
        });
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
  onPullDownRefresh: function() {

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