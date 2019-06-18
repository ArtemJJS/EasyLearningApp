package by.anelkin.easylearning.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CourseLesson extends AppEntity{
    private int id;
    private int chapterId;
    private String name;
    private File content;
    private Date creationDate;
    private long length;
}
