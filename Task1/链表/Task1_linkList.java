// 实现单链表、循环链表、双向链表，支持增删操作
public class MyLink {
    Node head = null; // 头节点


    class Node {
        Node next = null;// 节点的引用，指向下一个节点
        int data;// 节点的对象，即内容

        public Node(int data) {
            this.data = data;
        }
    }


    public void addNode(int d) {
        Node newNode = new Node(d);// 实例化一个节点
        if (head == null) {
            head = newNode;
            return;
        }
        Node tmp = head;
        while (tmp.next != null) {
            tmp = tmp.next;
        }
        tmp.next = newNode;
    }


    public boolean deleteNode(int index) {
        if (index < 1 || index > length()) {
            return false;
        }
        if (index == 1) {
            head = head.next;
            return true;
        }
        int i = 1;
        Node preNode = head;
        Node curNode = preNode.next;
        while (curNode != null) {
            if (i == index) {
                preNode.next = curNode.next;
                return true;
            }
            preNode = curNode;
            curNode = curNode.next;
            i++;
        }
        return false;
    }


    public int length() {
        int length = 0;
        Node tmp = head;
        while (tmp != null) {
            length++;
            tmp = tmp.next;
        }
        return length;
    }


    public boolean deleteNode11(Node n) {
        if (n == null || n.next == null)
            return false;
        int tmp = n.data;
        n.data = n.next.data;
        n.next.data = tmp;
        n.next = n.next.next;
        System.out.println("删除成功！");
        return true;
    }

    public void printList() {
        Node tmp = head;
        while (tmp != null) {
            System.out.println(tmp.data);
            tmp = tmp.next;
        }
    }

    public static void main(String[] args) {
        MyLink list = new MyLink();
        list.addNode(5);
        list.addNode(3);
        list.addNode(1);
        list.addNode(2);
        list.addNode(55);
        list.addNode(36);
        System.out.println("linkLength:" + list.length());
        System.out.println("head.data:" + list.head.data);
        list.printList();
        list.deleteNode(4);
        System.out.println("After deleteNode(4):");
        list.printList();
    }
}


//实现单链表反转
package javatest1;
public class javatest1 {
	public static void main(String[] args) {
		Node head = new Node(0);
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);
		head.setNext(node1);
		node1.setNext(node2);
		node2.setNext(node3);
 
		// 打印反转前的链表
		Node h = head;
		while (null != h) {
			System.out.print(h.getData() + " ");
			h = h.getNext();
		}
		// 调用反转方法
		head = Reverse1(head);
 
		System.out.println("\n**************************");
		// 打印反转后的结果
		while (null != head) {
			System.out.print(head.getData() + " ");
			head = head.getNext();
		}
	}
 
	/**
	 * 递归，在反转当前节点之前先反转后续节点
	 */
	public static Node Reverse1(Node head) {
		// head看作是前一结点，head.getNext()是当前结点，reHead是反转后新链表的头结点
		if (head == null || head.getNext() == null) {
			return head;// 若为空链或者当前结点在尾结点，则直接还回
		}
		Node reHead = Reverse1(head.getNext());// 先反转后续节点head.getNext()
		head.getNext().setNext(head);// 将当前结点的指针域指向前一结点
		head.setNext(null);// 前一结点的指针域令为null;
		return reHead;// 反转后新链表的头结点
	}
}
 
	class Node {
		private int Data;// 数据域
		private Node Next;// 指针域
 
		public Node(int Data) {
			// super();
			this.Data = Data;
		}
 
		public int getData() {
			return Data;
		}
 
		public void setData(int Data) {
			this.Data = Data;
		}
 
		public Node getNext() {
			return Next;
		}
 
		public void setNext(Node Next) {
			this.Next = Next;
		}
	}
//实现两个有序的链表合并为一个有序链表
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
       ListNode head = null;
       if (l1 == null) {
           return l2;
       }
       if (l2 == null) {
           return l1;
       }
       if (l1.val <= l2.val) {//如果l1节点的值小于等于l2节点的值，由于这两个链表是有序的，所以合并后最小的节点(head节点)就是它们两者中的小者
           head = l1;
           l1 = l1.next;//后移，用于继续比较选出接下来最小的节点
       }else {
           head = l2;
           l2 = l2.next;
       }
        ListNode temp = head;
       //接着比较两个链表，对两个链表中的最小的节点进行比较选出最小的，也就是合并后的第二小的节点，循环知道有一个链表为空
       while (l1 != null && l2 != null) {
           if (l1.val <= l2.val) {
              temp.next = l1;
               l1 = l1.next;
           }else {
               temp.next = l2;
               l2 = l2.next;
           }
           temp = temp.next;
       }
       if (l1 == null) {
           temp.next = l2;
       }
       if (l2 == null) {
           temp.next = l1;
       }
        return head;
    }

}

//实现求链表的中间结点
public class l链表中间节点 {
	public static void main(String[] args){
		int[] array = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		ListNode head = ListNode.arrayToList(array);
		ListNode.printList(head);
		findMid(head);
	}
 
	private static void findMid(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;
		while(fast.next != null){
			if(fast.next.next != null){
				fast = fast.next.next;
				slow = slow.next;
			} else {
				slow = slow.next;
			}
			
		}
		System.out.println(slow.val);
	}
}