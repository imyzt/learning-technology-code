java spi 机制: https://pdai.tech/md/java/advanced/java-advanced-spi.html

1. 新建接口,以及接口实现
2. 在`resources/META-INF/services`目录下新建文件,文件名为接口的全类名,文件内容为实现类的全类名
3. 新建main方法,通过`ServiceLoader.load(接口)`获取实现类,调用其方法
