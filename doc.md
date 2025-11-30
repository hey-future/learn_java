# 爱知汇开放接口

# 概要

国开数据中台提供标准数据接口获取对应项目下的资源文件

# 前提

项目编号以及统一认证的账号密码由媒资人员提供

# 接口名字及地址

- 正式环境接口域名：domain= dms.multimediapress.cn

## 统一认证

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/token

- 参数配置：JSON

- 请求头：Content-type:application/json

- 详细参数：

  | 字段名   | 字段类型 | 字段值 | 字段描述 | 是否必传 | 备注                 |
    | -------- | -------- | ------ | -------- | -------- | -------------------- |
  | userName | 字符串   |        | 用户名   | 是       | 媒资系统提供         |
  | password | 字符串   |        | 密码     | 是       | 密码需要base64加密下 |

- 请求类型：POST请求

- 返回值格式：JSON

- 返回值字段：

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | string   | 对应错误信息等                                               |
  | data   | object   | 数据                                                         |

- 返回值示例：

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "name": "系统管理员",
          "id": 1,
          "avatar": null,
          "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IndhbmdoYWlsb25nIn0.bKfGOPo5bc2rrto5TLy5qcRnsOZkL0zmFEpTuNNL0oQ"
      }
  }
  ```

## 获取课程列表

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/list/resource

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json

- 请求参数

  | 字段名   | 字段类型 | 字段值 | 字段描述   | 是否必传 | 备注         |
    | -------- | -------- | ------ | ---------- | -------- | ------------ |
  | nodeId   | 数字     |        | 资源节点id | 是       | 由接入方提供 |
  | page     | 数字     | 1      | 页码       | 否       |              |
  | pageSize | 数字     | 20     | 每页展示数 | 否       |              |

- 返回值字段

  | 字段名          | 字段类型 | 字段描述                                                     |
    | --------------- | -------- | ------------------------------------------------------------ |
  | code            | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg             | String   | 对应错误信息等                                               |
  | data            | object   | 数据                                                         |
  | data.total      | int      | 总数                                                         |
  | data.pages      | int      | 页数                                                         |
  | data.isFirst    | boolean  | 是否是第一页                                                 |
  | data.isLast     | boolean  | 是否是最后一页                                               |
  | data.prePage    | int      | 前一页                                                       |
  | data.nextPage   | int      | 后一页                                                       |
  | data.data       | array    |                                                              |
  | data.data.title | string   | 资源名称                                                     |
  | data.data.id    | int      | 资源id                                                       |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "isFirst": true,
          "total": 5,
          "pages": 1,
          "data": [
              {
                  "title": "数字化赋能上海",
                  "id": 58,
                  "nodeId": 6133,
                  "isTop": 0,
                  "topTime": 0,
                  "status": "草稿",
                  "progress": 100
              }
          ],
          "isLast": true,
          "prePage": 0,
          "nextPage": 0,
          "relationNode": 0
      }
  }
  ```



## 获取课程下的文件列表

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/childList

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json

- 请求参数

  | 字段名     | 字段类型 | 字段值 | 字段描述   | 是否必传 | 备注         |
    | ---------- | -------- | ------ | ---------- | -------- | ------------ |
  | parentNode | 数字     |        | 资源节点id | 是       | 由接入方提供 |
  | pid        | 数字     | 0      | 分类id     | 否       |              |
  | page       | 数字     | 1      | 页码       | 否       |              |
  | pageSize   | 数字     | 20     | 每页展示数 | 否       |              |

