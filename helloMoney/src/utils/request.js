import axios from 'axios' //引入axios

/* 创建axios实例 */
const service = axios.create({
  //这个process.env.BASE_URL在config/dev.evn.js、prod.evn.js里面进行配置
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 5000 // 请求超时时间
});

/*response拦截器，进行错误处理*/
service.interceptors.response.use(response => {
    return response;
  },
  error => {
    // 失败处理
    // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
    // 否则的话抛出错误
    switch (error.response.status) {
      case 400:
        error.message = '错误请求'
        break;
      case 401:
        error.message = '未授权，请重新登录'
        break;
      case 403:
        error.message = '拒绝访问'
        break;
      case 404:
        error.message = '请求错误,未找到该资源'
        break;
      case 405:
        error.message = '请求方法未允许'
        break;
      case 408:
        error.message = '请求超时'
        break;
      case 500:
        error.message = '服务器端出错'
        break;
      case 501:
        error.message = '网络未实现'
        break;
      case 502:
        error.message = '网络错误'
        break;
      case 503:
        error.message = '服务不可用'
        break;
      case 504:
        error.message = '网络超时'
        break;
      case 505:
        error.message = 'http版本不支持该请求'
        break;
      default:
        error.message = `连接错误${error.response.status}`
    }
    return Promise.reject(error);
  }
);

/*reuqest拦截器*/
service.interceptors.request.use(config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

export default service;
