package com.sulikdan.ERDMS.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.List;

/**
 * <p>
 * Class Document is main object representing any file received from user.
 * Doesn't matter, if it's jpg, tiff, png, etc .. all of them will be stored as object Document.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode //TODO in future think about better approach, lombok might not be the best tool for this
public class Document {

    protected String id;
    protected List<Page> pageList;
    protected String nameOfFile;
    protected Path filePath;
    protected Byte [] documentFile;

    protected DocConfig docConfig;
}
