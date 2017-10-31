
// Ziwei Wang
// vivianwang
// Zhuo Chen
// aslantachen


//*******************************Book***********************************
// to represent a book
class Book {
  String title;
  String author;
  int price;

  // constructor
  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

//*****************IComparator<T>************************
// to represent an general purpose interface IComparator<T>
// that describes comparisons over any particular type
interface IComparator<T> {
  int compare(T t1, T t2);
}

// to compare books by their title
// to see if the title of b1 comes before that of b2
// if the return value is < 0, b1 should go before b2
class BooksByTitle implements IComparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.title.compareTo(b2.title);
  }
}

// to compare books by their author
// to see if the author of b1 comes before that of b2
// if the return value is < 0, b1 should go before b2
class BooksByAuthor implements IComparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.author.compareTo(b2.author);
  }
}

// to compare books by their price
// if the return value is < 0, b1 should go before b2
class BooksByPrice implements IComparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.price - b2.price;
  }
}

//********************ABST<T>*************************************
// to represent a abstract ABST
abstract class BinarySearchTree<T> {
  IComparator<T> order;

  // constructor
  BinarySearchTree(IComparator<T> order) {
    this.order = order;
  }

  // to take an item and produces a new binary search tree with the given item
  // inserted in the correct place.
  public abstract BinarySearchTree<T> insert(T t);

  // to return the smallest item (according to the tree order) contained in this
  // tree
  public abstract T getLeast();

  // helps to return the smallest item (according to the tree order) contained
  // in this tree
  public abstract T getLeastHelper(T acc);

  // that returns the tree containing everything except the least item of this
  // tree.
  public abstract BinarySearchTree<T> getAllButLeast();

  // return -1 if it's a Leaf<T> and return 1 if it's a Node<T>
  public abstract int nodeOrLeaf();

  // to determine whether this binary search tree is the same as the given one:
  // that is, they have matching structure and matching data in all nodes.
  public abstract boolean sameTree(BinarySearchTree<T> t);

  // helps to determine whether this binary search tree is the same as the given
  // one:
  // that is, they have matching structure and matching data in all nodes.
  public abstract boolean sameLeaf(Leaf<T> t);

  // helps to determine whether this binary search tree is the same as the given
  // one:
  // that is, they have matching structure and matching data in all nodes.
  public abstract boolean sameNode(Node<T> t);

  // determines whether this binary search tree contains the same books in the
  // same order as the given tree.
  public abstract boolean sameData(BinarySearchTree<T> t);

  // represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public abstract IList<T> buildList(IList<T> l);

  // represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public abstract IList<T> buildListHelper(IList<T> l);

  // determines whether the binary search tree contains the same books as the
  // given list (in the same order).
  public abstract boolean sameAsList(IList<T> l);
}

// *********************************Leaf<T>*****************************************
// to represent a Leaf class
class Leaf<T> extends BinarySearchTree<T> {
  // constructor
  Leaf(IComparator<T> order) {
    super(order);
  }

  // to take an item and produces a new binary search tree with the given item
  // inserted in the correct place.
  public BinarySearchTree<T> insert(T t) {
    return new Node<T>(this.order, t, new Leaf<T>(this.order), new Leaf<T>(this.order));
  }

  // to return the smallest item (according to the tree order) contained in this
  // tree
  public T getLeast() {
    throw new RuntimeException("No least item of an empty tree");
  }

  // helper of getLeast()
  // use accumulator
  // helps to return the smallest item (according to the tree order) contained
  // in this tree
  public T getLeastHelper(T acc) {
    return acc;
  }

  // that returns the tree containing everything except the least item of this
  // tree.
  public BinarySearchTree<T> getAllButLeast() {
    throw new RuntimeException("No items in an empty tree");
  }

  // helps to return the tree containing everything except the least item of
  // this tree.
  public int nodeOrLeaf() {
    return -1;
  }

  // to determine whether this binary search tree is the same as the given one:
  // that is, they have matching structure and matching data in all nodes.
  public boolean sameTree(BinarySearchTree<T> t) {
    return t.sameLeaf(this);
  }

  // helps to determine whether this binary search tree is the same as the given
  // one:
  // that is, they have matching structure and matching data in all nodes.
  public boolean sameLeaf(Leaf<T> t) {
    return true;
  }

  // helps to determine whether this binary search tree is the same as the given
  // one:
  // that is, they have matching structure and matching data in all nodes.
  public boolean sameNode(Node<T> t) {
    return false;
  }

  // determines whether this binary search tree contains the same books in the
  // same order as the given tree.
  public boolean sameData(BinarySearchTree<T> t) {
    return t.sameLeaf(this);
  }

  // represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public IList<T> buildList(IList<T> l) {
    return l;
  }

  // helps to represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public IList<T> buildListHelper(IList<T> l) {
    return l;
  }

  // determines whether the binary search tree contains the same books as the
  // given list (in the same order).
  public boolean sameAsList(IList<T> l) {
    return this.sameTree(l.buildTree(this));
  }
}

// to represent a Node class
class Node<T> extends BinarySearchTree<T> {
  T data;
  BinarySearchTree<T> left;
  BinarySearchTree<T> right;

  // constructor
  Node(IComparator<T> order, T data, BinarySearchTree<T> left, BinarySearchTree<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  // to take an item and produces a new binary search tree with the given item
  // inserted in the correct place.
  public BinarySearchTree<T> insert(T t) {
    if (this.order.compare(t, this.data) < 0) {
      return new Node<T>(this.order, this.data, this.left.insert(t), this.right);
    }
    else {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(t));
    }
  }

  // to return the smallest item (according to the tree order) contained in this
  // tree
  public T getLeast() {
    return this.left.getLeastHelper(this.data);
  }

  // helper of getLeast()
  // use accumulator
  // helps to return the smallest item (according to the tree order) contained
  // in this tree
  public T getLeastHelper(T acc) {
    return this.left.getLeastHelper(this.data);
  }

  // that returns the tree containing everything except the least item of this
  // tree.
  public BinarySearchTree<T> getAllButLeast() {
    if (this.left.nodeOrLeaf() < 0 && this.right.nodeOrLeaf() < 0) {
      return new Leaf<T>(this.order);
    }
    else if (this.left.nodeOrLeaf() < 0 && this.right.nodeOrLeaf() > 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getAllButLeast(), this.right);
    }
  }

  // helps to return the tree containing everything except the least item of
  // this tree.
  public int nodeOrLeaf() {
    return 1;
  }

  // to determine whether this binary search tree is the same as the given one:
  // that is, they have matching structure and matching data in all nodes.
  public boolean sameTree(BinarySearchTree<T> t) {
    return t.sameNode(this);
  }

  // helps to determine whether this binary search tree is the same as the given
  // one:that is, they have matching structure and matching data in all nodes.
  public boolean sameLeaf(Leaf<T> t) {
    return false;
  }

  // helps to determine whether this binary search tree is the same as the given
  // one ,that is, they have matching structure and matching data in all nodes.
  public boolean sameNode(Node<T> t) {
    return t.order.compare(this.data, t.data) == 0 && t.left.sameTree(this.left)
        && t.right.sameTree(this.right);
  }

  // determines whether this binary search tree contains the same books in the
  // same order as the given tree.
  public boolean sameData(BinarySearchTree<T> t) {
    if (t.nodeOrLeaf() < 0) {
      return false;
    }
    else if (this.left.nodeOrLeaf() < 0 && this.right.nodeOrLeaf() < 0) {
      return t.sameTree(this);
    }
    {
      return this.order.compare(t.getLeast(), this.getLeast()) == 0
          && this.getAllButLeast().sameData(t.getAllButLeast());
    }
  }

  // represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public IList<T> buildList(IList<T> l) {
    return this.buildListHelper(l);
  }

  // helps to represent the binary search tree that consumes a list of books
  // and adds to it one at a time all items from this tree in the sorted order.
  public IList<T> buildListHelper(IList<T> l) {
    return this.getAllButLeast().buildListHelper(new ConsList<T>(this.getLeast(), l));
  }

  // determines whether the binary search tree contains the same books as the
  // given list (in the same order).
  public boolean sameAsList(IList<T> l) {
    return this.sameTree(l.buildTree(new Leaf<T>(this.order)));
  }
}

// *************************************IList<T>**********************************************
interface IList<T> {
  // to insert into it all items in this list,
  // returning at the end a binary search tree that contains all items in this list.
  BinarySearchTree<T> buildTree(BinarySearchTree<T> t);
}

// **************************************MtList<T>**********************************
class MtList<T> implements IList<T> {

  // to insert into it all items in this list,
  // returning at the end a binary search tree that contains all items in this
  // list.
  public BinarySearchTree<T> buildTree(BinarySearchTree<T> t) {
    return t;
  }

}

// *************************************ConsList<T>********************************
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // to insert into it all items in this list,
  // returning at the end a binary search tree that contains all items in this
  // list.
  public BinarySearchTree<T> buildTree(BinarySearchTree<T> t) {
    return this.rest.buildTree(t.insert(this.first));
  }
}
