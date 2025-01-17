# javabatis

#### 介绍
简单的javaweb后端代码生成工具，根据mysql数据库中的表属性生成常用java和XML代码，方便大家更加快捷地开发java相关的项目。

#### 软件架构
软件架构说明：maven、mybatis


#### 安装教程

1. 请配置好开发环境，本项目使用的是maven 3.6.1,java21和mysql 8.0；
2. 请下载好集成开发环境IntelliJ IDEA，下载的建议为旗舰版

#### 使用说明

1.  修改application.properties中的数据库配置等内容，其中输出地址建议以main结尾，内容见下：
![文件输出地址](https://foruda.gitee.com/images/1735635008648580554/50843349_13382299.png "屏幕截图")
![数据库配置](https://foruda.gitee.com/images/1735635009727621604/62c7bbf8_13382299.png "屏幕截图")
2. 在IntelliJ IDEA中运行
3. 最后可在自己设置的输出文件夹中找到生成的文件

#### 注意
由于个人水平有限，代码工具生成的文件会覆盖原同名文件，故个人建议，在使用开发之前，请在数据库中将需要的表建立完成。