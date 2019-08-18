//实现一个小顶堆、大顶堆、优先级队列

//实现堆排序
public class HeapSort {
 
	public static void main(String[] args) {
		int[] arr = { 50, 10, 90, 30, 70, 40, 80, 60, 20 };
		System.out.println("排序之前：");
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
 
		// 堆排序
		heapSort(arr);
 
		System.out.println();
		System.out.println("排序之后：");
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
	}
 
	/**
	 * 堆排序
	 */
	private static void heapSort(int[] arr) { 
		// 将待排序的序列构建成一个大顶堆
		for (int i = arr.length / 2; i >= 0; i--){ 
			heapAdjust(arr, i, arr.length); 
		}
		
		// 逐步将每个最大值的根节点与末尾元素交换，并且再调整二叉树，使其成为大顶堆
		for (int i = arr.length - 1; i > 0; i--) { 
			swap(arr, 0, i); // 将堆顶记录和当前未经排序子序列的最后一个记录交换
			heapAdjust(arr, 0, i); // 交换之后，需要重新检查堆是否符合大顶堆，不符合则要调整
		}
	}
 
	/**
	 * 构建堆的过程
	 * @param arr 需要排序的数组
	 * @param i 需要构建堆的根节点的序号
	 * @param n 数组的长度
	 */
	private static void heapAdjust(int[] arr, int i, int n) {
		int child;
		int father; 
		for (father = arr[i]; leftChild(i) < n; i = child) {
			child = leftChild(i);
			
			// 如果左子树小于右子树，则需要比较右子树和父节点
			if (child != n - 1 && arr[child] < arr[child + 1]) {
				child++; // 序号增1，指向右子树
			}
			
			// 如果父节点小于孩子结点，则需要交换
			if (father < arr[child]) {
				arr[i] = arr[child];
			} else {
				break; // 大顶堆结构未被破坏，不需要调整
			}
		}
		arr[i] = father;
	}
 
	// 获取到左孩子结点
	private static int leftChild(int i) {
		return 2 * i + 1;
	}
	
	// 交换元素位置
	private static void swap(int[] arr, int index1, int index2) {
		int tmp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = tmp;
	}
}
//利用优先级队列合并K个有序数组
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) { val = x; }
     * }
     */
    class Solution {
        //思路是 优先队列(小顶堆) 遍历

        //遇到的坑!!!K个节点，每个节点都有可能是空！！！也就是说虽然lists数有长度，但是可能很多个元素都是null
        public ListNode mergeKLists(ListNode[] lists) {
            PriorityQueue<ListNode> queue = new PriorityQueue<>((a,b)->a.val-b.val);
            for(int i=0;i<lists.length;i++){
                if(lists[i]!=null){
                    queue.add(lists[i]);
                }
            }
            if(queue.isEmpty()){
                return null;
            }
            ListNode temp = new ListNode(queue.peek().val);
            ListNode head = temp;
            while(!queue.isEmpty()){
                ListNode p = queue.poll();
                temp.next = p;
                temp = temp.next;
                if(p.next!=null){
                    queue.offer(p.next);
                }
            }
            return head.next;
        }
    }
//求一组动态数据集合最大TOP K
//固定容量的优先队列，模拟大顶堆，用于解决求topN小的问题
public class FixSizedPriorityQueue<E extends Comparable> {
	private PriorityQueue<E> queue;
	private int maxSize; // 堆的最大容量

	public FixSizedPriorityQueue(int maxSize) {
		if (maxSize <= 0)
			throw new IllegalArgumentException();
		this.maxSize = maxSize;
		this.queue = new PriorityQueue(maxSize, new Comparator<E>() {
			public int compare(E o1, E o2) {
				// 生成最大堆使用o2-o1,生成最小堆使用o1-o2, 并修改 e.compareTo(peek) 比较规则
				return (o2.compareTo(o1));
			}
		});
	}

	public void add(E e) {
		if (queue.size() < maxSize) { // 未达到最大容量，直接添加
			queue.add(e);
		} else { // 队列已满
			E peek = queue.peek();
			if (e.compareTo(peek) < 0) { // 将新元素与当前堆顶元素比较，保留较小的元素
				queue.poll();
				queue.add(e);
			}
		}
	}

	public List<E> sortedList() {
		List<E> list = new ArrayList<E>(queue);
		Collections.sort(list); // PriorityQueue本身的遍历是无序的，最终需要对队列中的元素进行排序
		return list;
	}

	public static void main(String[] args) {
		final FixSizedPriorityQueue pq = new FixSizedPriorityQueue(10);
		Random random = new Random();
		int rNum = 0;
		System.out.println("100 个 0~999 之间的随机数：-----------------------------------");
		for (int i = 1; i <= 100; i++) {
			rNum = random.nextInt(1000);
			System.out.println(rNum);
			pq.add(rNum);
		}
		System.out.println("PriorityQueue 本身的遍历是无序的：-----------------------------------");
		Iterable<Integer> iter = new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return pq.queue.iterator();
			}
		};
		for (Integer item : iter) {
			System.out.print(item + ", ");
		}
		System.out.println();
		System.out.println("PriorityQueue 排序后的遍历：-----------------------------------");
		/*
		 * for (Integer item : pq.sortedList()) { System.out.println(item); }
		 */
		// 或者直接用内置的 poll() 方法，每次取队首元素（堆顶的最大值）
		while (!pq.queue.isEmpty()) {
			System.out.print(pq.queue.poll() + ", ");
		}
	}
}
