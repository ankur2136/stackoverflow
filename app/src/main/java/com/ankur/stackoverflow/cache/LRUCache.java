package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.common.ValueWrapper;

import java.util.concurrent.ConcurrentMap;


public class LRUCache extends LocalConcurrentMapCache {

    private DoubleLinkedListNode head;
    private DoubleLinkedListNode end;

    public LRUCache(String name, ConcurrentMap<Object, Object> store, long expiration, long maxSize) {
        super(name, store, expiration, maxSize);
    }

    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (valueWrapper != null) {
            DoubleLinkedListNode latest = (DoubleLinkedListNode) valueWrapper.getValue();
            removeNode(latest);
            setHead(latest);
            return new ValueWrapper(latest.val);
        }
        return valueWrapper;
    }

    public void put(Object key, Object value) {
        if (evictionRequired()) {
            evict();
        }
        DoubleLinkedListNode newNode =
                new DoubleLinkedListNode(key, value);
        setHead(newNode);
        super.put(key, newNode);
    }

    public void evict(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (valueWrapper != null) {
            DoubleLinkedListNode node = (DoubleLinkedListNode) valueWrapper.getValue();
            removeNode(node);
        }
        super.evict(key);
    }

    @Override
    public void evict() {
        super.evict(end.key);
        end = end.pre;
        if (end != null) {
            end.next = null;
        }
    }

    public void removeNode(DoubleLinkedListNode node) {
        DoubleLinkedListNode cur = node;
        DoubleLinkedListNode pre = cur.pre;
        DoubleLinkedListNode post = cur.next;

        if (pre != null) {
            pre.next = post;
        } else {
            head = post;
        }

        if (post != null) {
            post.pre = pre;
        } else {
            end = pre;
        }
    }

    public void setHead(DoubleLinkedListNode node) {
        node.next = head;
        node.pre = null;
        if (head != null) {
            head.pre = node;
        }

        head = node;
        if (end == null) {
            end = node;
        }
    }

    class DoubleLinkedListNode {
        public Object val;
        public Object key;
        public DoubleLinkedListNode pre;
        public DoubleLinkedListNode next;

        public DoubleLinkedListNode(Object key, Object value) {
            val = value;
            this.key = key;
        }
    }
}