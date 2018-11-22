package bgu.spl.mics;

import java.util.*;

public class myVector <T> implements List<T> {
    private Vector<T> v;
    @Override
    public synchronized int size() {
        return v.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return v.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return v.contains(o);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return v.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return v.toArray();
    }

    @Override
    public synchronized <T1> T1[] toArray(T1[] a) {
        return v.toArray(a);
    }

    @Override
    public synchronized boolean add(T t) {
        return v.add(t);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return v.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return v.contains(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return v.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        return v.addAll(index,c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return v.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return v.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        v.clear();
    }

    @Override
    public synchronized T get(int index) {
        return v.get(index);
    }

    @Override
    public synchronized T set(int index, T element) {
        return v.set(index,element);
    }

    @Override
    public synchronized void add(int index, T element) {
        v.add(index,element);
    }

    @Override
    public synchronized T remove(int index) {
        return v.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return v.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return v.lastIndexOf(o);
    }

    @Override
    public synchronized ListIterator<T> listIterator() {
        return v.listIterator();
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        return v.listIterator(index);
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return v.subList(fromIndex,toIndex);
    }
}
