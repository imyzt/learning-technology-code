# 笔记


## CompletableFuture

1. 执行,不要结果
```java
CompletableFuture.runAsync((() -> {
    CommonUtils.print("start");
    CommonUtils.sleepSecond(1);
    CommonUtils.print("end");
}));
```

2. 执行, 要结果
```java
CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(() -> {
    return CommonUtils.readFiles("news.txt");
});
```