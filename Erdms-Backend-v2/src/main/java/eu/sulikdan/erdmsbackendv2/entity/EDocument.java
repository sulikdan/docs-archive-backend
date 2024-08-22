package eu.sulikdan.erdmsbackendv2.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class Document is main object representing any file received from user. Doesn't matter, if it's
 * jpg, tiff, png, etc .. all of them will be stored as object Document.
 *
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "e_document")
public class EDocument {

    @Id
    private final UUID id = UUID.randomUUID();

    @OneToMany
    @JoinColumn(name = "e_document_id")
    private List<EDocPage> pages;

    private String eDocName;

    @Enumerated(EnumType.STRING)
    private EDocType docType;

    private LocalDateTime createDateTime = LocalDateTime.now();

    private LocalDateTime updateDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "async_api_info_id")
    private AsyncApiInfo asyncApiInfo;

    @OneToOne
    @JoinColumn(name = "e_doc_config_id")
    private EDocConfig eDocConfig;

    @ManyToMany
    @JoinTable(name = "e_document_tag",
            joinColumns = @JoinColumn(name = "e_document_id"),
            inverseJoinColumns = @JoinColumn(name = "e_tag_id")
    )
    private List<ETag> ETags = new ArrayList<>();

    private Boolean isShared = false;

    @ManyToOne
    @JoinColumn(name = "e_user_id")
    private User owner;

}
