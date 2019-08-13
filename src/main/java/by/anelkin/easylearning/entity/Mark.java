package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Date;

/**
 * model Mark entity
 * contains {@link MarkType} enum
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Mark extends AppEntity {
    private int id;
    private int targetId;
    private int accId;
    private String accPathToPhoto;
    private String accLogin;
    private int markValue;
    private String comment;
    private long markDate;
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
