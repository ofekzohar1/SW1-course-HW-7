package il.ac.tau.cs.sw1.ex7;

import java.util.*;

public class FractionalKnapSack implements Greedy<FractionalKnapSack.Item> {
    int capacity;
    List<Item> lst;
    // Empty list and Item objects for empty lst case
    private static final Item emptyListItem = new Item(-1,-1);
    private static final List<Item> emptyList = Arrays.asList(emptyListItem);

    FractionalKnapSack(int c, List<Item> lst1) {
        capacity = c;
        lst = lst1;
    }

    public static class Item implements Comparable<Item> {
        double weight, value;

        Item(double w, double v) {
            weight = w;
            value = v;
        }

        @Override
        public String toString() {
            return "{" + "weight=" + weight + ", value=" + value + '}';
        }

        /**
         * Compare two Item objects - this object with the argument object.
         * @param o Other Item object to compare to
         * @return Positive num if this > o, negative if this < o and 0 if this == o
         */
        @Override
        public int compareTo(Item o) {
            return Double.compare(this.value / this.weight, o.value / o.weight);
        }
    }

    /**
     * Sort best selection list according to Item's compareTo method.
     * Time complexity: O(nlogn) (sort), auxiliary space: O(1)
     * @return Iterator for the best selection list
     */
    @Override
    public Iterator<Item> selection() {
        if (lst.size() == 0){
            return emptyList.iterator(); // Empty list iterator for empty lst
        }
        Collections.sort(lst, Collections.reverseOrder()); // Sort in descending order
        return lst.iterator();
    }

    /**
     * This method checks if there is any available space in the sack.
     * Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @param element Item object to check for the feasibility
     * @return True - if there is any available space, else false
     */
    @Override
    public boolean feasibility(List<Item> candidates_lst, Item element) {
        return sumWeight(candidates_lst) < capacity;
    }

    /**
     * This method assign new element to the candidates list.
     * Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @param element Item object to assign
     */
    @Override
    public void assign(List<Item> candidates_lst, Item element) {
        double availableCapacity = capacity - sumWeight(candidates_lst);
        if (element == emptyListItem || availableCapacity <= 0)
            return; // Empty lst case or There isn't available capacity
        if (element.weight <= availableCapacity)
            candidates_lst.add(element);
        else // Add element with the remain space
            candidates_lst.add(new Item(availableCapacity, element.value));
    }

    /**
     * This method checks if the given candidates list is a valid and optimal solution.
     * Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     * @param candidates_lst Optional solution list
     * @return True - if a valid and optimum solution, else false
     */
    @Override
    public boolean solution(List<Item> candidates_lst) {
        double sumWeight = sumWeight(candidates_lst);
        // Sack is full or all the items added
        if ((sumWeight < capacity && candidates_lst.size() == lst.size()) || sumWeight(candidates_lst) == capacity)
            return isValidSolution(candidates_lst); // Check valid solution
        return false;
    }

    /*
    Private method checks if the candidates list built from valid items.
    Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     */
    private boolean isValidSolution(List<Item> candidates_lst) {
        Iterator<Item> candidates_lst_iter = candidates_lst.iterator();
        Iterator<Item> lst_iter = selection();
        Item nextCandidate, nextLst;
        while (candidates_lst_iter.hasNext() && lst_iter.hasNext()) {
            nextCandidate = candidates_lst_iter.next();
            nextLst = lst_iter.next();
            // Checking for the right order and weight didn't exceed his maximum
            if (nextCandidate.value != nextLst.value || nextCandidate.weight > nextLst.weight)
                return false;
        }
        return true;
    }

    /*
    Sum the total weight in the candidates list
    Time complexity: O(n), auxiliary space: O(1) (candidates_lst.size() == n)
     */
    private double sumWeight(List<Item> candidates_lst) {
        double sum = 0;
        for (Item item : candidates_lst) {
            sum += item.weight;
        }
        return sum;
    }
}
