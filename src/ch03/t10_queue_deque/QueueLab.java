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
        Queue<String> queue = new ArrayDeque<>();
        for (String item : items) {
            queue.add(item);
        }
        return queue;
    }

    /** Кезекті түбіне дейін босатып, шығу ретімен тізім қайтарады. */
    public static List<String> drain(Queue<String> queue) {
        List<String> result = new ArrayList<>();
        // Кезек бос болғанша, poll() арқылы элементтерді кезекпен алып тізімге қосамыз
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }

    /** Сандарды PriorityQueue арқылы өткізіп, шығу ретімен қайтарады (кішісінен). */
    public static List<Integer> prioritized(int... numbers) {
        // PriorityQueue әдепкі бойынша элементтерді өсу ретімен (кішісінен үлкеніне) реттейді
        Queue<Integer> pq = new PriorityQueue<>();
        for (int num : numbers) {
            pq.offer(num);
        }

        List<Integer> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        return result;
    }

    /**
     * Ең үлкен n санды, кемуі бойынша.
     * Кеңес: PriorityQueue-ге Comparator.reverseOrder() беруге болады.
     */
    public static List<Integer> topN(int n, int... numbers) {
        // Егер сұралып тұрған n нөл немесе одан кіші болса, бос тізім қайтарамыз
        if (n <= 0) return new ArrayList<>();

        // Comparator.reverseOrder() арқылы ең үлкен элемент бірінші шығатын PriorityQueue жасаймыз
        Queue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int num : numbers) {
            pq.offer(num);
        }

        List<Integer> result = new ArrayList<>();
        // Кезек бос болғанша немесе n элемент жинағанша poll() жасаймыз
        while (!pq.isEmpty() && result.size() < n) {
            result.add(pq.poll());
        }
        return result;
    }

    /** Жолды Deque-ті СТЕК ретінде қолданып аударады: "abc" -> "cba". */
    public static String reverse(String text) {
        if (text == null) return null;

        // Deque-ті Стек (LIFO) ретінде қолданамыз
        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : text.toCharArray()) {
            stack.push(ch); // элементті басына қосады
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop()); // соңғы кірген элемент бірінші шығады
        }
        return sb.toString();
    }

    /**
     * Жақшалар дұрыс жабылған ба: "(a[b]{c})" -> true, "(]" -> false.
     * Үш түрі: ( ) [ ] { }. Басқа таңбаларды елемеу керек.
     * Бұл — Deque-тің классикалық қолданысы (әрі жиі сұхбат сұрағы).
     */
    public static boolean isBalanced(String text) {
        if (text == null) return true;

        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : text.toCharArray()) {
            // Ашылатын жақша болса стекке саламыз
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            }
            // Жабылатын жақша болса тексереміз
            else if (ch == ')' || ch == ']' || ch == '}') {
                // Жабылатын жақша бар, бірақ стек бос болса - қате
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                // Жақша түрі сәйкес келмесе - қате
                if ((ch == ')' && open != '(') ||
                        (ch == ']' && open != '[') ||
                        (ch == '}' && open != '{')) {
                    return false;
                }
            }
        }
        // Егер стекте ашық жақша қалып қоймаса, онда бәрі дұрыс жабылған
        return stack.isEmpty();
    }

    /**
     * Deque-ті екі жағынан толтырады:
     * front элементтері берілген ретпен addFirst() арқылы (сондықтан кері шығады),
     * back элементтері addLast() арқылы.
     * Мысал: front=[1,2], back=[8,9] -> [2, 1, 8, 9].
     */
    public static Deque<Integer> buildDeque(List<Integer> front, List<Integer> back) {
        Deque<Integer> deque = new ArrayDeque<>();

        // front элементтерін addFirst арқылы қосамыз (кері ретпен орналасады)
        if (front != null) {
            for (Integer num : front) {
                deque.addFirst(num);
            }
        }

        // back элементтерін addLast арқылы соңына қосамыз
        if (back != null) {
            for (Integer num : back) {
                deque.addLast(num);
            }
        }

        return deque;
    }
}