- 返回值字段

  | 字段名               | 字段类型 | 字段描述                                                     |
    | -------------------- | -------- | ------------------------------------------------------------ |
  | code                 | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg                  | String   | 对应错误信息等                                               |
  | data                 | object   | 数据                                                         |
  | data.total           | int      | 总数                                                         |
  | data.pages           | int      | 页数                                                         |
  | data.isFirst         | boolean  | 是否是第一页                                                 |
  | data.isLast          | boolean  | 是否是最后一页                                               |
  | data.prePage         | int      | 前一页                                                       |
  | data.nextPage        | int      | 后一页                                                       |
  | data.data            | array    |                                                              |
  | data.data.title      | string   | 文件名称                                                     |
  | data.data.id         | int      | 文件id                                                       |
  | data.data.suffix     | string   | 文件格式                                                     |
  | data.data.pid        | int      | 文件分类id，如上传时未指定pid，则pid为0   [此处获得的id](#获取资源分类列表) |
  | data.data.riskLevel  | string   | 智能审核的风险等级，如果未审核值为未识别                     |
  | data.data.size       | string   | 文件大小                                                     |
  | data.data.createDate | string   | 创建时间                                                     |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "isFirst": true,
          "total": 16,
          "pages": 1,
          "data": [
              {
                  "riskLevel": "高危",
                  "id": 249163,             
                  "title": "bxlss测试",               
                  "suffix": "mp4",
                  "size": "293261095",
                  "createDate": "2024-04-19 11:02:49",     
                  "pid": 249063
              }
          ],
          "isLast": true,
          "prePage": 0,
          "nextPage": 0
      }
  }
  ```


## 获取文件详情

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/file/info/{fileId}

- 请求方式：GET

- 请求头：Authorization: 验证token    Content-type:application/json

- 请求参数

  | 字段名 | 字段类型 | 字段值 | 字段描述 | 是否必传 | 备注                                                         |
    | ------ | -------- | ------ | -------- | -------- | ------------------------------------------------------------ |
  | fileId | 数字     |        | 文件id   | 是       | 文件id，可根据获取课程下的文件列表获取 [获取资源下文件资源列表接口](#获取课程下的文件资源列表) |

- 返回值字段

  | 字段名               | 字段类型 | 字段描述                                                     |
    | -------------------- | -------- | ------------------------------------------------------------ |
  | code                 | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg                  | String   | 对应错误信息等                                               |
  | data                 | object   | 数据                                                         |
  | data.id              | int      | 文件id                                                       |
  | data.title           | string   | 文件名称                                                     |
  | data.photo           | string   | 封面图                                                       |
  | data.suffix          | string   | 文件格式                                                     |
  | data.duration        | int      | 时长，音视频才有                                             |
  | data.size            | int      | 字节                                                         |
  | data.folder          | string   | 所属目录名称                                                 |
  | data.folderId        | int      | 所属目录编号，也就是pid                                      |
  | data.keyword         | string   | 关键词                                                       |
  | data.summary         | string   | 描述                                                         |
  | data.printCount      | int      | 印次                                                         |
  | data.censorStatus    | int      | 审核状态，0待审1通过2需人工确认3未通过4失败5审核中           |
  | data.resikLevel      | int      | 风险等级，0风险未识别，1无风险2需复核3高危4已排除            |
  | data.applicationType | int      | 应用类型，                                                   |
  | data.mediaType       | int      | 媒体类型，1 文本类，2微课类，3图形/图像类，4音频类，5视频类，6动画类，7虚拟仿真类，8PPT演示文稿，9网页课件，10富文本，11其他 |

- 返回值格式

  ```json
  {
  "code":200,
  "msg":"",
  "data":{
          "id":1,//文件编号
          "title":"文件名字",
          "photo":"",//封面图
          "suffix":"mp4",//文件格式
          "duration":1,//时长，音视频才有
          "size":11,//字节
          "folder":""//所属目录名字
          "folderId":11//所属目录编号，也就是pid
          "keyword":"",//关键词
          "summary":"",//描述
          "printCount":1,//印次
          "censorStatus":0,//审核状态，0待审1通过2需人工确认3未通过4失败5审核中
          "resikLevel":0,//风险等级，0风险未识别，1无风险2需复核3高危4已排除
          "applicationType":0,//应用类型
          "mediaType":,1//媒体类型
  	}
  }
  ```

## 文件下载

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/book/download/{fileId}

- 请求方式：GET

- 请求头：Authorization: 验证token    Content-type:application/json

- 请求参数

  | 字段名 | 字段类型 | 字段值 | 字段描述 | 是否必传 | 备注                                                         |
    | ------ | -------- | ------ | -------- | -------- | ------------------------------------------------------------ |
  | fileId | 数字     |        | 文件id   | 是       | 文件id，可根据获取课程下的文件列表获取 [获取课程下文件列表接口](#获取课程下的文件资源列表) |

- 响应类型

  ```
  application/octet-stream
  ```

## 获取课程分类列表

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/tree/parentId/{parentId}/pnode/{pnode}

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json

- 请求参数

  | 字段名   | 字段类型 | 字段值 | 字段描述   | 是否必传 | 备注                               |
    | -------- | -------- | ------ | ---------- | -------- | ---------------------------------- |
  | parentId | 数字     |        | 资源id     | 是       | 资源id，可根据获取资源列表接口获取 |
  | pnode    | 数字     |        | 资源节点id | 是       | 由接入方提供                       |

- 返回值字段

  | 字段名             | 字段类型 | 字段描述                                                     |
    | ------------------ | -------- | ------------------------------------------------------------ |
  | code               | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg                | String   | 对应错误信息等                                               |
  | data               | object   | 数据                                                         |
  | data.count         | int      | 总数                                                         |
  | data.data          | array    |                                                              |
  | data.data.title    | string   | 分类名称                                                     |
  | data.data.id       | int      | 分类id                                                       |
  | data.data.pid      | int      | 上级分类id                                                   |
  | data.data.hasChild | int      | 是否有字分类  1有  0 无                                      |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "data": [
              {
                  "id": 249063,
                  "title": "测试",
                  "hasChild": 0,
                  "pid": 0
              }
          ],
          "count": 6
      }
  }
  ```

