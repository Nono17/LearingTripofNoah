//实现一个二叉查找树，并且支持插入、删除、查找操作
static class BinaryNode<T>
        {
            T data;
            BinaryNode<T> left;
            BinaryNode<T> right;
            public BinaryNode(T data){
                this(data,null,null);
            }
            public BinaryNode(T data,BinaryNode<T> left,BinaryNode right){
                    this.data = data;
                    this.left = left;
                    this.right = right;
            }
            public BinaryNode()
            {
                data = null;
                this.left = left;
                this.right = right;
            }
        }
/**插入元素 */
    public void insert(T t)
    {
        rootTree = insert(t, rootTree);
    }
    public BinaryNode<T> insert(T t,BinaryNode<T> node)
    {
        if(node==null)
        {
            return new BinaryNode<T>(t, null, null);
        }
        int result = t.compareTo(node.data);
        if(result<0)
            node.left = insert(t, node.left);
        else if(result>0)
            node.right = insert(t,node.right);
        else
            ;
        return node;
    }
/**查找指定的元素，默认为从根节点开始查询 */
    public boolean contains(T t)
    {
       return contains(t, rootTree);
        
    }
  /**从某个结点出开始查找元素*/
    public boolean contains(T t, BinaryNode<T> node)
    {
       if(node==null)
         return false;//结点为空，查找失败
       int result = t.compareTo(node.data);
        if(result>0)
           return contains(t,node.right);//递归查询右子树
       else if(result<0)
           return contains(t, node.left);//递归查询左子树  
       else
           return true;
    }
     /**
        这里我提供一个对二叉树最大值
        最小值的搜索*/
  
  
  /**找到二叉查找树中的最小值*/
    public T findMin()
    {
       if(isEmpty())
       {
           System.out.println("二叉树为空");
           return null;
       }else
        return findMin(rootTree).data;
        
    }
    /**找到二叉查找树中的最大值*/
    public T findMax()
    {
        if(isEmpty())
           {
               System.out.println("二叉树为空");
               return null;
           }else
            return findMax(rootTree).data;
    }
  
 /**查询出最小元素所在的结点*/
    public BinaryNode<T> findMin(BinaryNode<T> node)
    {
        if(node==null)
            return null;
        else if(node.left==null)
            return node;
        return findMin(node.left);//递归查找
    }
    /**查询出最大元素所在的结点*/
    public BinaryNode<T> findMax(BinaryNode<T> node)
    {
        if(node!=null)
        {
            while(node.right!=null)
                node=node.right;
        }
        return node;       
    }
/**删除元素*/
    public void remove(T t){
        rootTree = remove(t,rootTree);
    } /**在某个位置开始判断删除某个结点*/
    public BinaryNode<T> remove(T t,BinaryNode<T> node){
       if(node == null)
           return node;//没有找到,doNothing
       int result = t.compareTo(node.data);
       if(result>0)
           node.right = remove(t,node.right);
       else if(result<0)
           node.left = remove(t,node.left);
       else if(node.left!=null&&node.right!=null)
       {
           node.data = findMin(node.right).data;
           node.right = remove(node.data,node.right);
       }
       else
           node = (node.left!=null)?node.left:node.right;
       return node;
           
   }
//实现查找二叉查找树中某个节点的后继、前驱节点
//求前驱节点
TreeNode* getRightNode(TreeNode* root)
{
  if(root ==NULL)  return NULL;
  while(root->right !=NULL)
      root = root->right;
  return root;
}


TreeNode* getPNode(TreeNode* root,int value,TreeNode*& parent,TreeNode*& firstRParent)
{
    while(root)
    {
        if(root->val == value)
            return root;

        parent = root;
        if(root->val>value)
        {
            root = root->left;
        }else{
            firstRParent = root;//出现右拐点
            root = root->right;
        }
    }

    return NULL;
}
//主函数
TreeNode*  getPreNode(TreeNode* root, int value)
{
     if(root)
     {
           TreeNode* parent =NULL;
           TreeNode* firstRParent =NULL;

          TreeNode* node = getPNode(root,value,parent ,firstRParent );
          if(node == NULL)
                  return node;
          if(node->left) //有左子树
             return getRightNode(node->right);

          if(NULL == parent ||(parent && (NULL == firstRParent))) return NULL; //没有前驱节点的情况

          if(node == parent->right) //没有左子树 是其父节点的右边节点 
              return parent;
          else//没有左子树 是其父节点的左边节点 
           {
               return firstRParent ;
           }

     }
    return root;
}
//求后继节点
TreeNode* getLeftNode(TreeNode* root)
{
  if(root ==NULL)  return NULL;
  while(root->left !=NULL)
      root = root->left;
  return root;
}

TreeNode* getNode(TreeNode* root,int value,TreeNode*& parent,TreeNode*& firstlParent)
{
    while(root)
    {
        if(root->val == value)
            return root;

        parent = root;//设置父亲节点
        if(root->val<value)
        {
            root = root->right;
        }else{
            firstlParent = root;//发生了左拐
            root = root->left;
        }
    }
    return NULL;
}

