package ru.wert.normic.entities.db_connection.UserGroup;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.entities.db_connection.Item;
import ru.wert.normic.entities.db_connection._BaseEntity;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class UserGroup extends _BaseEntity implements Item {

    private String name;

    private boolean administrate;
    private boolean editUsers;
    //----------------------
    private boolean readDrafts;
    private boolean editDrafts;
    private boolean commentDrafts;
    private boolean deleteDrafts;
    //------------------------
    private boolean readProductStructures;
    private boolean editProductStructures;
    private boolean deleteProductStructures;
    //------------------------
    private boolean readMaterials;
    private boolean editMaterials;
    private boolean deleteMaterials;
    //--------------------------
    private boolean editNormConstants;
    private boolean editNormExtraOperations;


    @Override
    public String toUsefulString() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
