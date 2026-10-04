package kz.learn.library.model;

/**
 * id-сы бар кез келген нәрсе. ДАЙЫН.
 *
 * ID — типтік параметр: Item мен Loan үшін Long, Member үшін String (оқырман билетінің нөмірі).
 * Осының арқасында бір ғана InMemoryRepository үш түрлі сущностьқа жарайды.
 */
public interface Identifiable<ID> {

    ID getId();
}
