import request from '@/utils/request'
import des from '../utils/des'
const key = 'mmhll1314520lhxq';

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
      data:des.encryptByDES(JSON.stringify(postData),key)
    })
  }
}
