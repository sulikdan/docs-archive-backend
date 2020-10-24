package com.sulikdan.ERDMS.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by Daniel Šulik on 06-Sep-20
 * <p>
 * Class Tag is used for .....
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    String tagType;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Tag tag = (Tag) o;

        return Objects.equals(tagType, tag.tagType);
    }

    @Override
    public int hashCode() {
        return tagType != null ? tagType.hashCode() : 0;
    }
}
