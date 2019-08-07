package by.anelkin.easylearning.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// FIXME: 8/6/2019 ПЕРЕНЕСТИ В УТИЛ????
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
