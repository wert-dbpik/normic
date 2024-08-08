package ru.wert.normic.dataBaseEntities.db_connection.version;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;

import java.util.Arrays;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class VersionNormic extends _BaseEntity implements Item, Comparable<VersionNormic> {

    private String data;
    private String name;
    private String note;
    private String constants;

    @Override
    public String toUsefulString() {
        return name;
    }

    @Override
    public int compareTo(VersionNormic o) {
        String[] nn1 = getName().split("\\.", -1);
        System.out.println(Arrays.toString(nn1));
        String[] nn2 = o.getName().split("\\.", -1);
        System.out.println(Arrays.toString(nn2));
        for(int i = 0; i < nn1.length; i ++){
            int res = nn1[i].compareTo(nn2[i]);
            if(res != 0)
                return res;
        }
        return 0;
    }
}
