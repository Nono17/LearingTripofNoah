//用数组实现一个顺序队列
public class ArrayQueue <T>
{
    private T[] queue;//队列数组
    private int head=0;//头下标
    private int tail=0;//尾下标
    private int count=0;//元素个数
    public ArrayQueue()
    {
        queue=(T[])new Object[10];
        this.head=0;//头下标为零
        this.tail=0;
        this.count=0;
    }
    public ArrayQueue(int size)
    {
        queue=(T[])new Object[size];
        this.head=0;
        this.tail=0;
        this.count=0;
    }
    //入队
    public boolean inQueue(T t)
    {
        if(count==queue.length)
            return false;
        queue[tail++%(queue.length)]=t;//如果不为空就放入下一个
        count++;
        return true;
    }
    //出队
    public T outQueue()
    {
        if(count==0)//如果是空的那就不能再出栈了
            return null;
        count--;
        return queue[head++%(queue.length)];
    }
    //查队头
    public T showHead()
    {
        if(count==0) return null;
        return queue[head];
    }
    //判满
    public boolean isFull()
    {
        return count==queue.length;
    }
    //判空
    public boolean isEmpty()
    {
        return count==0;
    }
    //
} 
//用链表实现一个链式队列

public class Element<T> {
 
    private Element<T> next;
 
    private T value;
 
    public Element<T> getNext() {
        return next;
    }
 
    public void setNext(Element<T> next) {
        this.next = next;
    }
 
    public T getValue() {
        return value;
    }
 
    public void setValue(T value) {
        this.value = value;
    }
}
 

public interface IList<T> {
 
    boolean pushBack(T newElement);
 
    boolean popFront();
 
    Element<T> front();
 
    int size();
}
 

public class ListImpl<T> implements IList<T> {
 
    private Element<T> first = null;
 
    private Element<T> last = null;
 
    private int size = 0;
 
    public boolean pushBack(T newElement) {
        Element<T> element = new Element<>();
        element.setValue(newElement);
        if (size == 0) {
            first = element;
            size++;
            return true;
        }
        if (last == null) {
            last = new Element<>();
            last.setValue(newElement);
            first.setNext(last);
        } else {
            last.setNext(element);
            last = element;
        }
        size++;
        return true;
    }
 
    public boolean popFront() {
        if (size == 0) {
            return false;
        }
        first = first.getNext();
        size--;
        return true;
    }
 
    public Element<T> front() {
        return first;
    }
 
    public int size() {
        return size;
    }
}
 

public interface IQueue<R> {
 
    boolean push(R newElement);
 
    boolean pop();
 
    R front();
 
    int size();
 
    boolean display();
}
 

public class QueueImpl<R> implements IQueue<R> {
 
    private ListImpl<R> list;
 
    public boolean push(R newElement) {
        if (list == null) {
            list = new ListImpl<>();
        }
        list.pushBack(newElement);
        return true;
    }
 
 
    public boolean pop() {
        if (list == null) {
            list = new ListImpl<>();
        }
        list.popFront();
        return true;
    }
 

    public R front() {
        return list.front().getValue();
    }
 
 
    public int size() {
        return list.size();
    }
 

    public boolean display() {
        Element<R> front = list.front();
        System.out.println("列表元素：");
        Element<R> temp = front;
        while (temp != null) {
            System.out.print(temp.getValue());
            temp = temp.getNext();
            System.out.println("\t");
        }
        return true;
    }
}
 

public class Test {
 
    public static void main(String[] args) {
        IQueue<String> queue = new QueueImpl<>();
        queue.push("one");
        queue.push("two");
        queue.push("three");
        queue.pop();
        queue.display();
    }
}
//实现一个循环队列
 
public class CircleSqQueue implements IQueue {
 
	private Object[] queueElem;//队列存储空间
	private int front;//队首引用，若队列不为空，指向队首元素
	private int rear;//队尾引用，若队列不为空，指向队尾的下一个元素
	
	public CircleSqQueue(int maxsize) {
		front=rear=0;
		queueElem=new Object[maxsize];//分配maxsize个单元
	}
 

	public void clear() {
		front=rear=0;
	}
 

	public boolean isEmpty() {
		return rear==front;
	}
 

	public int length() {
		return (rear-front+queueElem.length)%queueElem.length;
	}
 

	public Object peek() {
		if(front==rear){
			return null;
		}
		else{
			return queueElem[front];
		}
	}
 
	public void offer(Object x) throws Exception {
		if((rear+1)%queueElem.length==front){//队满
			throw new Exception("队列已满");
		}
		else{
			queueElem[rear]=x;
			rear=(rear+1)%queueElem.length;//修改队尾指针
		}
	}
 
	public Object poll() {
		if(front==rear){
			return null;//队列为空
		}
		else{
			Object t=queueElem[front];
			front=(front+1)%queueElem.length;
			return t;
		}
	}
 
	public void display() {
		if(!isEmpty()){
			for(int i=front;i!=rear;i=(i+1)%queueElem.length){
				System.out.println(queueElem[i].toString()+" ");
			}
		}
		else{
			System.out.println("此队列为空");
		}
	}
 
}