import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;


/**
 * 斐波那契堆，节点类
 * @author peterjzhou
 * @contact peterjzhou@qq.com
 */
class FibNode{
    private FibNode parent;
    private FibNode child;
    private FibNode left;
    private FibNode right;
    private int degree;
    private boolean mark;
    private int key;

    public FibNode(){
        this.parent = null;
        this.child = null;
        this.left = this;
        this.right = this;
        this.degree = 0;
        this.mark = false;
        this.key = -1;
    }

    public FibNode(int key){
        this();
        this.key = key;
    }

    // Setters and Getters
    void setParent(FibNode parent){
        this.parent = parent;
    }

    FibNode getParent(){
        return this.parent;
    }

    void setChild(FibNode child){
        this.child = child;
    }

    FibNode getChild(){
        return this.child;
    }

    void setLeft(FibNode left){
        this.left = left;
    }

    FibNode getLeft(){
        return this.left;
    }

    FibNode getRight(){
        return this.right;
    }

    void setRight(FibNode right){
        this.right = right;
    }

    int getDegree(){
        return this.degree;
    }

    void setDegree(int degree){
        this.degree = degree;
    }

    boolean getMark(){
        return this.mark;
    }

    void setMark(boolean mark){
        this.mark = mark;
    }

    int getKey(){
        return this.key;
    }

    void setKey(int key){
        this.key = key;
    }
}

/**
 * 斐波那契堆类
 * @author peterjzhou
 * @contact peterjzhou@qq.com
 */
public class FibonacciHeap {
    /**
     * 指向堆中最小值节点，即整个堆的根节点
     */
    FibNode min;
    /**
     * 记录堆中元素数量
     */
    int n;
    /**
     * 跟踪模式，用于调试，可视情况使用
     */
    boolean trace;
    /**
     * 保存查找的结果节点
     */
    FibNode found;

    /**
     * 调整堆结构时的常量
     *
     */
    private static final double PHI = (1 + Math.sqrt(5)) / 2;

    // getters and setters
    public boolean getTrace(){
        return this.trace;
    }

    public void setTrace(boolean trace){
        this.trace = trace;
    }

    public int getN() {
        return n;
    }

    /**
     * 构造方法
     */
    public FibonacciHeap(){
        this.min = null;
        this.n = 0;
        this.trace = false;
    }

    /**
     * 插入一个对节点，私有
     * @param x FibNode
     */
    private void insert(FibNode x){
        // this.min为空，堆空
        if(this.min == null) {
            this.min = x;
            x.setLeft(this.min);
            x.setRight(this.min);
        }
        else{
            x.setRight(this.min);
            x.setLeft(this.min.getLeft());
            this.min.getLeft().setRight(x);
            this.min.setLeft(x);
            if(x.getKey() < this.min.getKey()){
                this.min = x;
            }
        }
        this.n++;
    }

    // 插入一个值为key的节点

    /**
     * 插入值为key的节点，入口方法，公有
     * @param key   插入值key
     */
    public void insert(int key){
        FibNode x = new FibNode(key);
        this.insert(x);
        // 每次插入可能会破坏堆结构，要调用一次consolidate方法
        // insert会递归调用自己，所以不能放到insert方法里
        this.consolidate();
    }

    /**
     * 结构化展示
     */
    public void display(){
        display(this.min);
        System.out.println();
    }

    // 递归打印fib heap
    private void display(FibNode x){
        System.out.print("(");
        if(x == null){
            System.out.print(")");
            return;
        }
        else{
            FibNode temp = x;
            do{
                System.out.println(temp.getKey());
                FibNode k = temp.getChild();
                display(k);
                System.out.println("->");
                temp = temp.getRight();
            }while(temp != x);
            System.out.println(")");
        }
    }

    /**
     * 合并两个斐波那契堆，把1和2合并为3
     * @param H1    堆1
     * @param H2    堆2
     * @param H3    堆3
     */
    // 合并两个fib heap
    public static void mergeHeap(FibonacciHeap H1,
                                 FibonacciHeap H2,
                                 FibonacciHeap H3){
        H3.min = H1.min;

        if(H1.min != null && H2.min != null){
            FibNode t1 = H1.min.getLeft();
            FibNode t2 = H2.min.getLeft();
            H1.min.setLeft(t2);
            t1.setRight(H2.min);

            H2.min.setLeft(t1);
            t2.setRight(H1.min);
        }

        if(H1.min == null || (H2.min != null && H2.min.getKey() < H1.min.getKey())){
            H3.min = H2.min;
        }
        H3.n = H1.n + H2.n;
    }


