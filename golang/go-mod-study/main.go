package main

import (
	"fmt"
	lruUtil "github.com/hashicorp/golang-lru"
)

func main() {

	cache, err := lruUtil.New(128)
	if err != nil {
		fmt.Println(err)
		return
	}

	for i := 0; i < 1000; i++ {
		cache.Add(i, nil)
	}

	fmt.Println("cache len -> ", cache.Len())
}
