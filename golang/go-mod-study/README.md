


| 指令 | 作用 |
| --- | --- |
| go mod init <module_name> | 初始化mod |
| go mod graph | 展示项目的依赖包 |
| go mod download | 下载依赖包到 go path 中的 mod目录下，go mod的依赖包还是放在gopath下的 |
| go mod tidy | 优化依赖，如不需要的依赖会从go.mod中移除,如需要则会进行添加 |
| go mod verify | 验证依赖包是否有问题（版本不存在等），会对比源代码hash，如源码发生改变则不通过检查 |
| go mod why -m <module_name> | 指定工具包输出哪些模块在使用 |
| go mod edit -format | 命令行修改go.mod文件 |
| go mod vendor | 冗余一份依赖包到本工程目录下，便于DevOps依赖打包 |

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

4. go mod edit

    1. go mod edit -module <new_module_name>
```
➜  go-mod-study git:(master) ✗ cat go.mod           
module go-study-test

go 1.15

require github.com/hashicorp/golang-lru v0.5.4
➜  go-mod-study git:(master) ✗ go mod edit -module go-mod-study 
➜  go-mod-study git:(master) ✗ cat go.mod                      
module go-mod-study

go 1.15

require github.com/hashicorp/golang-lru v0.5.4
```

    2. go mod edit -json

```
➜  go-mod-study git:(master) ✗ go mod edit -json               
{
        "Module": {
                "Path": "go-mod-study"
        },
        "Go": "1.15",
        "Require": [
                {
                        "Path": "github.com/hashicorp/golang-lru",
                        "Version": "v0.5.4"
                }
        ],
        "Exclude": null,
        "Replace": null
}
```

    3. go mod edit -go=1.14

```
➜  go-mod-study git:(master) ✗ cat go.mod       
module go-mod-study

go 1.15

require github.com/hashicorp/golang-lru v0.5.4
➜  go-mod-study git:(master) ✗ go mod edit -go=1.14
➜  go-mod-study git:(master) ✗ cat go.mod          
module go-mod-study

go 1.14

require github.com/hashicorp/golang-lru v0.5.4
```

    4. go mod edit -fmt

```
➜  go-mod-study git:(master) ✗ cat go.mod         
module go-mod-study

  go 1.14

  require github.com/hashicorp/golang-lru v0.5.4
➜  go-mod-study git:(master) ✗ go mod edit -fmt   
➜  go-mod-study git:(master) ✗ cat go.mod      
module go-mod-study

go 1.14

require github.com/hashicorp/golang-lru v0.5.4
```

    5. go mod edit -require=path@version
```
➜  go-mod-study git:(master) ✗ cat go.mod          
module go-mod-study

go 1.14

➜  go-mod-study git:(master) ✗ go mod edit -require=github.com/hashicorp/golang-lru@v0.5.4
➜  go-mod-study git:(master) ✗ cat go.mod
module go-mod-study

go 1.14

require github.com/hashicorp/golang-lru v0.5.4

```
    6. go mod edit -exclude=path@version
    7. go mod edit -replace=old[@v]=new[@v]


5. go mod vendor

```
➜  go-mod-study git:(master) ✗ ll
total 32
-rw-r--r--  1 imyzt  staff   1.2K Nov  7 11:31 README.md
-rw-r--r--  1 imyzt  staff    77B Nov  7 11:49 go.mod
-rw-r--r--  1 imyzt  staff   181B Nov  7 11:23 go.sum
-rw-r--r--  1 imyzt  staff   270B Nov  7 11:29 main.go
➜  go-mod-study git:(master) ✗ go mod vendor
➜  go-mod-study git:(master) ✗ ll
total 32
-rw-r--r--  1 imyzt  staff   1.2K Nov  7 11:31 README.md
-rw-r--r--  1 imyzt  staff    77B Nov  7 11:49 go.mod
-rw-r--r--  1 imyzt  staff   181B Nov  7 11:23 go.sum
-rw-r--r--  1 imyzt  staff   270B Nov  7 11:29 main.go
drwxr-xr-x  4 imyzt  staff   128B Nov  7 11:50 vendor

```