    /**
     * 查找最小值，即查看堆顶元素
     * @return  堆顶元素
     */
    public int findMin(){
        return this.min.getKey();
    }

    /**
     * 打印一个节点的信息
     * @param x 节点x
     */
    private void displayNode(FibNode x){
        System.out.println("right:" + ((x.getRight() == null) ? "null" : x.getRight().getKey()));
        System.out.println("left:" + ((x.getLeft() == null) ? "null" : x.getLeft().getKey()));
        System.out.println("child:" + ((x.getChild() == null) ? "null" : x.getChild().getKey()));;
        System.out.println("parent:" + ((x.getParent() == null) ? "null" : x.getParent().getKey()));
        System.out.println("degree:" + x.getDegree());
    }

    // 从堆中pop出最小值

    /**
     * 提取出堆中最小值，即堆顶
     * @return
     */
    public int extractMin(){
        FibNode z = this.min;
        // min指针不为空，堆中有元素
        if(z != null){
            // 获取堆顶的子节点，把所有子节点重新插入到堆中
            FibNode c = z.getChild();
            FibNode k = c, temp;
            if(c != null){
                do{
                    temp = c.getRight();
                    insert(c);
                    // insert内部会把n+1，此处调节结构，不是真的加节点，所以n--
                    this.n--;
                    c.setParent(null);
                    c = temp;
                }while(c != null && c != k);
            }
            // 放出根节点
            z.getLeft().setRight(z.getRight());
            z.getRight().setLeft(z.getLeft());
            z.setChild(null);

            // 放出后z还等于z的right，说明根节点时唯一的节点
            if(z == z.getRight()){
                this.min = null;
            }
            else{
                this.min = z.getRight();
                // 调整堆结构
                this.consolidate();
            }
            this.n--;
            z.setLeft(null);
            z.setRight(null);
            return z.getKey();
        }
        // 堆为空，返回一个int类型最大值
        else{
            return Integer.MAX_VALUE;
        }
    }


    /**
     * 调整堆结构
     */
    public void consolidate(){
        int Dofn = (int) (Math.log(this.n) / Math.log(PHI));
//        int Dofn = this.n; // 或者this.n,只不过更浪费空间
        // 记录二项树度数的数组
        FibNode[] A = new FibNode[Dofn + 1];
        for(int i = 0; i <= Dofn; i++){
            A[i] = null;
        }
        FibNode w = this.min;
        if(w != null){
            // 标记遍历根节点是否结束
            FibNode check = this.min;
            do{
                FibNode x = w;
                int d = x.getDegree();
                while(A[d] != null && A[d] != x){
                    FibNode y = A[d];
                    // 保证y指向的节点值比x的大
                    if(x.getKey() > y.getKey()){
                        FibNode temp = x;
                        x = y;
                        y = temp;
                        w = x;
                    }
                    // 把y接到x下面
                    FibHeapLink(y, x);
                    check = x;
                    A[d] = null;
                    d++;
                }
                A[d] = x;
                w = w.getRight();
            }while(w != null && w != check);
            // 先置空根节点，重新插入堆中
            this.min = null;
            for(int i = 0; i <= Dofn; ++i){
                if(A[i] != null){
                    insert(A[i]);
                    this.n--;
                }
            }
        }
    }


    /**
     * 将y子树链接到x节点上
     * @param y 子树y
     * @param x 节点x
     */
    private void FibHeapLink(FibNode y, FibNode x){
        // 可能存在传进来的x和y相同的情况
        if(x == y){
            return  ;
        }
        // 把y的独立出来
        y.getLeft().setRight(y.getRight());
        y.getRight().setLeft(y.getLeft());

        // 把y链接到x的孩子链表上
        FibNode p = x.getChild();
        // x没有子节点，y作为x的第一个子节点
        if(p == null){
            y.setRight(y);
            y.setLeft(y);
        }
        else{
            y.setRight(p);
            y.setLeft(p.getLeft());
            p.getLeft().setRight(y);
            p.setLeft(y);
        }
        y.setParent(x);
        x.setChild(y);
        x.setDegree(x.getDegree() + 1);
        y.setMark(false);
    }


