package by.anelkin.easylearning.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Class-wrapper to implements increment option on jsp pages
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Getter
@Setter
public class JspIntWrapper {
    private int value;

    public JspIntWrapper() {
        value = 1;
    }

    public int receiveAndIncrement() {
        return value++;
    }

    public void increment() {
        value++;
    }

    public void resetToOne() {
        value = 1;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
