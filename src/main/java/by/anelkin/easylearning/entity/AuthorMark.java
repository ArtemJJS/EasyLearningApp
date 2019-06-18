package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthorMark extends AppEntity {
    private int id;
    private int authorId;
    private int userId;
    private int markValue;
    private String comment;
    private Date markDate;
}
