package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Mark extends AppEntity {
    private int id;
    private int targetId;
    private int accId;
    private int markValue;
    private String comment;
    private Date markDate;
    @NonNull private MarkType markType;

    public enum MarkType{
        AUTHOR_MARK,
        COURSE_MARK
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
