//编程实现斐波那契数列求值 f(n)=f(n-1)+f(n-2)

    public static int getFib(int n){
        if(n < 0){
            return -1;
        }else if(n == 0){
            return 0;
        }else if(n == 1 || n ==2){
            return 1;
        }else{
            return getFib(n - 1) + getFib(n - 2);
        }
    }
//编程实现求阶乘 n! 
public class Factorial2 {  
    public static BigDecimal factorial(BigDecimal n){  
        BigDecimal bd1 = new BigDecimal(1);//1  
        if(n.equals(new BigDecimal(1))){  
            return bd1;  
        }  
        else  
            return n.multiply(factorial(n.subtract(bd1)));//n*f(n-1)  
    }  
    public static void main(String[] arguments){  
        Scanner sc = new Scanner(System.in);  
        BigDecimal a = sc.nextBigDecimal();  
        BigDecimal result = factorial(a);     
        System.out.println(a + "!=" +result);  
          
    }  
  
}  
//编程实现一组数据集合的全排列
public  void Permutation(char chs[],int start )
    {
        if(start==chs.length-1)
        {
            Arrays.toString(chs);
            //如果已经到了数组的最后一个元素，前面的元素已经排好，输出。
        }
        for(int i=start;i<=chs.length-1;i++)
        {
        //把第一个元素分别与后面的元素进行交换，递归的调用其子数组进行排序
                Swap(chs,i,start);
                Permutation(chs,start+1);
                Swap(chs,i,start);
        //子数组排序返回后要将第一个元素交换回来。  
        //如果不交换回来会出错，比如说第一次1、2交换，第一个位置为2，子数组排序返回后如果不将1、2
        //交换回来第二次交换的时候就会将2、3交换，因此必须将1、2交换使1还是在第一个位置 
        }
    }
    public  void Swap(char chs[],int i,int j)
    {
        char temp;
        temp=chs[i];
        chs[i]=chs[j];
        chs[j]=temp;
    }
