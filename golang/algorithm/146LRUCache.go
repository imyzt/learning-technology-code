package main

import "container/list"

type LRUCache struct {
	// 当前容量
	size int
	// 总容量
	capacity int
	// 数据本身
	data map[int]*list.Element
	// 每个key最近使用时间
	lru *list.List
}

func ConstructorLRUCache(capacity int) LRUCache {
	return LRUCache{
		size:     0,
		capacity: capacity,
		data:     make(map[int]*list.Element),
		lru:      list.New(),
	}
}

type cacheItem struct {
	key   int
	value int
}

func (this *LRUCache) Get(key int) int {
	if ele, ok := this.data[key]; ok {
		this.moveToFront(ele)
		return ele.Value.(cacheItem).value
	}
	return -1
}

func (this *LRUCache) Put(key int, value int) {

	if ele, ok := this.data[key]; ok {
		this.moveToFront(ele)
		ele.Value = cacheItem{key, value}
	} else {
		if this.size >= this.capacity {
			this.removeOldest()
		}
		newEle := this.lru.PushFront(cacheItem{key, value})
		this.data[key] = newEle
		this.size++
	}
}

func (this *LRUCache) moveToFront(elem *list.Element) {
	this.lru.MoveToFront(elem)
}

func (this *LRUCache) removeOldest() {
	oldest := this.lru.Back()
	if oldest != nil {
		delete(this.data, oldest.Value.(cacheItem).key)
		this.lru.Remove(oldest)
		this.size--
	}
}
