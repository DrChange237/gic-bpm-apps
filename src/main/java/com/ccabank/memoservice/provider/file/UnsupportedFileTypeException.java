package com.ccabank.memoservice.provider.file;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file
 * <p>
 * @date: 19/06/2023
 * @time: 09:57
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class UnsupportedFileTypeException extends Exception {

    // `private static final long serialVersionUID = 1L;` is a unique identifier for the
    // `UnsupportedFileTypeException` class. It is used during serialization and deserialization of
    // objects to ensure that the same class is being used on both ends. If the `serialVersionUID` is not
    // specified, the JVM will generate one based on the class definition, which can cause issues if the
    // class definition changes between serialization and deserialization. By specifying a
    // `serialVersionUID`, we can ensure that the class definition remains consistent even if changes are
    // made to the class.
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur a
     *
     * @param message
     */
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
