package kz.learn.shop.exception;

/**
 * Тапсырыстың осы күйінде бұл әрекет жасалмайды: "Cannot ship order in status NEW".
 * Хабарламаны шақырған жер құрастырады — бұл класс оны тек ары қарай береді.
 */
public class OrderStateException extends ShopException {

    public OrderStateException(String message) {
        super(message);
    }
}
