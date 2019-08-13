package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * model CourseChapter entity
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseChapter extends AppEntity{
    private int id;
    private int courseId;
    private String name;
    private int lessonAmount;
    private long duration;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
