// Collections Overview in Java: Queue
// Generally follows FIFO (First In, First Out).
// Elements are processed in order.

import java.util.LinkedList;
import java.util.Queue;

public class QueueExample {
    public static void main(String[] args) {
        Queue<String> waiting =
            new LinkedList<>();
        waiting.add("Customer1");
        waiting.add("Customer2");
        System.out.println("Waiting Queue: " + waiting);
    }
}
