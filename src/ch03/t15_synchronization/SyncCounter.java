package ch03.t15_synchronization;

/**
 * Тапсырма 15 — synchronized арқылы қорғалған санағыш.
 * Кітап: "Синхронизация и потокобезопасность", "Синхронизированные методы и блоки" (139–140 б.)
 *
 * synchronized метод объектінің ҚҰЛПЫН (монитор) алады: бір мезетте ішке
 * бір ғана ағын кіреді, қалғандары кезекте (BLOCKED) тұрады.
 *
 * increment() — synchronized МЕТОД ретінде жаз.
 * get() — synchronized БЛОК ретінде жаз: synchronized (this) { ... }
 * Екі жазылу түрін де көріп ал; get()-ті де қорғау керек, әйтпесе басқа ағын
 * жаңартқан мәнді көрмей қалуы мүмкін (видимость / visibility мәселесі).
 */
public class SyncCounter implements Counter {

    // TODO: private int count;
    private int count = 0;

    @Override
    public synchronized void increment() {
        // TODO: synchronized МЕТОД қыл
        count++;
    }

    @Override
    public int get() {
        synchronized (this) {
            return count;
        }
    }
}
