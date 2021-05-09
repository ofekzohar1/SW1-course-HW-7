package il.ac.tau.cs.sw1.ex7;
import java.util.*;

public class FractionalKnapSack implements Greedy<FractionalKnapSack.Item>{
    int capacity;
    List<Item> lst;

    FractionalKnapSack(int c, List<Item> lst1){
        capacity = c;
        lst = lst1;
    }

    public static class Item {
        double weight, value;
        Item(double w, double v) {
            weight = w;
            value = v;
        }

        @Override
        public String toString() {
            return "{" + "weight=" + weight + ", value=" + value + '}';
        }
    }

    @Override
    public Iterator<Item> selection() {
        return false;
    }

    @Override
    public boolean feasibility(List<Item> candidates_lst, Item element) {
        return false;
    }

    @Override
    public void assign(List<Item> candidates_lst, Item element) {
        return false;
    }

    @Override
    public boolean solution(List<Item> candidates_lst) {
        return false;
    }
}
