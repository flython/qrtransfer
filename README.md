# 背景
一个利用二维码从远程桌面下载文件的工具。

当前，云桌面等技术的运用日益广泛，我们通过一个远程桌面工具即可实现远程开发和运维。
为了保证数据不泄露，远程桌面往往是禁用了拷贝文件到本地的功能。

然而，大部分产商在保证安全的时候，却没有配套的措施能够满足高效运维的需求：

例如，当我们通过云桌面部署的服务发生了异常并需要紧急修复，我们需要把日志或者运行dump文件拿到本地来分析，
云桌面却没有配备日志导出功能，这时候只能求爷爷告奶奶的去联系运维打车过来帮你拷文件，或者打开一个UltraEdit等软件手工抄写下来。。无论如何，这都要花掉很长时间。

此时，我们可以用这个工具，模仿打开UltraEdit对着屏幕抄写的过程，将日志文件"抄写"到本地

# 工作原理
远程桌面上的文件 -> 生成二维码图像 -> 读图像中的数据 -> 存本地文件

# 使用说明

下载最新的包
https://github.com/codingmiao/qrtransfer/releases/download/v1.0/qrtransfer_v1.0.zip

然后[点击这里](doc/manual.md)按步骤配置和启动


# 声明

## 如果你是运维/开发等角色
这个工具是用来方便运维的同学快速排查文件的，不是拿来偷取公司机密的，你只应该拿它来传输你可以对着屏幕抄写下来的文件，请谨记。
这个软件的性能十分差劲，只有5kb/s左右的最高传输速度，传输过程会比较长，是很容易被抓到的，不要有侥幸心理。

## 如果你是云桌面的产商/公司机密管理者等角色
担心这个软件会泄露机密，那你可以通过定期抓取屏幕截图的方式来分析用户是否有违规行为（包括前面说的用UltraEdit等软件手工抄写下来等行为）。
这个软件的性能十分差劲，只有5kb/s左右的最高传输速度，传输过程会比较长，是很容易抓到不正当行为的。
同时，也请思考这个软件在什么情况下会出现在你的管辖范围内，是缺乏完善的开发运维工具体系？是管理机制逼的太紧？还是没有应急预案？因为这个软件的目的就是为了辅助快速通过日志等文件来排查紧急问题。

## 再次强调

本软件使用读取屏幕图像的方式从远程桌面拷贝文件，其运作原理与您用ByteEdit、UltraEdit等软件从远程桌面查看并抄写字节到本地文件是一样的，并不具备泄密特性。

但是，本软件仅供拷贝运行命令、分析参数等非敏感信息，作紧急排查问题等用途。

使用本软件时，请严格准守相关法律法规及贵公司各项规定，不要拷被任何涉密文件。

如您擅自违反上述法律法规及规定，造成的任何责任将完全由您自行承担。

