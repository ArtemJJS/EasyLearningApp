package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CourseChapter extends AppEntity{
    private int id;
    private int courseId;
    private String name;
}
