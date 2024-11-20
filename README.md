# 背景

1. ref原声明：https://github.com/codingmiao/qrtransfer
2. Swing的BorderLayout是真难用啊！

# 改动
1. 修改了下Jdk版本，使其在Jdk8上能跑起来（你懂的）
2. 修改了画布大小，为了优化识别的速度
3. 增加了人工干预模式，可以在识别失败可以暂停下来做些补救措施
   4. ~~（如用手机扫一下然后贴出来二维码生成一下贴到屏幕上给它识别一下，累死了，还有更优雅的解决方案）~~
   4. ~~更优雅的方案可以是增加一个输入框，毕竟你用微信扫了贴过来直接填也可以~~
   5. 蠢了，直接引入另一个二维码识别框架（boofcv）就好了，zxing快是快，可是截屏识别率有点奇怪，说不行就不行
4. 截屏方式换成了从屏幕1左上角开始0,60 开始截取512长款的正方形，与sender的大小对应（后续可以做成动态配置）
5. 整完上面的东西以后识别速度可以提高到30ms完成一次识别，而且可以完全自动化（除非你超级脸黑两个库都失败）

# 存在Bug
1. 启动后总会跳过很多次又回到开头
2. 识别效率超高，但自动切页的效率不高（大概5fps)，怀疑其实是画面本身fps效率就不高，还在考虑优化方案（说不定可以但页多QR分区截取）


# 声明

## 再次强调

本软件使用读取屏幕图像的方式从远程桌面拷贝文件，其运作原理与您用ByteEdit、UltraEdit等软件从远程桌面查看并抄写字节到本地文件是一样的，并不具备泄密特性。

但是，本软件仅供拷贝运行命令、分析参数等非敏感信息，作紧急排查问题等用途。

使用本软件时，请严格准守相关法律法规及贵公司各项规定，不要拷被任何涉密文件。

如您擅自违反上述法律法规及规定，造成的任何责任将完全由您自行承担。

