package main

import (
	"log"
	"os"
	"strconv"
)

type Env struct {
	S Storage
}

func getEnv() *Env {
	addr := os.Getenv("APP_REDIS_ADDR")
	if addr == "" {
		addr = "localhost:6379"
	}
	pwd := os.Getenv("APP_REDIS_PASSWD")
	if pwd == "" {
		pwd = ""
	}
	dbs := os.Getenv("APP_REDIS_DB")
	if dbs == "" {
		dbs = "0"
	}
	db, err := strconv.Atoi(dbs)
	if err != nil {
		log.Fatal(err)
	}
	log.Printf("connect to redis (addr: %s pwd: %s db: %d)", addr, pwd, db)

	r := NewRedisCli(addr, pwd, db)
	return &Env{S: r}
}
