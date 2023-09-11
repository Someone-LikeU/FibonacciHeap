# FibonacciHeap
斐波那契堆Java实现，后续可能添加python和C++实现，
参考链接
https://www.programiz.com/dsa/fibonacci-heap#google_vignette
https://www.youtube.com/watch?v=6JxvKfSV9Ns

原链接中有较严重bug，修复以下问题：
1. 记录堆中节点数量的属性n更新过程中变化情况不对，例如堆为空时n可能不为0.
2. 在数据量较大时，Find方法递归调用时可能会发生栈溢出，修改为迭代方式进行查找
3. cut方法没有正确处理父子节点指针，运行过程中可能会乱指向。
4. consolidate方法在运行时某些场景下可能会把某个节点的父子指针、左右指针都指向自己，进而丢失堆中其他节点，且会陷入死循环
