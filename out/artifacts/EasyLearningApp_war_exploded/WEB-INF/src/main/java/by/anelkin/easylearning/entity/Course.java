package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, exclude = "pathToPicture")
public class Course extends AppEntity {
    private int id;
    private int authorId;
    private String name;
    private String description;
    private Date creationDate;
    private String pathToPicture;
    private BigDecimal price;
    private double avgMark;
    private int lessonAmount;
    private long duration;
    private CourseState state; // 1 - approved, 0 - not approved, -1 - freeze

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
