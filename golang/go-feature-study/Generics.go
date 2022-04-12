package main

type myStr string

type customType interface {
	float32 | float64 | int | int8 | int32 | int16 | int64 | uint | uint8 | uint32 | uint16 | uint64 | ~string
}

func min[T customType](a, b T) T {
	if a > b {
		return b
	}
	return a
}
