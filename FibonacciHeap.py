# @author: peter

class FibNode:
    def __init__(self, key):
        self.key = key
        self.parent = None
        self.child = None
        self.left = self
        self.right = self
        self.degree = 0
        self.mark = False

class FibonacciHeap:
    def __init__(self):
        self.min = None
        self.n = 0
        self.trace = False
        self.found = None

    def insert(self, key):
        x = FibNode(key)
        if self.min is None:
            self.min = x
            x.left = x
            x.right = x
        else:
            x.right = self.min
            x.left = self.min.left
            self.min.left.right = x
            self.min.left = x
            if x.key < self.min.key:
                self.min = x
        self.n += 1
        self.consolidate()

    def display(self):
        self._display(self.min)
        print()

    def _display(self, x):
        if x is None:
            return
        temp = x
        while True:
            print(temp.key)
            if temp.child:
                self._display(temp.child)
            print("->")
            temp = temp.right
            if temp == x:
                break

    def find_min(self):
        return self.min.key

    def extract_min(self):
        z = self.min
        if z:
            c = z.child
            k = c
            temp = None
            if c:
                while True:
                    temp = c.right
                    self.insert(c)
                    self.n -= 1
                    c.parent = None
                    c = temp
                    if c == k:
                        break
            z.left.right = z.right
            z.right.left = z.left
            z.child = None
            if z == z.right:
                self.min = None
            else:
                self.min = z.right
                self.consolidate()
            self.n -= 1
            z.left = None
            z.right = None
            return z.key
        else:
            return float('inf')

    def consolidate(self):
        Dofn = int(self._log_phi(self.n))
        A = [None] * (Dofn + 1)
        w = self.min
        if w:
            check = self.min
            while True:
                x = w
                d = x.degree
                while A[d] is not None and A[d] != x:
                    y = A[d]
                    if x.key > y.key:
                        x, y = y, x
                        w = x
                    self._fib_heap_link(y, x)
                    check = x
                    A[d] = None
                    d += 1
                A[d] = x
                w = w.right
                if w == check:
                    break
            self.min = None
            for i in range(Dofn + 1):
                if A[i] is not None:
                    self.insert(A[i])
                    self.n -= 1

    def _fib_heap_link(self, y, x):
        if x == y:
            return
        y.left.right = y.right
        y.right.left = y.left
        y.left = None
        y.right = None
        p = x.child
        if not p:
            y.right = y
            y.left = y
        else:
            y.right = p
            y.left = p.left
            p.left.right = y
            p.left = y
        y.parent = x
        x.child = y
        x.degree += 1
        y.mark = False

    def find(self, k):
        self.found = None
        self._find(k, self.min)
        return self.found

    def _find(self, k, c):
        if self.found or not c:
            return
        temp = c
        while True:
            if k == temp.key:
                self.found = temp
                return
            k_node = temp.child
            self._find(k, k_node)
            temp = temp.right
            if temp == c and not self.found:
                break

    def find_iter(self, k):
        self.found = None
        self._find_value(k, self.min)
        return self.found

    def _find_value(self, k, c):
        if not c:
            return
        que = []
        seen = set()
        que.append(c)
        current = None
        while que and len(seen) <= self.n:
            current = que.pop(0)
            if current in seen:
                continue
            seen.add(current)
            if current.key == k:
                self.found = current
                break
            child = current.child
            if child:
                que.append(child)
                next_sib = child.right
                while next_sib != child:
                    que.append(next_sib)
                    next_sib = next_sib.right
            right = current.right
            if right not in seen:
                que.append(right)

    def decrease_key(self, key, new_val):
        x = self.find(key)
        if x:
            self._decrease_key(x, new_val)

    def _decrease_key(self, x, k):
        if k > x.key:
            return
        x.key = k
        y = x.parent
        if y and x.key < y.key:
            self._cut(x, y)
            self._cascading_cut(y)
        if x.key < self.min.key:
            self.min = x

    def _cut(self, x, y):
        y.degree -= 1
        if y.child == x:
            y.child = x.right
        if y.degree == 0:
            y.child = None
        x.right.left = x.left
        x.left.right = x.right
        x.left = None
        x.right = None
        self.insert(x)
        self.n -= 1
        x.parent = None
        x.mark = False

    def _cascading_cut(self, y):
        z = y.parent
        if z:
            if not y.mark:
                y.mark = True
            else:
                self._cut(y, z)
                self._cascading_cut(z)

    def delete(self, x):
        self.decrease_key(x.key, float('-inf'))
        self.extract_min()

    def _log_phi(self, n):
        return int(round(math.log(n) / math.log((1 + math.sqrt(5)) / 2)))
