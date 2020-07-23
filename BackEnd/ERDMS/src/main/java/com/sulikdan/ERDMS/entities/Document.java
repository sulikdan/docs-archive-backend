package com.sulikdan.ERDMS.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Daniel Šulik on 18-Jul-20
 * <p>
 * Class Document is used for .....
 */
@Getter
@Setter
@Builder
public class Document {

    protected String id;
    protected List<Page> pageList;
    protected String nameOfFile;
    protected Byte [] documentFile;
}