## 文件上传（小文件,文件大小 < 5M）

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/upload/ai/{ai}

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:multipart/form-data;

- 请求参数

    - 路径参数

  | 字段名 | 字段类型 | 字段值 |              字段描述               | 是否必传 | 备注 |
    | :----: | :------: | :----: | :---------------------------------: | :------: | :--: |
  |   ai   |   数字   |        | 是否开启自动智能审核 0不开启  1开启 |    是    |      |

    - 表单中的数据

  |   字段名   | 字段类型 | 字段值 |  字段描述  | 是否必传 |                             备注                             |
    | :--------: | :------: | :----: | :--------: | :------: | :----------------------------------------------------------: |
  | parentNode |   数字   |        | 资源节点id |    是    |                         由接入方提供                         |
  |  parentId  |   数字   |        |   资源id   |    是    | 资源id，可根据获取资源列表接口获取  [资源列表接口](#获取资源列表) |
  |    pid     |   数字   |   0    | 资源分类id |    是    | 资源分类id，默认0，可通过获取资源分类列表获取  [资源分类接口](#获取资源分类列表) |
  |    file    |   文件   |        |    文件    |    是    |                           文件file                           |
  | thirdHash | String |  |文件的哈希值，通过 sha1 算法，由 文件名 + parentNode + parentId 计算而成 | 否 | 用于版权认证 |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | object   | 上传成功后文件的id                                           |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": 249187
  }
  ```

## 文件上传（大文件，文件大小 > 5M，分片上传）

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/fileup/nodeId/{nodeId}

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:multipart/form-data;

- 请求参数

  | 字段名           | 字段类型 | 字段值                                           | 字段描述     | 是否必传 | 备注                                                         |
    | ---------------- | -------- | ------------------------------------------------ | ------------ | -------- | ------------------------------------------------------------ |
  | parentNode       | 数字     | 6127                                             | 媒体节点     | 是       | 资源节点id，由接入方提供                                     |
  | parentId         | 数字     | 52                                               | 资源id       | 是       | 资源id，可根据获取资源列表接口获取  [资源列表接口](#获取资源列表) |
  | pid              | 数字     | 0                                                | 资源节点id   | 否       | 资源分类id，可通过获取资源分类列表获取  [资源分类接口](#获取资源分类列表) |
  | chunkNumber      | 数字     | 0                                                | 当前文件块   | 是       | 当前文件块                                                   |
  | identifier       | string   | be091cc71fddb75b336b49079861e0_0_1723440912804_0 | 文件标识     | 是       | 文件标识                                                     |
  | chunkSize        | 数字     | 5242880                                          | 分块大小     | 是       | 分块大小                                                     |
  | currentChunkSize | 数字     | 5242880                                          | 当前分块大小 | 是       | 当前分块大小                                                 |
  | totalSize        | 数字     | 699775416                                        | 总大小       | 是       | 总大小                                                       |
  | totalChunks      | 数字     | 134                                              | 总块数       | 是       | 总块数                                                       |
  | fileName         | string   | 操作流程                                         | 文件名       | 是       | 文件名   **注意每次上传时文件名要相同**                                                      |
  | file             | File     |                                                  | 文件         | 是       | 文件，二进制文件                                             |
  | uid              | string   | 1723440906493                                    |              | 否       |                                                              |
  | ai               | 数字     |                                                  |              | 是       | 是否开启自动智能审核  0 不开启 1 开启                        |
  | thirdHash | String |  |文件的哈希值，通过 sha1 算法，由 文件名 + parentNode + parentId 计算而成 ，文件名用原文件名，不要用分片的文件名| 否 | 用于版权认证 |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | object   | 上传成功后文件的id                                           |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": 249186
  }
  ```


## 分片上传，检查当前片段是否上传过

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/upload/isUpload?md5={md5}

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  | 字段名 | 字段类型 |                       字段值                        | 字段描述 | 是否必传 |                             备注                             |
    | :----: | :------: | :-------------------------------------------------: | :------: | :------: | :----------------------------------------------------------: |
  |  md5   |  字符串  | da813c91d1e200aa111f67331f41978e_0_1723440912804_15 | 文件表示 |    是    | 分片上传时文件的的标识identifier  [文件标识](#文件上传（分片上传）) |

- 返回值字段

  | 字段名      | 字段类型 | 字段描述                                                     |
    | ----------- | -------- | ------------------------------------------------------------ |
  | code        | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg         | String   | 对应错误信息等                                               |
  | data        | object   | 上传成功后文件的id                                           |
  | data.status | int      | 状态  0 未上传   1 已上传   2 上传中                         |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": {
  		"status": 0
  	}
  }
  ```


## 文件分片上传，前端逻辑

- 大文件分片上传时，前端需要将文件分成每片为 5M的文件然后上传。
- 大文件上传需要配合  [大文件上传接口](##文件上传（大文件，文件大小 > 5M，分片上传）)    [分片上传，断点检查接口](##分片上传，检查当前片段是否上传过)

```js
methods: {

// 获取md5
  readFileMD5(file) {
    // 读取每个文件的md5
    return new Promise(async(resolve, reject) => {
      try {
        const fileRederInstance = new FileReader()
        fileRederInstance.readAsBinaryString(file)
        let fileMD5 = ''
        fileRederInstance.addEventListener('load', e => {
          const fileBolb = e.target.result
          fileMD5 = md5(fileBolb)
          resolve(fileMD5)
        }, false)
      } catch (e) {
        reject(e)
      }
    })
  },
  // 文件分块,利用Array.prototype.slice方法
  splitFile(file, eachSize, chunks) {
    // eachSize: 5 * 1024 * 1024, // 每块文件大小  chunks 分片数量
    return new Promise((resolve, reject) => {
      try {
        setTimeout(() => {
          const fileChunk = []
          for (let chunk = 0; chunks > 0; chunks--) {
            fileChunk.push(file.slice(chunk, chunk + eachSize))
            chunk += eachSize
          }
          resolve(fileChunk)
        }, 0)
      } catch (e) {
        reject(new Error('文件切块发生错误'))
      }
    })
  },
  fileUpload(file, data, onProgress) {
      return new Promise(async(resolve, reject) => {
        try {
          const { eachSize } = this
          const chunks = Math.ceil(file.size / eachSize)
          const fileChunks = await this.splitFile(file, eachSize, chunks)
          let currentChunk = 0
          const index = this.uploadList.findIndex(val => val.uid === file.uid)
          let currentFile = {} // 当前文件信息
          if (index !== -1) {
            // 记录之前传的分片数，从第几片开始传
            currentChunk = parseInt(this.uploadList[index].percentage / 100 * chunks)
            currentFile = this.uploadList[index]
          }
          const timestamp = new Date().getTime()
          for (let i = 0; i < fileChunks.length; i++) {
            // 服务端检测已经上传到第currentChunk块了，那就直接跳到第currentChunk块，实现断点续传
            if (Number(currentChunk) === i) {
              // 每块上传完后则返回需要提交的下一块的index
              const md5 = await this.readFileMD5(fileChunks[i]) + '_' + index + '_' + timestamp + '_' + i
              const isValidate = await this.validateFile(md5, {
                chunked: true,
                chunkNumber: i, // 当前文件块
                identifier: md5, // identifier
                chunkSize: eachSize, //	每块文件大小
                currentChunkSize: fileChunks[i].size, // 	当前文件块大小
                totalSize: file.size, // 总大小
                totalChunks: chunks, // 	总块数
                fileName: file.name, // 文件名字
                file: fileChunks[i], // 	文件
                uid: file.uid,
                // type: file.type // 文件类型
              }) // 0未上传过1已上传2上传进行中
              if (!isValidate) {
                const { code, msg } = await this.postFile({
                  parentNode: this.nodeId,
                  parentId: this.id,
                  pid: data.pid,
                  id: data.id,
                  chunked: true,
                  chunkNumber: i, // 当前文件块
                  identifier: md5, // identifier
                  chunkSize: eachSize, //	每块文件大小
                  currentChunkSize: fileChunks[i].size, // 	当前文件块大小
                  totalSize: file.size, // 总大小
                  totalChunks: chunks, // 	总块数
                  fileName: file.name, // 文件名字
                  file: fileChunks[i], // 	文件
                  uid: file.uid,
                  fieldId: 940
                  // type: file.type // 文件类型
                })
                if (code === 200) {
                  currentChunk++
                  const percentObj = { percent: 0 }
                  percentObj.percent = Number((((i * eachSize + fileChunks[i].size) / file.size) * 100).toFixed(2))
                  percentObj.percent = percentObj.percent > 100 ? 100 : percentObj.percent
                  percentObj.percent = this.uploadList[index].percentage > percentObj.percent ? this.uploadList[index].percentage : percentObj.percent // 如果之前上传的文件大于当前进度，显示之前的上传进度
                  console.log('percent1:' + percentObj.percent)
                  onProgress({ percent: percentObj.percent })
                } else {
                  this.$message.error(msg)
                }
              } else {
                currentChunk++
                const percentObj = { percent: 0 }
                percentObj.percent = Number((((i * eachSize + fileChunks[i].size) / file.size) * 100).toFixed(2))
                percentObj.percent = percentObj.percent > 100 ? 100 : percentObj.percent
                percentObj.percent = this.uploadList[index].percentage > percentObj.percent ? this.uploadList[index].percentage : percentObj.percent // 如果之前上传的文件大于当前进度，显示之前的上传进度
                console.log('percent2:' + percentObj.percent)
                onProgress({ percent: percentObj.percent })
              }
            }
          }
          resolve()
        } catch (e) {
          reject(e)
        }
      })
    },
    // 提交文件方法,将参数转换为FormData, 然后通过axios发起请求
    postFile(param) {
      const formData = new FormData()
      for (const p in param) {
        if (param[p] !== undefined) {
          formData.append(p, param[p])
        }
      }
      const { requestCancelQueue } = this
      const config = {
      // 中断接口请求的配置，不需要可以不加
        cancelToken: new axios.CancelToken(function executor(cancel) {
          if (requestCancelQueue[param.uid]) {
            requestCancelQueue[param.uid]()
            delete requestCancelQueue[param.uid]
          }
          requestCancelQueue[param.uid] = cancel
        })
      }
      // 调用上传接口
      return uploadRequest(133, param.id, formData, config).then(response => response)
    },
    // 文件校验方法
    validateFile(md5, param) {
      const { requestCancelQueue } = this
      const config = {
        cancelToken: new axios.CancelToken(function executor(cancel) {
          if (requestCancelQueue[param.uid]) {
            requestCancelQueue[param.uid]()
            delete requestCancelQueue[param.uid]
          }
          requestCancelQueue[param.uid] = cancel
        })
      }
      // 使用axios调用isUpload接口判断文件是否上传，返回接口数据状态
      return isUpload(md5, config).then(rs => rs.data.status)
    },
},
```



## 获取课程资源节点

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/resource/tree

- 请求方式：GET

- 请求头：Authorization: 验证token    Content-type:application/json;

- 返回值字段

  只需关注 有字段描述的即可

  | 字段名                    | 字段类型 | 字段描述                                                     |
    | ------------------------- | -------- | ------------------------------------------------------------ |
  | code                      | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg                       | string   | 对应错误信息等                                               |
  | data                      | object   | 上传成功后文件的id                                           |
  | data.id                   | int      | 资源库节点id                                                 |
  | data.parent               | int      | 所属上级                                                     |
  | data.isDisplay            | int      |                                                              |
  | data.type                 | int      |                                                              |
  | data.nodeType             | int      |                                                              |
  | data.name                 | string   | 资源库名称                                                   |
  | data.permsId              | int      |                                                              |
  | data.icon                 | string   |                                                              |
  | data.hasPerms             | boolean  |                                                              |
  | data.recommend            | boolean  |                                                              |
  | data.quote                | boolean  |                                                              |
  | data.excelImport          | boolean  |                                                              |
  | data.projectId            | int      |                                                              |
  | data.hasChild             | int      | 是否有子集  0 没有  1 有                                     |
  | data.extra                | string   |                                                              |
  | data.origins              | int      |                                                              |
  | data.children             | arrays   | 下级资源库                                                   |
  | data.children.id          | int      | 资源库id                                                     |
  | data.children.parent      | int      | 所属上级id                                                   |
  | data.children.isDisplay   | int      |                                                              |
  | data.children.type        | int      |                                                              |
  | data.children.nodeType    | int      |                                                              |
  | data.children.name        | string   | 资源库名称                                                   |
  | data.children.permsId     | int      |                                                              |
  | data.children.icon        | string   |                                                              |
  | data.children.hasPerms    | int      |                                                              |
  | data.children.recommend   | boolean  |                                                              |
  | data.children.quote       | boolean  |                                                              |
  | data.children.excelImport | boolean  |                                                              |
  | data.children.projectId   | int      |                                                              |
  | data.children.hasChild    | int      |                                                              |
  | data.children.extra       | string   |                                                              |
  | data.children.origins     | int      |                                                              |
  | data.children.children    | arrays   |                                                              |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": [
          {
              "id": 6079,
              "parent": 6082,
              "isDisplay": 1,
              "type": 0,
              "nodeType": 7,
              "name": "职业教育资源库",
              "permsId": null,
              "icon": "jiedian1",
              "hasPerms": false,
              "recommend": false,
              "quote": false,
              "excelImport": false,
              "projectId": 1,
              "hasChild": 1,
              "extra": null,
              "origins": 1,
              "children": [
                  {
                      "id": 6090,
                      "parent": 6079,
                      "isDisplay": 1,
                      "type": 1,
                      "nodeType": 7,
                      "name": "建筑学",
                      "permsId": null,
                      "icon": "",
                      "hasPerms": false,
                      "recommend": false,
                      "quote": false,
                      "excelImport": false,
                      "projectId": 1,
                      "hasChild": 0,
                      "extra": null,
                      "origins": 1,
                      "children": null
                  },
                  {
                      "id": 6091,
                      "parent": 6079,
                      "isDisplay": 1,
                      "type": 1,
                      "nodeType": 7,
                      "name": "现代物流管理（广西职院）",
                      "permsId": null,
                      "icon": "",
                      "hasPerms": false,
                      "recommend": false,
                      "quote": false,
                      "excelImport": false,
                      "projectId": 1,
                      "hasChild": 0,
                      "extra": null,
                      "origins": 1,
                      "children": null
                  }
              ]
          }
      ]
  }
  ```

