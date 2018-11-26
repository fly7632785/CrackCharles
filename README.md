# CrackCharles
通过javassit框架来破解charles

破解charles:

1、官网下载dmg安装，然后打开包内容，然后找到charles.jar包

2、jd-GUI打开jar包，分析，通过字符串文字来找到对应的类、方法等，一般都是混淆的要耐心寻找

3、使用javassit框架来进行jar包的class文件修改（javassit主要就是可以修改字节码文件），一般都是一个变量来控制的，修改其方法的返回值为true即可

4、然后重新生成class文件流，然后写入到对应的包名对应的文件目录下

5、然后调用jar uvf xxx.jar xxx/xxx.class来修改jar包下面的class类，然后再替换到原来的app的包内容下即可
