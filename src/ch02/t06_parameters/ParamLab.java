package ch02.t06_parameters;

/**
 * Тапсырма 06 — Java-да параметрлер қалай беріледі.
 * Кітап: "Передача параметров в Java" (85-б.)
 *
 * АЛДЫМЕН ОЙЛА, СОСЫН ЖАЗ: әр методтың жанындағы сұраққа өз болжамыңды
 * комментарий етіп жаз, содан кейін ғана кодты жаз да, тексерісті жүгірт.
 *
 * Java-да БАРЛЫҚ нәрсе мәні (by value) бойынша беріледі.
 * Объект үшін берілетін "мән" — сілтеменің көшірмесі.
 */
public class ParamLab {

    /**
     * Параметрді 1-ге өсіреді.
     * Болжам: шақырған жақтағы айнымалы өзгере ме? ____
     */
    public static void increment(int value) {
        // TODO: value++ жаз
    }

    /**
     * Counter объектісінің ІШІНДЕГІ мәнді 1-ге өсіреді.
     * Болжам: шақырған жақтағы объект өзгере ме? ____
     */
    public static void incrementInside(Counter counter) {
        // TODO: counter.setValue(counter.getValue() + 1)
    }

    /**
     * Параметрге МҮЛДЕМ ЖАҢА объект меншіктейді.
     * Болжам: шақырған жақтағы айнымалы жаңа объектіге көрсете ме? ____
     */
    public static void reassign(Counter counter) {
        // TODO: counter = new Counter(999)
    }

    /**
     * Массивтің әр элементін 2-ге көбейтеді.
     * Болжам: шақырған жақтағы массив өзгере ме? ____
     */
    public static void doubleAll(int[] numbers) {
        // TODO
    }

    /**
     * Параметрге жаңа массив меншіктейді.
     * Болжам: шақырған жақтағы массив ауыса ма? ____
     */
    public static void replaceArray(int[] numbers) {
        // TODO: numbers = new int[]{7, 7, 7}
    }

    /**
     * Массивтің ӨЗГЕРТІЛГЕН КӨШІРМЕСІН қайтарады, түпнұсқаға тимейді.
     * (Мутация орнына таза функция жазудың дұрыс жолы.)
     */
    public static int[] doubledCopy(int[] numbers) {
        // TODO
        return null;
    }
}
