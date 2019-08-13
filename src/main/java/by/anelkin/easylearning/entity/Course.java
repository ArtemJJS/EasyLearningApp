package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * model Course entity
 * contains{@link CourseState} enum
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"pathToPicture", "avgMark", "state", "updatePhotoPath"})
public class Course extends AppEntity {
    private int id;
    private int authorId;
    private String name;
    private String description;
    private Date creationDate;
    private String pathToPicture;
    private String updatePhotoPath;
    private BigDecimal price;
    private double avgMark;
    private int lessonAmount;
    private long duration;
    private CourseState state;

    public enum CourseState{
        FREEZING,
        NOT_APPROVED,
        APPROVED
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
