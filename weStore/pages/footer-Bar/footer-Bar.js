// pages/footer-Bar/footer-Bar.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // bar_index: Number
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    onChange: function (e){
      var _this = this;
      var index = e.detail
      if(index == 0){
        wx.redirectTo({
          url: '/pages/index/index',
        })
      } else if (index == 1){
        
      }else if(index == 2){
        wx.redirectTo({
          url: '/pages/items-upLoad/upload',
        })  
      }else {
        wx.redirectTo({
          url: '/pages/selfInfo/selfInfo',
        })
      }
    }
  }
})