    /**
     * 递归查询key所在节点，私有
     * @param key   查找值
     * @param c     起点节点，通常是this.min，即整个堆的根节点
     */
    private void find(int key, FibNode c){
        if(this.found != null || c == null){
            return;
        }
        else{
            FibNode temp = c;
            do{
                if(key == temp.getKey()){
                    this.found = temp;
                    return;
                }
                else{
                    FibNode k = temp.getChild();
                    find(key, k);
                    temp = temp.getRight();
                }
            }while(temp != c && this.found == null);

        }
    }


    // 在堆中查询key，从最小值开始

    /**
     * 查找值为k的入口方法
     * @param k 待查找值k
     * @return  查找节点found
     */
    public FibNode find(int k){
        this.found = null;
        find(k, this.min);
        return this.found;
    }

    /**
     * 迭代方式查询
     * @param k 查询关键字
     * @return  found节点
     */
    public FibNode findIter(int k){
        this.found = null;
        findValue(k, this.min);
        return this.found;
    }

    /**
     * 将堆中某值k减小到newVal，即改变大小，入口方法
     * @param key       原值
     * @param newVal    新值
     */
    public void decreaseKey(int key, int newVal){
        FibNode x = find(key);
        decreaseKey(x, newVal);
    }

    /**
     * 改变大小，私有方法
     * @param x 节点
     * @param k 新值
     */
    private void decreaseKey(FibNode x, int k){
        // 新值比原值大，忽略这个操作
        if (k > x.getKey()){
            return;
        }
        x.setKey(k);
        FibNode y = x.getParent();
        if(y != null && x.getKey() < y.getKey()){
            // 设置新值后，子节点值可能比父节点大，违反堆的性质，进行剪枝
            cut(x, y);
            // 级联剪枝y
            cascadingCut(y);
        }
        if(x.getKey() < this.min.getKey()){
            this.min = x;
        }
    }

    // 树的剪枝操作,cut

    /**
     * 剪枝
     * @param x 子节点
     * @param y x的父节点
     */
    private void cut(FibNode x, FibNode y){
        y.setDegree(y.getDegree() - 1);
        // y度为0，则无子节点，设为null，否则应该设为x的右节点
        y.setChild(y.getDegree() == 0 ? null : x.getRight());
        // 独立出x
        x.getRight().setLeft(x.getLeft());
        x.getLeft().setRight(x.getRight());
        x.setRight(null);
        x.setLeft(null);
        // 重新插入x
        insert(x);
        this.n--;
        x.setParent(null);
        x.setMark(false);
    }

    // 递归剪枝操作,cascading cut

    /**
     * 级联剪枝y
     * @param y 某父节点
     */
    private void cascadingCut(FibNode y){
        FibNode z = y.getParent();
        if(z != null){
            if(!y.getMark()){
                y.setMark(true);
            }
            else{
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    // 删除节点

    /**
     * 删除节点，通过设置该节点为最小值，然后pop掉根节点实现
     * @param x 待删除节点
     */
    public void delete(FibNode x){
        decreaseKey(x, Integer.MIN_VALUE);
        // 提取最小值，不接收返回值
        extractMin();
    }

    /**
     * 迭代的查询值k是否在堆中，起点通常为根节点
     * @param k 查询值
     * @param c 起点节点
     */
    private void findValue(int k, FibNode c){
        if(c == null){
            return;
        }
        // 用一个简单的队列，遍历时类似层序遍历，优先一个根节点的子节点
        Queue<FibNode> que = new ArrayDeque<>();
        que.add(c);
        Set<FibNode> seen = new HashSet<>();
        FibNode current = null;
        while(!que.isEmpty() && seen.size() <= this.n){
            current = que.poll();
            if(current == null || seen.contains(current)){
                continue;
            }
            seen.add(current);
            if(current.getKey() == k){
                // 找到值，通过found属性返回
                this.found = current;
                break;
            }
            FibNode child = current.getChild();
            // 遍历子节点
            if(child != null){
                que.add(child);
                FibNode nextSib = child.getRight();
                while(nextSib != child){
                    que.add(nextSib);
                    nextSib = nextSib.getRight();
                }
            }
            FibNode right = current.getRight();
            if(!seen.contains(right)){
                que.add(right);
            }
        }
    }
}

