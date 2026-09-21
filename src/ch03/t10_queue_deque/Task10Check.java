package ch03.t10_queue_deque;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Task10Check {

    public static void run() {
        Check.task("Тапсырма 10 — Queue, Deque, PriorityQueue (QueueLab)");

        Queue<String> queue = QueueLab.fifo("a", "b", "c");
        Check.notNull("fifo() null емес", queue);
        if (queue != null) {
            Check.eq("peek() — бірінші кірген", "a", queue.peek());
            Check.eq("peek() кезекті өзгертпейді", 3, queue.size());
            Check.eq("drain() — FIFO реті", List.of("a", "b", "c"), QueueLab.drain(queue));
            Check.eq("drain() кезекті босатады", 0, queue.size());
            Check.eq("бос кезекте poll() -> null", null, queue.poll());
            Check.throwsEx("бос кезекте remove() -> NoSuchElementException",
                    NoSuchElementException.class, queue::remove);
        }

        Check.eq("drain(бос кезек)", List.of(), QueueLab.drain(QueueLab.fifo()));

        Check.eq("prioritized() — кішісінен", List.of(1, 3, 5, 9), QueueLab.prioritized(5, 1, 9, 3));
        Check.eq("prioritized() қайталанғанды да сақтайды", List.of(2, 2, 7),
                QueueLab.prioritized(7, 2, 2));
        Check.eq("prioritized(бос)", List.of(), QueueLab.prioritized());

        Check.eq("topN(2)", List.of(9, 5), QueueLab.topN(2, 5, 1, 9, 3));
        Check.eq("topN(10) — барлығынан көп сұрасаң, бәрін береді", List.of(9, 5, 3, 1),
                QueueLab.topN(10, 5, 1, 9, 3));
        Check.eq("topN(0)", List.of(), QueueLab.topN(0, 5, 1));

        Check.eq("reverse(\"abc\")", "cba", QueueLab.reverse("abc"));
        Check.eq("reverse(\"\")", "", QueueLab.reverse(""));
        Check.eq("reverse(\"Salem\")", "melaS", QueueLab.reverse("Salem"));

        Check.isTrue("isBalanced(\"(a[b]{c})\")", QueueLab.isBalanced("(a[b]{c})"));
        Check.isTrue("isBalanced(\"\")", QueueLab.isBalanced(""));
        Check.isTrue("isBalanced(\"жақшасыз мәтін\")", QueueLab.isBalanced("жақшасыз мәтін"));
        Check.isFalse("isBalanced(\"(]\")", QueueLab.isBalanced("(]"));
        Check.isFalse("isBalanced(\"(\") — жабылмаған", QueueLab.isBalanced("("));
        Check.isFalse("isBalanced(\")\") — ашылмаған", QueueLab.isBalanced(")"));
        Check.isFalse("isBalanced(\"([)]\") — қиылысқан", QueueLab.isBalanced("([)]"));

        Deque<Integer> deque = QueueLab.buildDeque(List.of(1, 2), List.of(8, 9));
        Check.eq("buildDeque(front=[1,2], back=[8,9])", List.of(2, 1, 8, 9),
                deque == null ? null : new ArrayList<>(deque));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
