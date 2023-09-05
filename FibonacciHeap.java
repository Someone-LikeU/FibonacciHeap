
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


public class FibonacciHeap {
    FibNode min;
    int n;
    boolean trace;
    FibNode found;

    public boolean getTrace(){
        return this.trace;
    }

    public void setTrace(boolean trace){
        this.trace = trace;
    }

    public FibonacciHeap(){
        this.min = null;
        this.n = 0;
        this.trace = false;
    }

    private void insert(FibNode x){
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
    public void insert(int key){
        FibNode x = new FibNode(key);
        this.insert(x);
    }

    private void display(){
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

    // 获得最小值
    public int findMin(){
        return this.min.getKey();
    }

    private void displayNode(FibNode x){
        System.out.println("right:" + ((x.getRight() == null) ? "null" : x.getRight().getKey()));
        System.out.println("left:" + ((x.getLeft() == null) ? "null" : x.getLeft().getKey()));
        System.out.println("child:" + ((x.getChild() == null) ? "null" : x.getChild().getKey()));;
        System.out.println("parent:" + ((x.getParent() == null) ? "null" : x.getParent().getKey()));
        System.out.println("degree:" + x.getDegree());
    }

    // 从堆中pop出最小值
    public int extractMin(){
        FibNode z = this.min;
        if(z != null){
            FibNode c = z.getChild();
            FibNode k = c, temp;
            if(c != null){
                do{
                    temp = c.getRight();
                    insert(c);
                    c.setParent(null);
                    c = temp;
                }while(c != null && c != k);
            }
            z.getLeft().setRight(z.getRight());
            z.getRight().setLeft(z.getLeft());
            z.setChild(null);

            if(z == z.getRight()){
                this.min = null;
            }
            else{
                this.min = z.getRight();
                // 调整堆结构
                this.consolidate();
            }
            this.n--;
            return z.getKey();
        }
        else{
            return Integer.MAX_VALUE;
        }
    }


    // 调整堆结构
    public void consolidate(){
        double phi = (1 + Math.sqrt(5)) / 2;
        int Dofn = (int) (Math.log(this.n) / Math.log(phi));
        // 记录二项树度数的数组
        FibNode[] A = new FibNode[Dofn + 1];
        for(int i = 0; i <= Dofn; i++){
            A[i] = null;
        }
        FibNode w = this.min;
        if(w != null){
            FibNode check = this.min;
            do{
                FibNode x = w;
                int d = x.getDegree();
                while(A[d] != null){
                    FibNode y = A[d];
                    if(x.getKey() > y.getKey()){
                        FibNode temp = x;
                        x = y;
                        y = temp;
                        w = x;
                    }
                    FibHeapLink(y, x);
                    check = x;
                    A[d] = null;
                    d++;
                }
                A[d] = x;
                w = w.getRight();
            }while(w != null && w != check);

            this.min = null;
            for(int i = 0; i <= Dofn; ++i){
                if(A[i] != null){
                    insert(A[i]);
                }
            }
        }
    }

    // 将y节点链接到x节点上
    private void FibHeapLink(FibNode y, FibNode x){
        // 把y的独立出来
        y.getLeft().setRight(y.getRight());
        y.getRight().setLeft(y.getLeft());

        // 把y链接到x的孩子链表上
        FibNode p = x.getChild();
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

    // 在节点中查询key，递归
    private void find(int key, FibNode c){
        if(this.found != null || c == null){
            return;
        }
        else{
            FibNode temp = c;
            do{
                if(key == temp.getKey()){
                    this.found = temp;
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
    public FibNode find(int k){
        this.found = null;
        find(k, this.min);
        return this.found;
    }

    public void decreaseKey(int key, int newVal){
        FibNode x = find(key);
        decreaseKey(x, newVal);
    }

    private void decreaseKey(FibNode x, int k){
        // 新值比原值大，忽略这个操作
        if (k > x.getKey()){
            return;
        }
        x.setKey(k);
        FibNode y = x.getParent();
        if(y != null && x.getKey() < y.getKey()){
            cut(x, y);
            cascadingCut(y);
        }
        if(x.getKey() < this.min.getKey()){
            this.min = x;
        }
    }

    // 树的剪枝操作,cut
    private void cut(FibNode x, FibNode y){
        x.getRight().setLeft(x.getLeft());
        x.getLeft().setRight(x.getRight());
        y.setDegree(y.getDegree() - 1);
        x.setRight(null);
        x.setLeft(null);
        insert(x);
        x.setParent(null);
        x.setMark(false);
    }

    // 递归剪枝操作,cascading cut
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
    public void delete(FibNode x){
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }


    public static void main(String[] args) {
        FibonacciHeap obj = new FibonacciHeap();
        obj.insert(7);
        obj.insert(26);
        obj.insert(30);
        obj.insert(39);
        obj.insert(10);
        obj.insert(1);
        obj.display();

        System.out.println(obj.findMin());
        System.out.println(obj.extractMin());
        obj.display();
        System.out.println(obj.extractMin());
        obj.display();
        System.out.println(obj.extractMin());
        obj.display();
        System.out.println(obj.extractMin());
        obj.display();
        System.out.println(obj.extractMin());
        obj.display();
        
    }
}

