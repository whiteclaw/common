package com.whiteclaw.structure;

import java.util.LinkedHashMap;

/**
 * 二叉查找树
 *
 * @author whiteclaw
 * created on 2018-12-25
 */
public class KvBinarySearchTree<K extends Comparable<K>, V> {

    /**
     * 根节点
     */
    private Node<K, V> root;

    /**
     * 树当前节点的个数
     */
    private int size = 0;

    private final ValueMergeFunction<V> mergeFunction;

    public KvBinarySearchTree(ValueMergeFunction<V> mergeFunction) {
        this.mergeFunction = mergeFunction;
    }

    /**
     * 往二叉查找树里面添加数据
     *
     * @param key 新增元素
     */
    public void add(K key, V data) {
        Node<K, V> node = new Node<>(null, key, data, null);
        if (root == null) {
            root = node;
            size++;
        } else {
            add(node, root);
        }
    }

    /**
     * 往指定树下添加新节点
     *
     * @param node   新增加点
     * @param parent 指定树节点
     */
    private void add(Node<K, V> node, Node<K, V> parent) {
        int compare = parent.key.compareTo(node.key);
        if (compare > 0) {
            if (parent.left == null) {
                parent.left = node;
                size++;
            } else {
                add(node, parent.left);
            }
        } else if (compare < 0) {
            if (parent.right == null) {
                parent.right = node;
                size++;
            } else {
                add(node, parent.right);
            }
        } else {
            parent.data = mergeFunction.apply(parent.data, node.data);
        }
    }

    /**
     * 删除指定元素
     *
     * @param key 要删除的元素
     */
    @SuppressWarnings("Duplicates")
    public void deleteKey(K key) {
        // 第一步, 找到要删除的元素所在节点和其父节点
        Node<K, V> node = root;
        Node<K, V> parent = null;
        while (node != null && !node.data.equals(key)) {
            parent = node;
            if (node.key.compareTo(key) < 0) {
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
            Node<K, V> minNode = node.right;
            Node<K, V> minNodeParent = parent;
            while (minNode.left != null) {
                minNodeParent = minNode;
                minNode = minNode.left;
            }
            node.data = minNode.data;
            node = minNode;
            parent = minNodeParent;
        }

        // 删除是叶节点或者仅有一个子节点的节点
        Node<K, V> child = null;
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
    public boolean exists(K e) {
        Node<K, V> node = root;
        while (node != null && node.key.compareTo(e) != 0) {
            if (node.key.compareTo(e) < 0) {
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
    public LinkedHashMap<K, V> preOrder() {
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        preOrder(result, root);
        return result;
    }

    private void preOrder(LinkedHashMap<K, V> result, Node<K, V> node) {
        if (node == null) {
            return;
        }
        result.put(node.key, node.data);
        preOrder(result, node.left);
        preOrder(result, node.right);
    }

    /**
     * 中序遍历树
     *
     * @return 按照遍历顺序的元素列表
     */
    public LinkedHashMap<K, V> inOrder() {
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        inOrder(result, root);
        return result;
    }

    private void inOrder(LinkedHashMap<K, V> result, Node<K, V> node) {
        if (node == null) {
            return;
        }
        inOrder(result, node.left);
        result.put(node.key, node.data);
        inOrder(result, node.right);
    }

    /**
     * 后序遍历树
     *
     * @return 按照遍历顺序的元素列表
     */
    public LinkedHashMap<K, V> postOrder() {
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        postOrder(result, root);
        return result;
    }

    private void postOrder(LinkedHashMap<K, V> result, Node<K, V> node) {
        if (node == null) {
            return;
        }
        postOrder(result, node.left);
        postOrder(result, node.right);
        result.put(node.key, node.data);
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
    public int getHeight(Node<K, V> node) {
        int height = Math.max(getLeftHeight(0, node), getRightHeight(0, node));
        return height > 0 ? height - 1 : 0;
    }

    private int getLeftHeight(int i, Node<K, V> node) {
        if (node == null) {
            return i;
        }
        return getLeftHeight(i + 1, node.left);
    }

    private int getRightHeight(int i, Node<K, V> node) {
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

    public Node<K, V> getRoot() {
        return root;
    }

    /**
     * 节点
     */
    static class Node<K, V> {
        Node<K, V> left;
        K key;
        V data;
        Node<K, V> right;

        Node(Node<K, V> left, K key, V data, Node<K, V> right) {
            this.left = left;
            this.key = key;
            this.data = data;
            this.right = right;
        }
    }

    public static interface ValueMergeFunction<V> {
        /**
         * 合并具有相同key的节点值得仿佛
         *
         * @param v1 原有值
         * @param v2 新元素的值
         * @return 合并后的值
         */
        V apply(V v1, V v2);
    }
}
