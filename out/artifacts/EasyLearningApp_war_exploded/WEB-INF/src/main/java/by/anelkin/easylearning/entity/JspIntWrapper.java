package by.anelkin.easylearning.entity;

import lombok.Getter;
import lombok.Setter;

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
