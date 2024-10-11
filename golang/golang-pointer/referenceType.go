package main

import (
	"encoding/json"
	"fmt"
)

func referenceType() {

	// golang 的引用类型默认也是值传递拷贝对象，如需要修改源对象，需要使用指针类型
	p := Person{
		Name: "Tom",
		Age:  18,
	}

	p.sayHi()
	p.changeAge(20)
	p.sayHi()
	p.changeAgeByReference(20)
	p.sayHi()

	// 转换为json
	jsonStr, err := json.Marshal(p)
	if err != nil {
		return
	}
	fmt.Println("json str -> ", string(jsonStr))

	//Person{Tom 18}
	//changeAge, personAddr->0xc0000a4060, old->18, new->20
	//Person{Tom 18}
	//changeAgeByReference, personAddr->0xc0000a4030, old->18, new->20
	//Person{Tom 20}
	//json str ->  {"name":"Tom","age":20}
}

type Person struct {
	Name string `json:"name,omitempty"`
	Age  uint16 `json:"age,omitempty"`
}

func (person Person) sayHi() {
	fmt.Printf("Person%v\n", person)
}
func (person Person) changeAge(newAge uint16) {
	oldAge := person.Age
	person.Age = newAge
	fmt.Printf("changeAge, personAddr->%p, old->%d, new->%d\n", &person, oldAge, newAge)
}
func (person *Person) changeAgeByReference(newAge uint16) {
	oldAge := person.Age
	person.Age = newAge
	fmt.Printf("changeAgeByReference, personAddr->%p, old->%d, new->%d\n", person, oldAge, newAge)
}
