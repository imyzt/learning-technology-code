package main

type Storage interface {
	Shorten(url string, exp int64) (string, error)
	ShortLinkInfo(eid string) (interface{}, error)
	UnShorten(eid string) (string, error)
}
