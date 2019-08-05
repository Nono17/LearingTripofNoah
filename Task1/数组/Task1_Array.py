# python版本
#实现数组的动态扩容
class Array:
    def __init_(self, capacity = 10):
        """
        构造函数
        """
        self._capacity = capacity #数组最大容量
        self._size = 0            #数组已使用的大小
        self._data = [None]*self._capacity #初始化元素
    def add(self, index, elem):
        """
        向数组中添加新元素
        """
        #1.index有效性判断
        if(index<0 or index>self._size):
            print("index不合法！")
            return;
        #2.判断容量capacity是否足够
        #已满：扩容
        if(self._size == self._capacity):
            self._resize(self._capacity*2)
        #3.在指定位置添加元素
        #先将index之后的元素全部依次后移一位
        for i in range(self._size-1, index-1, -1):
            self._data[i+1] = self._data[i]
        self._data[index] = elem #将元素添加到index
        #4.更新size
        self._size += 1
    def _resize(self, new_capacity):
        """
        扩容方法
        """ 
        #1.新建一个大容量数组
        newarr = Array(new_capacity)
        #2.将现在self._data的元素逐个拷贝到新数组
        for i in range(self._size):
            newarr.add(i, self._data[i])
        #3.更新变量
        self._capacity = new_capacity
        self._data = newarr._data
    def print(self):
        """
        打印数组
        """
        for i in range(self._size):
            print(self._data[i],end = '\t')

# 两个有序数组合并成一个有序数组
def merge(nums1, m, nums2, n):
    while m>0 and n>0:
        if nums1[m-1]>nums2[n-1]:#若nums1中最后一个元素大于nums2[]中最后一个元素
            nums1[m+n-1]=nums1[m-1]#则扩展后的列表最后一个元素是俩元素中最大的
            m-=1       #nums1中元素-1
        else:
            nums1[m+n-1]=nums2[n-1]
            n-=1
    if n>0:#若nums1完了，nums2还没完
        nums1[:n]=nums2[:n]#把剩下nums2加在最开始
 
if __name__=='__main__':
    nums1 = [0]#0的位置用来方nums2
    m = 0
    nums2 = [2, 5, 6]
    n = 3
    merge(nums1,m,nums2,n)
    print(nums1)
