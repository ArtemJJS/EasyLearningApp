package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class RestorePassRequest extends AppEntity{
    @NonNull private int accId;
    @NonNull private String uuid;
}
