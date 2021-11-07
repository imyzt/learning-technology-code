
# go mod 常用指令

| 指令 | 作用 |
| --- | --- |
| go mod init <module_name> | 初始化mod |
| go mod graph | 展示项目的依赖包 |
| go mod download | 下载依赖包到 go path 中的 mod目录下，go mod的依赖包还是放在gopath下的 |
| go mod tidy | 优化依赖，如不需要的依赖会从go.mod中移除,如需要则会进行添加 |
| go mod verify | 验证依赖包是否有问题（版本不存在等），会对比源代码hash，如源码发生改变则不通过检查 |
| go mod why -m <module_name> | 指定工具包输出哪些模块在使用 |


1. go mod why

```
➜  go-mod-study git:(master) ✗ go mod why "github.com/hashicorp/golang-lru"
# github.com/hashicorp/golang-lru
go-mod-study
github.com/hashicorp/golang-lru

```

2. go mod graph

```
➜  go-mod-study git:(master) ✗ go mod graph
go-mod-study github.com/hashicorp/golang-lru@v0.5.4
```

3. go mod verify

```
➜  go-mod-study git:(master) ✗ go mod verify
all modules verified
➜  go-mod-study git:(master) ✗ go mod verify
go: github.com/hashicorp/golang-lru@v0.5.421: reading github.com/hashicorp/golang-lru/go.mod at revision v0.5.421: unknown revision v0.5.421

```