## 创建课程

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/create/resource

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  |    字段名    | 字段类型 | 字段值 |                           字段描述                           | 是否必传 |     备注     |
    | :----------: | :------: | :----: | :----------------------------------------------------------: | :------: | :----------: |
  |    nodeId    |   int    |        |                         资源库节点id                         |    是    | 由接入方提供 |
  |    title     |  string  |        |                           资源名称                           |    是    |              |
  |  mediaType   |   int    |        | 媒体类型   1：视频课程  3：网络课程 8：音频课程 21：微课程 27：实验 28：实习  29：素材 30：直播课 |    否    |              |
  |  resolution  |  string  |        |                   分辨率（音视频资源需要）                   |    否    |              |
  |   duration   |  string  |        |                    时长（音视频资源需要）                    |    否    |              |
  |    counts    |   int    |        |                    条数（音视频资源需要）                    |    否    |              |
  | publishTime  |  string  |        |                  录制时间（音视频资源需要）                  |    否    |              |
  |   publish    |  string  |        |                  出版单位（文本类资源需要）                  |    否    |              |
  |    record    |  string  |        |                  录制单位（音视频资源需要）                  |    否    |              |
  |   issuing    |  string  |        |                  发行单位（文本类资源需要）                  |    否    |              |
  |    editor    |  string  |        |                    主讲（音视频资源需要）                    |    否    |              |
  |   keyword    |  string  |        |                            关键词                            |    否    |              |
  |   identity   |  string  |        |                  主讲身份（音视频资源需要）                  |    否    |              |
  |   book_no    |  string  |        |                    书号（文本类资源需要）                    |    否    |              |
  | chief_editor |  string  |        |                    主编（文本类资源需要）                    |    否    |              |
  |     tag      |  string  |        |                             标签                             |    否    |              |
  |   summary    |  string  |        |                           内容描述                           |    否    |              |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | int      | 创建的资源id                                                 |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": 70
  }
  ```


## 修改课程

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/update/resource

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  获取资源详情会获取所有值，根据需要修改其中的字段值

  |    字段名    | 字段类型 | 字段值 |                           字段描述                           | 是否必传 |     备注     |
    | :----------: | :------: | :----: | :----------------------------------------------------------: | :------: | :----------: |
  |    nodeId    |   int    |        |                         资源库节点id                         |    是    | 由接入方提供 |
  |      id      |   int    |        |                            资源id                            |    是    |              |
  |    title     |  string  |        |                           资源名称                           |    是    |              |
  |  mediaType   |   int    |        | 媒体类型   1：视频课程  3：网络课程 8：音频课程 21：微课程 27：实验 28：实习  29：素材 30：直播课 |    是    |              |
  |  resolution  |  string  |        |                   分辨率（音视频资源需要）                   |    是    |              |
  |   duration   |  string  |        |                    时长（音视频资源需要）                    |    是    |              |
  |    counts    |   int    |        |                    条数（音视频资源需要）                    |    是    |              |
  | publishTime  |  string  |        |                  录制时间（音视频资源需要）                  |    是    |              |
  |   publish    |  string  |        |                  出版单位（文本类资源需要）                  |    是    |              |
  |    record    |  string  |        |                  录制单位（音视频资源需要）                  |    是    |              |
  |   issuing    |  string  |        |                  发行单位（文本类资源需要）                  |    是    |              |
  |    editor    |  string  |        |                    主讲（音视频资源需要）                    |    是    |              |
  |   keyword    |  string  |        |                            关键词                            |    是    |              |
  |   identity   |  string  |        |                  主讲身份（音视频资源需要）                  |    是    |              |
  |   book_no    |  string  |        |                    书号（文本类资源需要）                    |    是    |              |
  | chief_editor |  string  |        |                    主编（文本类资源需要）                    |    是    |              |
  |     tag      |  string  |        |                             标签                             |    是    |              |
  |   summary    |  string  |        |                           内容描述                           |    是    |              |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | int      | 资源id                                                       |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": 70
  }
  ```

