package ch03.t10_queue_deque;

import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * Тапсырма 10 — Queue, Deque, PriorityQueue.
 * Кітап: "Интерфейс Queue<E>" (128 б.)
 *
 * Queue-да ӘР ӘРЕКЕТТІҢ ЕКІ НҰСҚАСЫ бар — біреуі exception лақтырады, біреуі жоқ:
 *
 *              exception лақтырады   арнайы мән қайтарады
 *   қосу       add(e)                offer(e)   -> false
 *   алу        remove()              poll()     -> null
 *   қарау      element()             peek()     -> null
 *
 * Бос кезекте remove() шақырсаң NoSuchElementException аласың, ал poll() — null.
 * Әдетте poll/peek/offer қолданылады.
 *
 *   ArrayDeque      әрі кезек (FIFO), әрі стек (LIFO). Ең жылдам әмбебап нұсқа.
 *   PriorityQueue   ЕҢ КІШІСІ бірінші шығады (табиғи рет немесе Comparator).
 *                   Назар аудар: toString() сұрыпталған КӨРІНБЕЙДІ — тек poll() реті дұрыс.
 *   Deque стек ретінде: push() / pop() / peek(). Ескі Stack класын қолданба.
 */
public class QueueLab {

    /** FIFO кезек жасайды (бірінші кірген бірінші шығады). Кеңес: ArrayDeque. */
    public static Queue<String> fifo(String... items) {
        return null; // TODO
    }

    /** Кезекті түбіне дейін босатып, шығу ретімен тізім қайтарады. */
    public static List<String> drain(Queue<String> queue) {
        return null; // TODO
    }

    /** Сандарды PriorityQueue арқылы өткізіп, шығу ретімен қайтарады (кішісінен). */
    public static List<Integer> prioritized(int... numbers) {
        return null; // TODO
    }

    /**
     * Ең үлкен n санды, кемуі бойынша.
     * Кеңес: PriorityQueue-ге Comparator.reverseOrder() беруге болады.
     */
    public static List<Integer> topN(int n, int... numbers) {
        return null; // TODO
    }

    /** Жолды Deque-ті СТЕК ретінде қолданып аударады: "abc" -> "cba". */
    public static String reverse(String text) {
        return null; // TODO
    }

    /**
     * Жақшалар дұрыс жабылған ба: "(a[b]{c})" -> true, "(]" -> false.
     * Үш түрі: ( ) [ ] { }. Басқа таңбаларды елемеу керек.
     * Бұл — Deque-тің классикалық қолданысы (әрі жиі сұхбат сұрағы).
     */
    public static boolean isBalanced(String text) {
        return false; // TODO
    }

    /**
     * Deque-ті екі жағынан толтырады:
     * front элементтері берілген ретпен addFirst() арқылы (сондықтан кері шығады),
     * back элементтері addLast() арқылы.
     * Мысал: front=[1,2], back=[8,9] -> [2, 1, 8, 9].
     */
    public static Deque<Integer> buildDeque(List<Integer> front, List<Integer> back) {
        return null; // TODO
    }
}
