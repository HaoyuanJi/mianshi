/*
 * Click `Run` to execute the snippet below!
 */

import java.io.*;
import java.util.*;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */

class Solution<K, V> {
  private static final long defaultTTLInMilli = 600000; //10 min
  private static class Node<K, V> {
    K key;
    V value;
    long timeStampInMilli;
    long ttl;
    Node<K, V> next;
    Node<K, V> prev;
    
    public Node (K key, V value) {
      this.key = key;
      this.value = value;
      this.ttl = defaultTTLInMilli;
    }
    
    public Node (K key, V value, long ttl) {
      this.key = key;
      this.value = value;
      this.ttl = ttl;
    }   
  }
  
  private static class DoubleLinkedList<K, V> {
    Node<K, V> head;
    Node<K, V> tail;
    
    public void removeHead() {
      removeNode(head);
    }
    
    public Node<K, V> getHead() {
      return head;
    }
    
    public void removeNode(Node<K, V> node) {
      if (node.prev != null) {
        node.prev.next = node.next;
      } else {
        head = node.next;
      }
      
      if (node.next != null) {
        node.next.prev = node.prev;
      } else {
        tail = node.prev;
      }
      
    }
    
    public void offerNode(Node<K, V> node) {
      if (tail != null) {
        tail.next = node;
      }
      
      node.prev = tail;
      node.next = null;
      tail = node;
      
      if (head == null) {
        head = tail;
      }
    }
  }
  
  public HashMap<K, Node<K, V>> map = new HashMap<K, Node<K, V>>(); 
  public DoubleLinkedList<K, V> store = new DoubleLinkedList<K, V>();
  int capacity = 3;
  
  public Solution() {}
  
  public Solution(int capacity) {this.capacity = capacity;}
  
  public V get(K key) {
    Node<K, V> node = map.get(key);
    if (node == null) {
      return null;
    }
    
    store.removeNode(node);
    store.offerNode(node);
    
    return node.value;
  }
  
  public void put(K key, V value) {
    if (map.containsKey(key)) {
      Node<K, V> node = map.get(key);
      node.value = value;
      
      store.removeNode(node);
      store.offerNode(node);
    } else {
      // System.out.println("K is: " + key + " V is : " + value);
      if (map.size() >= capacity) {
        Node<K, V> head = store.getHead();
        map.remove(head.key);
        store.removeHead();
      }
      
      Node<K,V> node = new Node<K, V>(key, value);
      store.offerNode(node);
      map.put(key, node);
      // System.out.println(map.get(key).value);
    }
  }
  
  public static void main(String[] args) {
    Solution<Integer, Integer> cache = new Solution<Integer, Integer>();
    
    cache.put(0, 12345);
    // System.out.println(cache.map.get(0).value);
    System.out.println(cache.store.head.key);
    cache.put(1, 678);
    System.out.println(cache.store.head.key);
    cache.put(2, 91011);
    System.out.println(cache.store.head.key);
    // System.out.println(cache.get(8));
    cache.put(3, 1213);
    System.out.println(cache.get(0));
    // System.out.println(cache.get(8));
    cache.put(4, 1415);
    // System.out.println(cache.map);
    cache.put(5, 1617);
    cache.put(6, 1819);
    cache.put(7, 2021);
    // System.out.println(cache.map);
    // System.out.println(cache.get(0));
    
//     ArrayList<String> strings = new ArrayList<String>();
//     strings.add("Hello, World!");
//     strings.add("Welcome to CoderPad.");
//     strings.add("This pad is running Java " + Runtime.version().feature());

//     for (String string : strings) {
//       System.out.println(string);
//     }
  }
}
