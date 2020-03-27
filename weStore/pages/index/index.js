//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    show:true,
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    data:'',
    tabList:['推荐','公司详情'],
    // slogn:[
    //   {url:'/static/icon/1.png',text:"易于心安"},
    //   {url:'/static/icon/2.png',text:"质量保证"},
    //   {url:'/static/icon/3.png',text:"价格亲民"},
    // ],
    swipeList:[
      {url:'/static/Swipe-items/1.jpg'},
      {url:'/static/Swipe-items/2.jpg'},
      {url:'/static/Swipe-items/3.jpg'},
    ]
  },
  onClickHide() {
    this.setData({ show: false });
  },
  onChange(e) {
    this.setData({
      value: e.detail
    });
  },
  onSearch() {
    
  },
  onClick() {
    wx.navigateTo({
      url: '/pages/itemsInfo/itemsInfo',
    })
   /*  wx.request({
      url: 'http://127.0.0.1:8181/upUserPass',  //本地服务器地址
      data: {
        userName: 'wal',
        password: 'e10adc3949ba59abbe56e057f20f883e',
      },
      method: 'post',
      header: {
        'content-type': 'application/json' //默认值
      },
      success: function (res) {
        console.log(res.data);
      },
      fail: function (res) {
        console.log("失败");
      }
    }) */
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})
