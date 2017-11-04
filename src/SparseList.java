import java.util.*;

/*
 * This list cannot hold nulls
 */
public class SparseList<E> implements List<E> {

    private final E defaultValue;
    private HashMap<Integer, E> sparseMap = new HashMap<>();
    private int size;

    private Integer[] sortedKeyArray() {
        Integer[] result = sparseMap.keySet().toArray(new Integer[0]);
        Arrays.sort(result);
        return result;
    }

    public SparseList(E defaultValue) {

        if (defaultValue==null) {
            throw new NullPointerException();
        }

        this.defaultValue = defaultValue;
    }

    public E getDefaultValue() {
        return defaultValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o==null) {
            throw new NullPointerException();
        }
        if (defaultValue.equals(o)) {
            return size > sparseMap.size(); // not all values are non-default
        }
        return sparseMap.containsValue(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new SparseListIterator<>(this, sparseMap);
    }

    /*
     * From Java spec:
     * Note that toArray(new Object[0]) is identical in function to toArray().
     */
    @Override
    public Object[] toArray() {
        return toArray(new Object[0]);
    }

    /*
     * From Java spec:
     * Returns an array containing all of the elements in this list in proper sequence (from first to last element);
     * the runtime type of the returned array is that of the specified array. If the list fits in the specified array,
     * it is returned therein. Otherwise, a new array is allocated with the runtime type of the specified array
     * and the size of this list.
     * If the list fits in the specified array with room to spare (i.e., the array has more elements than the list),
     * the element in the array immediately following the end of the list is set to null. (This is useful in
     * determining the length of the list only if the caller knows that the list does not contain any null elements.)
     */
    @Override
    public <T> T[] toArray(T[] a) {

        if (a==null) {
            throw new NullPointerException();
        }

        T[] result;

        if (a.length >= size) {
            result = a;
        } else {
            result = (T[]) new Object[size]; // Dlaczego nie mogę napisać new T[size]?
        }

        for (int i=0; i<size; i++)
        {
            result[i] = (T)defaultValue;
        }

        for (int i=size; i<result.length; i++) {
            result[i] = null;
        }

        for(int i: sparseMap.keySet()) {
            result[i] = (T)sparseMap.get(i);
        }
        return result;
    }

    @Override
    public boolean add(E e) {

        if (e == null) {
            throw new NullPointerException();
        }
        if (!defaultValue.equals(e)) {
            sparseMap.put(size, e);
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (defaultValue.equals(o)) {
            if (sparseMap.size() == size) { // no default values, nothing to remove
                return false;
            }

            int i = 0;
            while (sparseMap.containsKey(i)) { // find index of first default value (not in sparseMap)
                i++;
            }
            remove(i);
            return true;
        }

        int minIndexOf = size;
        for (int i: sparseMap.keySet()) {
            if (sparseMap.get(i).equals(o) && i < minIndexOf) {
                minIndexOf = i;
            }
        }

        if (minIndexOf == size) { // o not found
            return false;
        }
        remove(minIndexOf);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        if (c==null) {
            throw new NullPointerException();
        }
        for (Object o: c) {
            if (o == null) {
                throw new NullPointerException();
            }
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (E e: c) {
            if (e == null) {
                throw new NullPointerException();
            }
            add(e);
        }
        if (c.size() > 0) { //there were elements to be added
            return true;
        }

        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean result = false;
        for (Object o: c) {
            if (o == null) {
                throw new NullPointerException();
            }
            while (remove(o)) { // remove all occurences of 0, return true if anything removed
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c.contains(null)) {
            throw new NullPointerException();
        }
        Iterator<E> iterator = iterator();
        int oldSize = size;
        while(iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return size != oldSize;
    }

    @Override
    public void clear() {
        sparseMap.clear();
        size = 0;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (sparseMap.containsKey(index)) {
            return sparseMap.get(index);
        }
        return defaultValue;
    }

    @Override
    public E set(int index, E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (sparseMap.containsKey(index)) {

            if (defaultValue.equals(element)) {
                return sparseMap.remove(index);
            } else {
                return sparseMap.replace(index, element);
            }
        }
        if (!defaultValue.equals(element)) {
            sparseMap.put(index, element);
        }
        return defaultValue;
    }

    @Override
    public void add(int index, E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        Integer[] keyArray = sortedKeyArray();
        int shiftStart = Arrays.binarySearch(keyArray, index);
        if (shiftStart < 0) {
            shiftStart = -shiftStart;
        }
        for (int i=keyArray.length-1; i>= shiftStart; i--) {
            E e = sparseMap.remove(keyArray[i]);
            sparseMap.put(keyArray[i] + 1, e);
        }
        if(!element.equals(defaultValue)) {
            sparseMap.put(index, element);
        }
        size++;
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E result = sparseMap.remove(index);
        Integer[] keyArray = sortedKeyArray();
        int shiftStart = -(Arrays.binarySearch(keyArray, index) + 1);
        for (int i = shiftStart; i<keyArray.length; i++) {
            E value = sparseMap.remove(keyArray[i]);
            sparseMap.put(keyArray[i]-1, value);
        }
        size--;
        return result;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (defaultValue.equals(o)) {
            if (size == sparseMap.size()) { // the list does not contain default value
                return -1;
            }
            int i = 0;
            while (sparseMap.containsKey(i)) { // find index of first default value (not in sparseMap)
                i++;
            }
            return i;
        }
        Integer[] keyArray = sortedKeyArray();
        for (int i=0; i< keyArray.length; i++) {
            if (sparseMap.get(keyArray[i]).equals(o)) {
                return keyArray[i];
            }
        }
        return -1;

    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (defaultValue.equals(o)) {
            if (size == sparseMap.size()) { // the list does not contain default value
                return -1;
            }
            int i = size-1;
            while (sparseMap.containsKey(i)) { // find index of last default value (not in sparseMap)
                i--;
            }
            return i;
        }
        Integer[] keyArray = sortedKeyArray();
        for(int i=keyArray.length-1; i>=0; i--) {
            if (sparseMap.get(keyArray[i]).equals(o)) {
                return keyArray[i];
            }
        }
        return -1;

    }

    @Override
    public ListIterator<E> listIterator() {
        //TODO
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        //TODO
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        //TODO
        return null;
    }

    @Override
    public String toString() {
        String result = "[";
        if (size > 0) {
            result += get(0).toString();
        }
        for (int i=1; i< size; i++) {
            result += (", " + get(i).toString());
        }
        return result + "]";

    }

}
