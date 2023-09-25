#include <iostream>
#include <cmath>

class FibNode {
public:
    int key;
    FibNode* parent;
    FibNode* child;
    FibNode* left;
    FibNode* right;
    int degree;
    bool mark;

    FibNode(int key) : key(key), parent(nullptr), child(nullptr), left(this), right(this), degree(0), mark(false) {}
};

class FibonacciHeap {
private:
    FibNode* min;
    int n;
    bool trace;
    FibNode* found;

public:
    FibonacciHeap() : min(nullptr), n(0), trace(false), found(nullptr) {}

    void insert(int key) {
        FibNode* x = new FibNode(key);
        if (min == nullptr) {
            min = x;
            x->left = x;
            x->right = x;
        } else {
            x->right = min;
            x->left = min->left;
            min->left->right = x;
            min->left = x;
            if (x->key < min->key) {
                min = x;
            }
        }
        n++;
        consolidate();
    }

    void display() {
        _display(min);
        std::cout << std::endl;
    }

    int findMin() {
        return min->key;
    }

    int extractMin() {
        FibNode* z = min;
        if (z) {
            FibNode* child = z->child;
            FibNode* k = child;
            FibNode* temp = nullptr;
            if (child) {
                do {
                    temp = child->right;
                    insert(child);
                    n--;
                    child->parent = nullptr;
                    child = temp;
                } while (child && child != k);
            }
            z->left->right = z->right;
            z->right->left = z->left;
            z->child = nullptr;
            if (z == z->right) {
                min = nullptr;
            } else {
                min = z->right;
                consolidate();
            }
            n--;
            z->left = nullptr;
            z->right = nullptr;
            int key = z->key;
            delete z;
            return key;
        } else {
            return INT_MAX;
        }
    }

    void consolidate() {
        int Dofn = static_cast<int>(log(n) / log((1 + sqrt(5)) / 2));
        FibNode** A = new FibNode*[Dofn + 1]();
        FibNode* w = min;
        if (w) {
            FibNode* check = min;
            do {
                FibNode* x = w;
                int d = x->degree;
                while (A[d]) {
                    FibNode* y = A[d];
                    if (x->key > y->key) {
                        FibNode* temp = x;
                        x = y;
                        y = temp;
                        w = x;
                    }
                    fibHeapLink(y, x);
                    check = x;
                    A[d] = nullptr;
                    d++;
                }
                A[d] = x;
                w = w->right;
            } while (w && w != check);
            min = nullptr;
            for (int i = 0; i <= Dofn; ++i) {
                if (A[i]) {
                    insert(A[i]);
                    n--;
                }
            }
        }
        delete[] A;
    }

    void fibHeapLink(FibNode* y, FibNode* x) {
        if (x == y) {
            return;
        }
        y->left->right = y->right;
        y->right->left = y->left;
        y->left = nullptr;
        y->right = nullptr;
        FibNode* p = x->child;
        if (!p) {
            y->right = y;
            y->left = y;
        } else {
            y->right = p;
            y->left = p->left;
            p->left->right = y;
            p->left = y;
        }
        y->parent = x;
        x->child = y;
        x->degree++;
        y->mark = false;
    }

    FibNode* find(int k) {
        found = nullptr;
        findKey(k, min);
        return found;
    }

    void findKey(int k, FibNode* c) {
        if (found || !c) {
            return;
        }
        FibNode* temp = c;
        do {
            if (k == temp->key) {
                found = temp;
                return;
            }
            FibNode* kNode = temp->child;
            findKey(k, kNode);
            temp = temp->right;
        } while (temp != c && !found);
    }

    void findIter(int k) {
        found = nullptr;
        findValue(k, min);
    }

    void findValue(int k, FibNode* c) {
        if (!c) {
            return;
        }
        std::queue<FibNode*> que;
        std::unordered_set<FibNode*> seen;
        que.push(c);
        FibNode* current = nullptr;
        while (!que.empty() && seen.size() <= n) {
            current = que.front();
            que.pop();
            if (seen.find(current) != seen.end()) {
                continue;
            }
            seen.insert(current);
            if (current->key == k) {
                found = current;
                break;
            }
            FibNode* child = current->child;
            if (child) {
                que.push(child);
                FibNode* nextSib = child->right;
                while (nextSib != child) {
                    que.push(nextSib);
                    nextSib = nextSib->right;
                }
            }
            FibNode* right = current->right;
            if (seen.find(right) == seen.end()) {
                que.push(right);
            }
        }
    }

    void decreaseKey(int key, int newVal) {
        FibNode* x = find(key);
        if (x) {
            decreaseKey(x, newVal);
        }
    }

    void decreaseKey(FibNode* x, int k) {
        if (k > x->key) {
            return;
        }
        x->key = k;
        FibNode* y = x->parent;
        if (y && x->key < y->key) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x->key < min->key) {
            min = x;
        }
    }

    void cut(FibNode* x, FibNode* y) {
        y->degree--;
        if (y->child == x) {
            y->child = x->right;
        }
        if (y->degree == 0) {
            y->child = nullptr;
        }
        x->right->left = x->left;
        x->left->right = x->right;
        x->left = nullptr;
        x->right = nullptr;
        insert(x);
        n--;
        x->parent = nullptr;
        x->mark = false;
    }

    void cascadingCut(FibNode* y) {
        FibNode* z = y->parent;
        if (z) {
            if (!y->mark) {
                y->mark = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    void deleteNode(FibNode* x) {
        decreaseKey(x, INT_MIN);
        extractMin();
    }

    double log_phi(int n) {
        return log(n) / log((1 + sqrt(5)) / 2);
    }
};
