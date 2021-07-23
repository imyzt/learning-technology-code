通过Jackson的多态特性, 实现对同一控制器不同入参序列化到不同的子类对象中.

技术点:
1. @JsonTypeInfo 对基类及子类的声明, 详细使用见 https://www.jianshu.com/p/a21f1633d79c
2. @JsonTypeName 子类声明特征, 由于Spring不会自动扫描, 在`ObjectMapperConfig`中通过扫描找到所有 `BaseParam`的子类完成查找注册到`ObjectMapper`中
3. @JsonValue 使json属性反序列化时到Enum对象中, 见`ParamDTO#type`字段


主要参考文章:

1. https://www.cnblogs.com/Marydon20170307/p/13970490.html
2. https://www.jianshu.com/p/a21f1633d79c