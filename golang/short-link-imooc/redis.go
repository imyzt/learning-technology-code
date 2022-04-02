package main

import (
	"crypto/sha1"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/go-redis/redis"
	"github.com/mattheath/base62"
	"time"
)

const (
	// 全局计数器
	URLIDKEY = "next.url.id"
	// 短地址与源地址的映射
	ShortLinkKey = "shortlink:%s:url"
	// 地址hash与短地址的映射
	URLHashKey = "urlhash:%s:url"
	// 短地址和详情的映射
	ShortLinkDetailKey = "shortlink:%s:detail"
)

type RedisCli struct {
	Cli *redis.Client
}

type URLDetail struct {
	URL                 string        `json:"url"`
	CreatedAt           string        `json:"created_at"`
	ExpirationInMinutes time.Duration `json:"expiration_in_minutes"`
}

func NewRedisCli(add string, pwd string, db int) *RedisCli {
	c := redis.NewClient(&redis.Options{
		Addr:     add,
		Password: pwd,
		DB:       db,
	})
	if _, err := c.Ping().Result(); err != nil {
		panic(err)
	}
	return &RedisCli{Cli: c}
}

func (r *RedisCli) Shorten(url string, exp int64) (string, error) {
	h := toSha1(url)

	result, err := r.Cli.Get(fmt.Sprintf(URLHashKey, h)).Result()
	if err == redis.Nil {

	} else if err != nil {
		return "", err
	} else {
		if result == "{}" {

		} else {
			return result, nil
		}
	}

	eid, err := getEID(err, r)
	if err != nil {
		return "", err
	}

	err = r.Cli.Set(fmt.Sprintf(ShortLinkKey, eid), url, time.Minute*time.Duration((exp))).Err()
	if err != nil {
		return "", err
	}

	err = r.Cli.Set(fmt.Sprintf(URLHashKey, h), eid, time.Minute*time.Duration(exp)).Err()
	if err != nil {
		return "", err
	}

	err = cacheLinkDetail(url, exp, r, eid)
	if err != nil {
		return "", err
	}

	return eid, nil
}

func (r *RedisCli) ShortLinkInfo(eid string) (interface{}, error) {
	d, err := r.Cli.Get(fmt.Sprintf(ShortLinkDetailKey, eid)).Result()
	if err == redis.Nil {
		return "", StatusError{404, errors.New("Unknown short URL")}
	} else if err != nil {
		return "", err
	} else {
		return d, nil
	}
}

func (r *RedisCli) UnShorten(eid string) (string, error) {
	url, err := r.Cli.Get(fmt.Sprintf(ShortLinkKey, eid)).Result()
	if err == redis.Nil {
		return "", StatusError{404, err}
	} else if err != nil {
		return "", err
	} else {
		return url, nil
	}
}

func cacheLinkDetail(url string, exp int64, r *RedisCli, eid string) error {
	detail, err := json.Marshal(&URLDetail{
		URL:                 url,
		CreatedAt:           time.Now().String(),
		ExpirationInMinutes: time.Duration(exp),
	})
	if err != nil {
		return err
	}

	err = r.Cli.Set(fmt.Sprintf(ShortLinkDetailKey, eid), detail, time.Minute*time.Duration(exp)).Err()
	if err != nil {
		return err
	}
	return nil
}

func getEID(err error, r *RedisCli) (string, error) {
	err = r.Cli.Incr(URLIDKEY).Err()
	if err != nil {
		return "", err
	}

	id, err := r.Cli.Get(URLIDKEY).Int64()
	if err != nil {
		return "", err
	}

	eid := base62.EncodeInt64(id)
	return eid, err
}

func toSha1(str string) string {
	return string(sha1.New().Sum([]byte(str)))
}
