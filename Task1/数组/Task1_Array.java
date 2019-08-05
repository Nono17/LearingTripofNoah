// 数组动态扩容
// java版本
public class Solution{
    static int []array=new int [20];
    public static void main(String[] args){
        array=addLengthArray(array);
        for(int i = 0;i<array.length;i++){
            array[i]=i;
            System.out.println(array[i]);
        }
    }
public static int[] addLengthArray(int[] array){
     int[] newArray = new int [array.length*2];
     //将array数组从0位置至array.length位置,复制到newArray数组0位置到array.length位置
     System.arraycopy(array,0, newArray, 0, array.length);
     return newArray;
    }
}


//实现两个有序数组合并为一个有序数组
//java版本
class Solution{
    public void merge(int[] num1, int m, int[] num2, int n){
        int [] result = new int[m+n];
        int i = 0,j=0,k=0;
        while(i<m && j<n)
        {
            if(nums1[i] < nums2[j]){
                result[k++] = nums1[i++];
            }else{
                result[k++] = nums2[j++];
            }
        }
        if(i != m)
        { 
            while(i < m)
            {
                result[k++] = nums1[i++];
            }
        }
        if(j != n)
        {
            while(j < n)
            {
                result[k++] = nums2[j++];
            }
        }
        k = 0;
        for(i=0;i<nums1.length;i++)
            nums1[i] = result[k++];  
    }
}