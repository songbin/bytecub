索引规则
1. productCode + funcType + property 
2. 管理后台新建属性需要有发布操作，发布时动态调用创建模版
3. 模版前缀为 系统统一前缀 + product + funcType + property
    - 3.1 这样的话，添加文档的时候根据deviceCode前面的前缀就可以自动知道value的类型了
4. 数据结构
    - (注)开发者只能看到DeviceMessageRequest，后面的对象都是对开发者透明的
    - 4.1 DeviceMessage<br/> 
         这个是设备上传数据后，在协议里面通过decode方法都转换为这个对象
    - 4.2 不用了，要是用的话，每添加一个属性还得改协议解析代码 ~~DeviceMessageValue 抽象类，存储最终经过转换的对象<br/>
          这里对象有两类:属性和事件~~
    - 4.3 具体每个协议的对象，~~继承自DeviceMessageValue~~
    - 4.4 EsMessage  把第一步的 DeviceMessageRequest转化为该对象，然后存储到es中
    - 4.5 DeviceUpMessageBo 这是接收到设备消息后然后存储到消息队列的数据格式
   
遗留问题
1. ~~ID生成器~~ DONE
2. ~~产品属性没有缓存，这个在消息接收发布的时候是高频请求~~ DONE
3. es处理服务调用回执消息时没有激活路由

属性读取回执数据格式
<pre>
{
  "messageId":"8643080501761790846",
   "value":{"weight":41}
}
</pre>

服务调动回执数据格式
<pre>
{
   "messageId":"123",
   "result":{
     "code":200,
     "msg":"成功"
   }
}
</pre>

网关子设备上线/下线 数据格式
    <pre>
    ["deviceCode1", "deviceCode2", "deviceCode3"]
    </pre>       
