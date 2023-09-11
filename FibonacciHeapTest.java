
class FibonacciHeapTest {

    public static void main(String[] args) {
        FibonacciHeap obj = new FibonacciHeap();
        obj.insert(11);
        System.out.println("find min " + obj.findMin());
        obj.insert(17);
        System.out.println("find min " + obj.findMin());
        obj.insert(18);
        System.out.println("find min " + obj.findMin());
        obj.insert(20);
        System.out.println("find min " + obj.findMin());
        obj.insert(19);
        System.out.println("find min " + obj.findMin());
        obj.insert(22);
        System.out.println("find min " + obj.findMin());
        obj.insert(25);
        System.out.println("find min " + obj.findMin());
        obj.insert(21);
        System.out.println("find min " + obj.findMin());
        obj.insert(44);
        System.out.println("find min " + obj.findMin());
        obj.display();
        System.out.println("after all insert, n = " + obj.getN());
        obj.decreaseKey(21, 1);
        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        obj.display();
        System.out.println("find 20: " + obj.findIter(20).getKey());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());

        System.out.println("extract min: " + obj.extractMin());
        System.out.println("get n " + obj.getN());
    }
}