## 删除课程

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/delete/resource/nodeId/{nodeId}/id/{id}

- 请求方式：GET

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  | 字段名 | 字段类型 | 字段值 |   字段描述   | 是否必传 |     备注     |
    | :----: | :------: | :----: | :----------: | :------: | :----------: |
  | nodeId |   int    |        | 资源库节点id |    是    | 由接入方提供 |
  |   id   |   int    |        |    资源id    |    是    |              |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | boolean  | 是否删除成功                                                 |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": true
  }
  ```

## 获取课程详情获取资源详情（包含资源属性，资源分类）

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/resource/info/nodeId/{nodeId}/id/{id}

- 请求方式：GET

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  | 字段名 | 字段类型 | 字段值 |   字段描述   | 是否必传 |     备注     |
    | :----: | :------: | :----: | :----------: | :------: | :----------: |
  | nodeId |   int    |        | 资源库节点id |    是    | 由接入方提供 |
  |   id   |   int    |        |    资源id    |    是    |              |

- 返回值字段

  | 字段名                 | 字段类型 | 字段描述                                                     |
    | ---------------------- | -------- | ------------------------------------------------------------ |
  | code                   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg                    | String   | 对应错误信息等                                               |
  | data                   | object   | 数据                                                         |
  | data.data              | object   | 资源信息                                                     |
  | data.data.id           | int      | 资源id                                                       |
  | data.data.issuing      | string   | 发行单位                                                     |
  | data.data.summary      | string   | 内容描述                                                     |
  | data.data.publishTime  | string   | 录制时间                                                     |
  | data.data.editor       | string   | 主讲                                                         |
  | data.data.counts       | int      | 条数                                                         |
  | data.data.chief_editor | string   | 主编                                                         |
  | data.data.mediaType    | int      | 媒体类型   1：视频课程  3：网络课程 8：音频课程 21：微课程 27：实验 28：实习  29：素材 30：直播课 |
  | data.data.title        | string   | 资源名称                                                     |
  | data.data.resolution   | string   | 分辨率                                                       |
  | data.data.book_no      | string   | 书号                                                         |
  | data.data.duration     | string   | 时长                                                         |
  | data.data.identity     | string   | 主讲身份                                                     |
  | data.data.publish      | string   | 出版单位                                                     |
  | data.data.record       | string   | 录制单位                                                     |
  | data.data.tag          | string   | 标签                                                         |
  | data.data.keyword      | string   | 关键词                                                       |
  | data.data.nodeId       | int      | 资源节点id                                                   |
  | data.folder            | arrays   | 资源分类                                                     |
  | data.folder.id         | int      | 分类id                                                       |
  | data.folder.subFolder  | int      | 下级分类                                                     |
  | data.folder.pid        | int      |                                                              |
  | data.folder.title      | string   | 分类名称                                                     |
  | data.folder.count      | int      | 数量                                                         |

- 返回值格式

  ```
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "folder": [
              {
                  "id": 248998,
                  "subFolder": 0,
                  "pid": 0,
                  "title": "知识点",
                  "count": 88
              },
              {
                  "id": 248889,
                  "subFolder": 0,
                  "pid": 0,
                  "title": "啊啊",
                  "count": 54
              }
          ],
          "data": {
              "issuing": null,
              "summary": null,
              "publishTime": null,
              "editor": null,
              "counts": null,
              "chief_editor": null,
              "mediaType": 1,
              "title": "测试",
              "resolution": null,
              "book_no": null,
              "duration": null,
              "identity": null,
              "publish": null,
              "record": null,
              "id": 32,
              "tag": null,
              "keyword": null,
              "nodeId": 6127
          }
      }
  }
  ```

## 更新文件封面图

- 接口地址：https://dms.multimediapress.cn/dms/api/v1/update/file/photo

- 请求方式：POST

- 请求头：Authorization: 验证token    Content-type:application/json;

- 请求参数

  | 字段名 | 字段类型 | 字段值 |   字段描述    | 是否必传 | 备注 |
    | :----: | :------: | :----: | :-----------: | :------: | :--: |
  | photo  |  string  |        | 封面图地址url |    是    |      |
  |   id   |   int    |        |    资源id     |    是    |      |

- 返回值字段

  | 字段名 | 字段类型 | 字段描述                                                     |
    | ------ | -------- | ------------------------------------------------------------ |
  | code   | int      | 状态码 200成功，500失败，401会话过期需要重新调用此接口获取token |
  | msg    | String   | 对应错误信息等                                               |
  | data   | object   |                                                              |

- 返回值格式

  ```
  {
  	"code": 200,
  	"msg": "成功",
  	"data": null
  }
  ```
 