//主函数
TreeNode*  getNextNode(TreeNode* root, int value)
{
     if(root)
     {
           TreeNode* parent =NULL;
           TreeNode* firstlParent =NULL;

          TreeNode* node = getNode(root,value,parent ,firstlParent );
          if(node == NULL)
                  return node;
          if(node->right)//有右子树
             return getLeftNode(node->right);
          if(NULL == parent ||(parent && (NULL == firstlParent))) return NULL; //没有后继节点的情况
          if(node == parent->left)//没有右子树 是其父节点的左边节点 
              return parent;
          else//没有右子树 是其父节点的右边节点
           {
               return firstlParent ;
           }

     }
    return root;
}
// 实现二叉树前、中、后序以及按层遍历
/**前序遍历 */
public class Test 
{
	public static void main(String[] args)
	{
		TreeNode[] node = new TreeNode[10];//以数组形式生成一棵完全二叉树
		for(int i = 0; i < 10; i++)
		{
			node[i] = new TreeNode(i);
		}
		for(int i = 0; i < 10; i++)
		{
			if(i*2+1 < 10)
				node[i].left = node[i*2+1];
			if(i*2+2 < 10)
				node[i].right = node[i*2+2];
		}
		
		preOrderRe(node[0]);
	}
	
	public static void preOrderRe(TreeNode biTree)
	{//递归实现
		System.out.println(biTree.value);
		TreeNode leftTree = biTree.left;
		if(leftTree != null)
		{
			preOrderRe(leftTree);
		}
		TreeNode rightTree = biTree.right;
		if(rightTree != null)
		{
			preOrderRe(rightTree);
		}
	}
	
	public static void preOrder(TreeNode biTree)
	{//非递归实现
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while(biTree != null || !stack.isEmpty())
		{
			while(biTree != null)
			{
				System.out.println(biTree.value);
				stack.push(biTree);
				biTree = biTree.left;
			}
			if(!stack.isEmpty())
			{
				biTree = stack.pop();
				biTree = biTree.right;
			}
		}
	}
}
 
class TreeNode//节点结构
{
	int value;
	TreeNode left;
	TreeNode right;
	
	TreeNode(int value)
	{
		this.value = value;
	}
}
/**中序遍历 */
public class Test 
{
	public static void main(String[] args)
	{
		TreeNode[] node = new TreeNode[10];//以数组形式生成一棵完全二叉树
		for(int i = 0; i < 10; i++)
		{
			node[i] = new TreeNode(i);
		}
		for(int i = 0; i < 10; i++)
		{
			if(i*2+1 < 10)
				node[i].left = node[i*2+1];
			if(i*2+2 < 10)
				node[i].right = node[i*2+2];
		}
		
		midOrderRe(node[0]);
		System.out.println();
		midOrder(node[0]);
	}
	
	public static void midOrderRe(TreeNode biTree)
	{//中序遍历递归实现
		if(biTree == null)
			return;
		else
		{
			midOrderRe(biTree.left);
			System.out.println(biTree.value);
			midOrderRe(biTree.right);
		}
	}
	
	
	public static void midOrder(TreeNode biTree)
	{//中序遍历费递归实现
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while(biTree != null || !stack.isEmpty())
		{
			while(biTree != null)
			{
				stack.push(biTree);
				biTree = biTree.left;
			}
			if(!stack.isEmpty())
			{
				biTree = stack.pop();
				System.out.println(biTree.value);
				biTree = biTree.right;
			}
		}
	}
}
 
class TreeNode//节点结构
{
	int value;
	TreeNode left;
	TreeNode right;
	
	TreeNode(int value)
	{
		this.value = value;
	}
}

/**后序遍历 */
public class Test 
{
	public static void main(String[] args)
	{
		TreeNode[] node = new TreeNode[10];//以数组形式生成一棵完全二叉树
		for(int i = 0; i < 10; i++)
		{
			node[i] = new TreeNode(i);
		}
		for(int i = 0; i < 10; i++)
		{
			if(i*2+1 < 10)
				node[i].left = node[i*2+1];
			if(i*2+2 < 10)
				node[i].right = node[i*2+2];
		}
		
		postOrderRe(node[0]);
		System.out.println("***");
		postOrder(node[0]);
	}
	
	
	
	public static void postOrderRe(TreeNode biTree)
	{//后序遍历递归实现
		if(biTree == null)
			return;
		else
		{
			postOrderRe(biTree.left);
			postOrderRe(biTree.right);
			System.out.println(biTree.value);
		}
	}
	
	public static void postOrder(TreeNode biTree)
	{//后序遍历非递归实现
		int left = 1;//在辅助栈里表示左节点
		int right = 2;//在辅助栈里表示右节点
		Stack<TreeNode> stack = new Stack<TreeNode>();
		Stack<Integer> stack2 = new Stack<Integer>();//辅助栈，用来判断子节点返回父节点时处于左节点还是右节点。
		
		while(biTree != null || !stack.empty())
		{
			while(biTree != null)
			{//将节点压入栈1，并在栈2将节点标记为左节点
				stack.push(biTree);
				stack2.push(left);
				biTree = biTree.left;
			}
			
			while(!stack.empty() && stack2.peek() == right)
			{//如果是从右子节点返回父节点，则任务完成，将两个栈的栈顶弹出
				stack2.pop();
				System.out.println(stack.pop().value);
			}
			
			if(!stack.empty() && stack2.peek() == left)
			{//如果是从左子节点返回父节点，则将标记改为右子节点
				stack2.pop();
				stack2.push(right);
				biTree = stack.peek().right;
			}
				
		}
	}
}
 
class TreeNode//节点结构
{
	int value;
	TreeNode left;
	TreeNode right;
	
	TreeNode(int value)
	{
		this.value = value;
	}
}
/**层次遍历 */
public static void levelOrder(TreeNode biTree)
{//层次遍历
    if(biTree == null)
        return;
    LinkedList<TreeNode> list = new LinkedList<TreeNode>();
    list.add(biTree);
    TreeNode currentNode;
    while(!list.isEmpty())
    {
        currentNode = list.poll();
        System.out.println(currentNode.value);
        if(currentNode.left != null)
            list.add(currentNode.left);
        if(currentNode.right != null)
            list.add(currentNode.right);
    }
}
 