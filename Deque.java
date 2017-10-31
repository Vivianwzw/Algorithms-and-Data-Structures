
// Ziwei Wang
// vivianwang
// Zhuo Chen
// aslantachen

import tester.*;

//*********************IPred<T>******************
// represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

// checks if the string is abc
class FindABC implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("abc");
  }
}

// checks if the string is bcd
class FindBCD implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("bcd");
  }
}

// checks if the string is cde
class FindCDE implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("cde");
  }
}

// checks if the string is def
class FindDEF implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("def");
  }
}

// checks if the string is efg
class FindEFG implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("efg");
  }
}

// *********************ANode<T>******************
// to represent a abstract class ANode<T>
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts the number of nodes in a list Deque,
  // not including the header node.
  public abstract int sizeHelper(int acc);

  // add the node between previous node and next node
  // that are given
  public void addBetween(ANode<T> prev, ANode<T> next) {
    this.prev = prev;
    this.next = next;
    prev.next = this;
    next.prev = this;
  }

  // remove the node between previous node and next node
  // that are given
  public abstract T remove(ANode<T> n1, ANode<T> n2);

  // checks which node first passes the given predicate
  public abstract ANode<T> findSeparate(IPred<T> pred);

  // a helper method for testing
  // return the data of the first node of deque
  public abstract T getHead();

  // a helper method for testing
  // return the data of the last node of deque
  public abstract T getTail();
}

// *********************Sentinel<T>********************
// to represent a class Sentinel<T>
class Sentinel<T> extends ANode<T> {

  // constructor that takes zero arguments
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // counts the number of nodes in a list Deque,
  // not including the header node.
  public int size() {
    return this.next.sizeHelper(0);
  }

  // counts the number of nodes in a list Deque,
  // not including the header node.
  public int sizeHelper(int acc) {
    return acc;
  }

  // throw a RuntimeException if an attempt is made
  // to remove from an empty list.
  public T remove(ANode<T> n1, ANode<T> n2) {
    throw new RuntimeException("no nodes in list");
  }

  // checks which node first passes the given predicate
  public ANode<T> findHelper(IPred<T> pred) {
    return this.next.findSeparate(pred);
  }

  // returns the sentinel if no node passes the given predicate
  public ANode<T> findSeparate(IPred<T> pred) {
    return this;
  }

  // a helper method for testing
  // return the data of the first node of deque
  public T getHead() {
    return this.next.getHead();
  }

  // a helper method for testing
  // return the data of the last node of deque
  public T getTail() {
    return this.prev.getTail();
  }

}

// *********************Node<T>********************
// to represent a class Node<T>
class Node<T> extends ANode<T> {
  T data;

  // constructor1
  // takes just a value of type T,
  // initializes the data field, and then initializes next and prev to null.
  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }

  // constructor2
  // take a value of type T and two ANode<T> nodes,
  // initialize the data field to the given value,
  // initialize the next and prev fields to the given nodes,
  // and also update the given nodes to refer back to this node.
  Node(T data, ANode<T> n1, ANode<T> n2) {
    this.data = data;

    if (n1 == null || n2 == null) {
      new IllegalArgumentException("Either of given nodes is null");
    }
    else {
      this.next = n1;
      this.prev = n2;
      this.prev.next = this;
      this.next.prev = this;
    }
  }

  // counts the number of nodes in a list Deque,
  // not including the header node.
  public int sizeHelper(int acc) {
    return this.next.sizeHelper(acc + 1);
  }

  // remove the node between previous node and next node
  // that are given and return the data of the removed node
  public T remove(ANode<T> n1, ANode<T> n2) {
    T temp = this.data;
    n1.next = n2;
    n2.prev = n1;
    return temp;
  }

  // checks which node first passes the given predicate
  public ANode<T> findSeparate(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findSeparate(pred);
    }
  }

  // a helper method for testing
  // return the data of the first node of deque
  public T getHead() {
    return this.data;
  }

  // a helper method for testing
  // return the data of the last node of deque
  public T getTail() {
    return this.data;
  }

}

// **************************Deque<T>**********************
// to represent a class Deque<T>
class Deque<T> {
  Sentinel<T> header;

  // constructor1
  Deque() {
    this.header = new Sentinel<T>();
  }

  // constructor2
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts the number of nodes in a list Deque,
  // not including the header node.
  public int size() {
    return this.header.size();
  }

  // consumes a value of type T and inserts it at the front of the list
  public void addAtHead(T data) {
    Node<T> node = new Node<T>(data);
    node.addBetween(this.header, this.header.next);
  }

  // consumes a value of type T and inserts it at the front of the list
  public void addAtTail(T data) {
    Node<T> node = new Node<T>(data);
    node.addBetween(this.header.prev, this.header);
  }

  // consumes a value of type T and inserts it at the front of the list
  public T removeFromHead() {
    return this.header.next.remove(this.header, this.header.next.next);
  }

  // consumes a value of type T and inserts it at the front of the list
  public T removeFromTail() {
    return this.header.prev.remove(this.header.prev.prev, this.header);
  }

  // produces the first node in this Deque for which the given predicate returns
  // true
  public ANode<T> find(IPred<T> pred) {
    return this.header.findHelper(pred);
  }

  // removes the given node from this deque
  public void removeNode(ANode<T> n) {
    n.remove(n.prev, n.next);
  }

