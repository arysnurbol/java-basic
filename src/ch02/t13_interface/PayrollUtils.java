package ch02.t13_interface;


/**
 * Интерфейс арқылы полиморфизм: бұл метод FullTimeEmployee пе, Contractor па —
 * білмейді және білуі де қажет емес.
 */
public class PayrollUtils {

    /** Барлығының таза жалақысының қосындысы. null болса — 0. */
    public static double totalNetPay(Payable[] people) {

        if (people == null || people.length == 0) {
            return 0;
        }
        double total = 0;
        for (int i = 0; i < people.length; i++) {
            if (people[i] != null) {
                total += (people[i].netPay());
            }
        }

        return total;
    }
}
