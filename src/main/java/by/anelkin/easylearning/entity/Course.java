package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, exclude = "picture")
public class Course extends AppEntity {
    private int id;
    private String name;
    private String description;
    private Date creationDate;
    private File picture;
}
