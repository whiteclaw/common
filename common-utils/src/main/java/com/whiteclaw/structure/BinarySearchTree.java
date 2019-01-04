package com.whiteclaw.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * 二叉查找树
 *
 * @author whiteclaw
 * created on 2018-12-25
 */
public class BinarySearchTree<T extends Comparable<T>> {

    /**
     * 根节点
     */
    private Node<T> root;

    /**
     * 树当前节点的个数
     */
    private int size = 0;


    /**
     * 往二叉查找树里面添加数据
     *
     * @param e 新增元素
     */
    public void add(T e) {
        Node<T> node = new Node<>(null, e, null);
        if (root == null) {
            root = node;
        } else {
            add(node, root);
        }
        size++;
    }

    /**
     * 往指定树下添加新节点
     *
     * @param node   新增加点
     * @param parent 指定树节点
     */
    private void add(Node<T> node, Node<T> parent) {
        int compare = parent.data.compareTo(node.data);
        if (compare > 0) {
            if (parent.left == null) {
                parent.left = node;
            } else {
                add(node, parent.left);
            }
        } else if (compare < 0) {
            if (parent.right == null) {
                parent.right = node;
            } else {
                add(node, parent.right);
            }
        }
    }

    /**
     * 删除指定元素
     *
     * @param e 要删除的元素
     */
    public void delete(T e) {
        // 第一步, 找到要删除的元素所在节点和其父节点
        Node<T> node = root;
        Node<T> parent = null;
        while (node != null && !node.data.equals(e)) {
            parent = node;
            if (node.data.compareTo(e) < 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        // 没有找到, 不做处理
        if (node == null) {
            return;
        }

        // 如果要删除的节点下面有两个子节点, 则找到右子树种最小的节点
        // 将这个最小的节点和要删除的节点对换
        if (node.left != null && node.right != null) {
            Node<T> minNode = node.right;
            Node<T> minNodeParent = parent;
            while (minNode.left != null) {
                minNodeParent = minNode;
                minNode = minNode.left;
            }
            node.data = minNode.data;
            node = minNode;
            parent = minNodeParent;
        }

        // 删除是叶节点或者仅有一个子节点的节点
        Node<T> child = null;
        if (node.left != null) {
            child = node.left;
        } else if (node.right != null) {
            child = node.right;
        }

        if (parent == null) {
            root = child;
        } else if (parent.left.equals(node)) {
            parent.left = child;
        } else {
            parent.right = child;
        }

    }

    /**
     * 查找是否存在指定元素
     *
     * @param e 要查找的元素
     * @return 是否存在e
     */
    public boolean exists(T e) {
        Node<T> node = root;
        while (node != null && node.data.compareTo(e) != 0) {
            if (node.data.compareTo(e) < 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return node != null;
    }

    /**
     * 前序遍历树
     *
     * @return 按照遍历顺序的元素列表
     */
    public List<T> preOrder() {
        List<T> result = new ArrayList<>(size);
        preOrder(result, root);
        return result;
    }

    private void preOrder(List<T> result, Node<T> node) {
        if (node == null) {
            return;
        }
        result.add(node.data);
        preOrder(result, node.left);
        preOrder(result, node.right);
    }

    /**
     * 中序遍历树
     *
     * @return 按照遍历顺序的元素列表
     */
    public List<T> inOrder() {
        List<T> result = new ArrayList<>(size);
        inOrder(result, root);
        return result;
    }

    private void inOrder(List<T> result, Node<T> node) {
        if (node == null) {
            return;
        }
        inOrder(result, node.left);
        result.add(node.data);
        inOrder(result, node.right);
    }

    /**
     * 后序遍历树
     *
     * @return 按照遍历顺序的元素列表
     */
    public List<T> postOrder() {
        List<T> result = new ArrayList<>(size);
        postOrder(result, root);
        return result;
    }

    private void postOrder(List<T> result, Node<T> node) {
        if (node == null) {
            return;
        }
        postOrder(result, node.left);
        postOrder(result, node.right);
        result.add(node.data);
    }

    /**
     * 获取树的高度
     *
     * @return 当前树的高度
     */
    public int getHeight() {
        int height = Math.max(getLeftHeight(0, root), getRightHeight(0, root));
        return height > 0 ? height - 1 : 0;
    }

    /**
     * 获取指定树节点的高度
     *
     * @param node 指定节点
     * @return 指定节点的高度
     */
    public int getHeight(Node<T> node) {
        int height = Math.max(getLeftHeight(0, node), getRightHeight(0, node));
        return height > 0 ? height - 1 : 0;
    }

    private int getLeftHeight(int i, Node<T> node) {
        if (node == null) {
            return i;
        }
        return getLeftHeight(i + 1, node.left);
    }

    private int getRightHeight(int i, Node<T> node) {
        if (node == null) {
            return i;
        }
        return getRightHeight(i + 1, node.right);
    }

    /**
     * 获取节点个数
     *
     * @return 当前节点的个数
     */
    public int getSize() {
        return size;
    }

    public Node<T> getRoot() {
        return root;
    }

    /**
     * 节点
     */
    static class Node<T> {
        Node<T> left;
        T data;
        Node<T> right;

        Node(Node<T> left, T data, Node<T> right) {
            this.left = left;
            this.data = data;
            this.right = right;
        }
    }
}
