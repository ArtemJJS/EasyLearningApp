package by.anelkin.easylearning.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.Date;

/**
 * model CourseLesson entity
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseLesson extends AppEntity{
    private int id;
    private int chapterId;
    private String name;
    private String pathToContent;
    private Date creationDate;
    private long duration;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
