package client;

/**
 * An auxiliary class for the client.
 * @author Peng Jiang
 * @version 2020-03-17
 */
public abstract class ClientAux {
    /**
     * Simplify the println method
     * @param content the content to be printed
     */
    public static void println(String content) {
        System.out.println(content);
    }

    /**
     * Simplify the print method
     * @param content the content to be printed
     */
    public static void print(String content) {
        System.out.print(content);
    }
}
