package com.datastruct;
/*
 * Create Simple Binary Search Tree
 * 
 * @author: Lely Hiryanto
 * 
 */

 class BTNode<K,V> {
    private K key;    //key ada bilangan bulat
    private V data;   // object data dari sebuah class
    private BTNode<K,V> llink; //left link
    private BTNode<K,V> rlink; //right link
    private Direction direction;

    //constructor
    public BTNode(K k, V data) {
        this.key = k; 
        this.data = data;   
        this.llink = null;  
        this.rlink = null;  
    }

    public void setKey(K key) {
        this.key = key;
    }
    public K getKey() {
        return key;
    }
    public void setData(V data) {
        this.data = data;
    }
    public V getData() {
        return data;
    }
    public void setLlink(BTNode<K, V> llink) {
        this.llink = llink;
    }
    public BTNode<K, V> getLlink() {
        return llink;
    }
    public void setRlink(BTNode<K, V> rlink) {
        this.rlink = rlink;
    }
    public BTNode<K, V> getRlink() {
        return rlink;
    }
    public Direction getDirection() {
        return direction;
    }
}

public class BinaryTree<K, V>{

    //rekursif in order traversal
    public void printInOrder(BTNode<K,V> node) {
        //T sebelumnya adalah child node
        if(node == null) return;
        else {
            //ke left node secara rekursif
            printInOrder(node.getLlink());
            //cetak key dari node
            System.out.print(node.getKey() + ":" + node.getData() + " ");
            //ke right node secara rekursif
            printInOrder(node.getRlink());
        }
    }

    public String inOrderPanel(BTNode<K,V> node) {
        if(node == null) return "";
        else {
            return inOrderPanel(node.getLlink()) + node.getData() + " " + inOrderPanel(node.getRlink());
        }
    }

    public String reverseInOrderPanel(BTNode<K,V> node) {
        if(node == null) return "";
        else {
            return reverseInOrderPanel(node.getRlink()) + node.getData() + " " + reverseInOrderPanel(node.getLlink());
        }
    }
}

