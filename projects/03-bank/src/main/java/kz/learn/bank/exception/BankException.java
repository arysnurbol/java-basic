package kz.learn.bank.exception;

/**
 * Банктің барлық бизнес-қателерінің ата-класы (02-library-дағы LibraryException сияқты).
 * Unchecked — RuntimeException-нан мұраланады.
 */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super("TODO"); // TODO: хабарламаны ата-класқа бер
    }
}
