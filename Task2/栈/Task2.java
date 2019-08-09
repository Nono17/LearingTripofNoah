//用数组实现一个顺序栈
public class Stack{
    int[] data;
    private int top;
    public Stack(int size){
        this.size = size;
        data = new int[size];
        top = -1;
    }
    public int getSize(){
        return size;
    }
    public int getTop(){
        return top;
    }
    public boolean isEmpty(){
        return top == -1;
    }
    public boolean ifFull(){
        return (top+1) == size;
    }
    public boolean push(int data){
        if(isFull()){
            System.out.println("the stack is full!");
            return false;
        }else{
            top++;
            this.data[top] = data;
            return true;
        }
    }
    public int pop() throws Exception{
        if(isEmpty()){
            throw new Exception("the stack is empty!");
        }else{
            return this.data[top--];
        }
    } 
    public int peek(){
        return this.data[getTop()];
    }
    public static void main(String[] args){
        Stack stack = new Stack(20);
        stack.push(0);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Now the top_num is:" + stack.peek());

        while(! stack.isEmpty()){
            try{
                System.out.println(stack.pop());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
//用链表实现一个链式栈
public class Node {
    Object element;
    Node next;

    public Node(Object element){
        this(element,null);
    }
    public Node(Object element,Node n){
        this.element=element;
        next=n;
    }
    public Object getElement(){
        return element;
    }
}

public class ListStack{
    Node header;
    int elementCount;
    int size;
    public ListStack(){
        header=null;
        elementCount=0;
        size=0;
    }
    public ListStack(int size){
        header=null;
        elementCount=0;
        this.size=size;
    }
    public void setHeader(Node header){
        this.header=header;
    }
    public boolean isFull(){
        if(elementCount==size){
            return true;
        }
        return false;
    }
    public boolean isEmpty(){
        if(elementCount==0){
            return true;
        }
        return false;
    }
    public void push(Object value){
        if(this.isFull()){
            throw new RuntimeException("Stack is Full");
        }
        header=new Node(value, header);
        elementCount++;
    }
    public Object pop(){
        if(this.isEmpty()){
            throw new RuntimeException("Stack is empty");
        }
        Object object=header.getElement();
        header=header.next;
        elementCount--;
        return object;
    }
    public Object peak(){
        if(this.isEmpty()){
            throw new RuntimeException("Stack is empty");
        }
        return header.getElement();
    }
}
//编程模拟实现一个浏览器的前进、后退功能
// 基于数组实现的顺序栈
public class ArrayStack {
    private String[] items;  // 数组
    private int count;       // 栈中元素个数
    private int n;           // 栈的大小
  
    // 初始化数组，申请一个大小为 n 的数组空间
    public ArrayStack(int n) {
      this.items = new String[n];
      this.n = n;
      this.count = 0;
    }
  
    // 入栈操作
    public boolean push(String item) {
      // 数组空间不够了，直接返回 false，入栈失败。
      if (count == n) return false;
      // 将 item 放到下标为 count 的位置，并且 count 加一
      items[count] = item;
      ++count;
      return true;
    }
    
    // 出栈操作
    public String pop() {
      // 栈为空，则直接返回 null
      if (count == 0) return null;
      // 返回下标为 count-1 的数组元素，并且栈中元素个数 count 减一
      String tmp = items[count-1];
      --count;
      return tmp;
    }
  }
//package stack;

//基于链表实现的栈

public class StackBasedOnLinkedList {
	private Node top = null;
	
//	入栈
	public void push(int value) {
		Node newNode = new Node(value, null);
//		判断是否栈空
		if(top == null) {
			top = newNode;
		}
		else {
			newNode.next = top;
			top = newNode;
		}
	}
	
//	出栈
	public int pop() {
		if(top == null)
			return -1;//用-1表示栈中没有元素
		int value = top.data;
		top = top.next;
		return value;
	}
	
	public void printAll() {
		Node p = top;
		while(p != null) {
			System.out.print(p.data + ' ');
			p = p.next;
		}
		System.out.println();
	}
	
	private static class Node{
		private int data;
		private Node next;
		
		public Node(int data, Node next) {
			this.data = data;
			this.next = next;
		}
		
		public int getData() {
			return data;
		}
	}

}