  // a helper method for testing
  // return the data of the first node of deque
  public T getHead() {
    return this.header.getHead();
  }

  // a helper method for testing
  // return the data of the last node of deque
  public T getTail() {
    return this.header.getTail();
  }
}

// ****************************Examples***************************
class ExamplesDeque {
  Sentinel<String> sen1 = new Sentinel<String>();
  Deque<String> deque1 = new Deque<String>(sen1);

  Sentinel<String> sen2 = new Sentinel<String>();
  Deque<String> deque2 = new Deque<String>(sen2);

  Sentinel<String> sen3 = new Sentinel<String>();
  Deque<String> deque3 = new Deque<String>(sen3);

  // representing Nodes<String>
  Node<String> node1 = new Node<String>("abc", sen2, sen2);
  Node<String> node2 = new Node<String>("bcd", sen2, node1);
  Node<String> node3 = new Node<String>("cde", sen2, node2);
  Node<String> node4 = new Node<String>("def", sen2, node3);

  Node<String> node5 = new Node<String>("f", sen3, sen3);
  Node<String> node6 = new Node<String>("c", sen3, node5);
  Node<String> node7 = new Node<String>("z", sen3, node6);
  Node<String> node8 = new Node<String>("a", sen3, node7);
  Node<String> node9 = new Node<String>("k", sen3, node8);

  IPred<String> findABC = new FindABC();
  IPred<String> findBCD = new FindBCD();
  IPred<String> findCDE = new FindCDE();
  IPred<String> findDEF = new FindDEF();
  IPred<String> findEFG = new FindEFG();

  void initConditions() {

    // representing an empty list
    deque1 = new Deque<String>();
    sen2 = new Sentinel<String>();
    deque2 = new Deque<String>(sen2);
    sen3 = new Sentinel<String>();
    deque3 = new Deque<String>(sen3);

    // representing Nodes<String>
    node1 = new Node<String>("abc", sen2, sen2);
    node2 = new Node<String>("bcd", sen2, node1);
    node3 = new Node<String>("cde", sen2, node2);
    node4 = new Node<String>("def", sen2, node3);

    node5 = new Node<String>("summer", sen3, sen3);
    node6 = new Node<String>("yoyo", sen3, node5);
    node7 = new Node<String>("noexams", sen3, node6);
    node8 = new Node<String>("noassignments", sen3, node7);
    node9 = new Node<String>("noclasses", sen3, node8);
  }

  // tests for getHead()
  boolean testgetHead(Tester t) {
    this.initConditions();
    return t.checkExpect(this.deque2.getHead(), "abc") 
        && t.checkExpect(this.deque3.getHead(), "summer");
  }

  // tests for getHead()
  boolean testgetTail(Tester t) {
    this.initConditions();
    return t.checkExpect(this.deque2.getTail(), "def") 
        && t.checkExpect(this.deque3.getTail(), "noclasses");
  }

  // tests for Size()
  boolean testSize(Tester t) {
    this.initConditions();
    return t.checkExpect(this.deque1.size(), 0) 
        && t.checkExpect(this.deque2.size(), 4)
        && t.checkExpect(this.deque3.size(), 5);
  }

  // tests for addAtHead()
  boolean testAddAtHead(Tester t) {
    this.initConditions();
    this.deque2.addAtHead("fundies");
    this.deque3.addAtHead("great");
    return t.checkExpect(this.deque2.getHead(), "fundies")
        && t.checkExpect(this.deque2.getTail(), "def")
        && t.checkExpect(this.deque2.size(), 5)
        && t.checkExpect(this.deque3.getHead(), "great")
        && t.checkExpect(this.deque3.getTail(), "noclasses")
        && t.checkExpect(this.deque3.size(), 6);
  }
  

  // tests for addAtTail()
  boolean testAddAtTail(Tester t) {
    this.initConditions();
    this.deque2.addAtTail("great");
    return t.checkExpect(this.deque2.getTail(), "great")
        && t.checkExpect(this.deque2.getHead(), "abc");
  }

  // tests for removeFromHead()
  boolean testRemoveFromHead(Tester t) {
    this.initConditions();
    return t.checkExpect(this.deque2.removeFromHead(), this.node1.data)
        && t.checkExpect(this.deque3.removeFromHead(), this.node5.data);
  }

  // tests for removeFromTail
  boolean testRemoveFromTail(Tester t) {
    this.initConditions();
    return t.checkExpect(this.deque2.removeFromTail(), this.node4.data)
        && t.checkExpect(this.deque3.removeFromTail(), this.node9.data);
  }

  // tests for Find()
  boolean testFind(Tester t) {
    this.initConditions();
    return t.checkExpect(deque2.find(this.findABC), node1)
        && t.checkExpect(deque2.find(this.findBCD), node2)
        && t.checkExpect(deque2.find(this.findCDE), node3)
        && t.checkExpect(deque2.find(this.findDEF), node4)
        && t.checkExpect(deque2.find(this.findEFG), sen2);
  }

  // tests for removeNode()
  boolean testRemoveNode(Tester t) {
    this.initConditions();
    deque2.removeNode(node1);
    return t.checkExpect(deque2.getHead(), "bcd") && t.checkExpect(deque2.getTail(), "def");
  }
  
  boolean testConstructorException(Tester t){
    return t.checkConstructorException(
        new IllegalArgumentException("Either of given nodes is null"),
        "Node",1, null, this.node1);
  }
}
