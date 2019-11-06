import request from '@/utils/request'

export function commonAPI(api,postData){
  if(postData===''){
    return request({
      url:'/'+api,
      method:'post'
    })
  }else{
    return request({
      url:'/'+api,
      method:'post',
      data:postData
    })
  }
}
