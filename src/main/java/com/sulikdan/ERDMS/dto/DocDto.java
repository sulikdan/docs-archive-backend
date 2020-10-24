package com.sulikdan.ERDMS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.sulikdan.ERDMS.entities.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Daniel Šulik on 19-Sep-20
 * <p>
 * Class DocDTO is used for .....
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocDto extends RepresentationModel<DocDto> {

    @Builder.Default
    private String id = new ObjectId().toString();

    @JsonProperty("pages")
    private List<DocPage> docPageList;

    @JsonProperty("origName")
    private String nameOfFile;

    @JsonIgnore
    private byte[] documentAsBytes;

    private String documentPreview;

    private DocType docType;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime createDateTime ;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime updateDateTime;

    private AsyncApiState docState;

    private DocConfig docConfig;

    private List<Tag> tags;

    private Boolean isShared;
}
