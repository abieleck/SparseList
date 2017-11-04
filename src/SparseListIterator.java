import java.util.*;
import java.util.function.Consumer; // dlaczego bez tego IDE wywala błąd?

public class SparseListIterator<E> implements ListIterator<E> {

    SparseList<E> myList;
    private HashMap<Integer, E> sparseMap = new HashMap<>();
    private int indexOfNext;
    private boolean removeAllowed;

    public SparseListIterator(SparseList<E> myList, HashMap<Integer, E> sparseMap) {
        this.myList = myList;
        this.sparseMap = sparseMap;
    }

    @Override
    public boolean hasNext() {

        return indexOfNext < myList.size();
    }

    @Override
    public E next() {

        if (indexOfNext >= myList.size()) {
            throw new NoSuchElementException();
        }
        indexOfNext++;
        removeAllowed  = true;
        return sparseMap.getOrDefault(indexOfNext-1, myList.getDefaultValue());
    }

    @Override
    public boolean hasPrevious() {
        return indexOfNext > 0;
    }

    @Override
    public E previous() {
        if (indexOfNext <= 0) {
            throw new NoSuchElementException();
        }
        indexOfNext--;
        removeAllowed = true;
        return sparseMap.getOrDefault(indexOfNext, myList.getDefaultValue());
    }

    @Override
    public int nextIndex() {
        return indexOfNext;
    }

    @Override
    public int previousIndex() {
        return indexOfNext-1;
    }

    @Override
    public void remove() throws IllegalStateException {
        if (!removeAllowed) {
            throw new IllegalStateException();
        }
        removeAllowed  = false;
        indexOfNext--;
        myList.remove(indexOfNext);
    }

    @Override
    public void set(E e) {
        //TODO
    }

    @Override
    public void add(E e) {
        //TODO
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) throws NullPointerException {

        if (action == null) {
            throw new NullPointerException();
        }
        while(hasNext()) {
            action.accept(next());
        }
    }
}
