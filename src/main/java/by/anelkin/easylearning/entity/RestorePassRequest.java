package by.anelkin.easylearning.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * model RestorePassRequest entity
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RestorePassRequest extends AppEntity{
    @NonNull private int accId;
    @NonNull private String uuid;
}
