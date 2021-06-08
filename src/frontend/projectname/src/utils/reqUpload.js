// 文件上传
import service from '../common/js/req'

export function uploadFile (url, file, callback) {
  const parUrl = process.env.BASE_API + url
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    // 进度条回调
    onUploadProgress: function (progressEvent) {
      const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
      callback && callback(percentCompleted)
    }
  }
  return service.post(parUrl, file, config)
}
