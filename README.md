# jspxcms6

基于江西金智jspxcms6.1,做了一些修改和扩展,目前实现了原版本未开放的文库功能,依照原作者的[许可协议](http://www.jspxcms.com/license.html)保留了页脚相关的原作者的链接和名称.用于学习和参考

##安装

1.刷入根目录的jspxcms601.sql脚本到mysql数据库中

2.如果需要开启文库功能,还得先安装好以下几类工具和服务

	*swftools
	*xpdf
	*openoffice(此项需要开启服务)
3.修改webapp/WEB-INF/conf/custom.properties中的JDBC配置和媒体转换的命令路径和openoffice服务的IP和端口

4.导入源码到eclipse并发布到servlet2.4以上的servlet容器
访问 [http://localhost:port/cmscp/](http://localhost:port/cmscp/)
