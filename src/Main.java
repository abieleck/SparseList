import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Create empty list with default value 0");
        SparseList<Integer> sparseList = new SparseList<>(0);
        System.out.println(sparseList);

        System.out.println("Adding elements 0,2,4");
        sparseList.add(0);
        sparseList.add(2);
        sparseList.add(4);
        System.out.println(sparseList);

        System.out.println("inserting elements 1 and 3 retaining order");
        sparseList.add(1,1);
        sparseList.add(3,3);
        System.out.println(sparseList);

        System.out.println("Inserting 0 at index 2");
        sparseList.add(2,0);
        System.out.println(sparseList);

        System.out.println("Removing value of 2");
        sparseList.remove(new Integer(0));
        System.out.println(sparseList);

        System.out.println("Removing value of 3");
        sparseList.remove(new Integer(3));
        System.out.println(sparseList);

        System.out.println("Removing value at index 1");
        sparseList.remove(1);
        System.out.println(sparseList);

        System.out.println("Removing value at index 2");
        sparseList.remove(2);
        System.out.println(sparseList);

        System.out.println("Check if list contains 0");
        System.out.println(sparseList.contains(0));

        System.out.println("Check if list contains 3");
        System.out.println(sparseList.contains(3));

        System.out.println("Check if list contains -1");
        System.out.println(sparseList.contains(-1));

        System.out.println("Check if list contains all elements from collection with 0,2,3");
        System.out.println(sparseList.containsAll(Arrays.asList(new Integer[]{0,2,3})));

        System.out.println("Check if list contains all elements from empty collection");
        System.out.println(sparseList.containsAll(new ArrayList<Integer>()));

        System.out.println("Check if list contains all elements from collection with 0,2,4");
        System.out.println(sparseList.containsAll(Arrays.asList(new Integer[]{0,2,4})));

        System.out.println("Create array from list");
        Object[] array = sparseList.toArray();
        System.out.print("[" + array[0]);
        for (int i=1; i<array.length; i++) {
            System.out.print(", " + array[i]);
        }
        System.out.println("]");

        System.out.println("Removing elements 0,1,3 contained in collection");
        sparseList.removeAll(Arrays.asList(new Integer[]{0,1,3}));
        System.out.println(sparseList);

        System.out.println("Clearing the list");
        sparseList.clear();
        System.out.println(sparseList);

        System.out.println("Adding 0,1,0,2,0,3 from collection");
        sparseList.addAll(Arrays.asList(new Integer[]{0,1,0,2,0,3}));
        System.out.println(sparseList);

        System.out.println("Removing all elements except 0, 2");
        sparseList.retainAll(Arrays.asList(new Integer[]{0,2}));
        System.out.println(sparseList);

        System.out.println("Get value at index 1");
        System.out.println(sparseList.get(1));

        System.out.println("Get value at index 2");
        System.out.println(sparseList.get(2));

        System.out.println("First index of 2");
        System.out.println(sparseList.indexOf(2));

        System.out.println("Last index of 2");
        System.out.println(sparseList.lastIndexOf(2));

        System.out.println("Last index of 0");
        System.out.println(sparseList.lastIndexOf(0));

        System.out.println("Set value at index 1 to 1");
        sparseList.set(1,1);
        System.out.println(sparseList);

        System.out.println("Going through the list using iterator");
        Iterator iterator = sparseList.iterator();
        System.out.print(iterator.next());
        while (iterator.hasNext()) {
            System.out.print(", " + iterator.next());
        }
        System.out.println();

        System.out.println("Removing value of 2 using iterator");
        iterator = sparseList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(2)) {
                iterator.remove();
            }
        }
        System.out.println(sparseList);
    }
}
