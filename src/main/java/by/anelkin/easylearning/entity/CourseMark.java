package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CourseMark extends AppEntity{
    private int id;
    private int courseId;
    private int userId;
    private int markValue;
    private String comment;
}
