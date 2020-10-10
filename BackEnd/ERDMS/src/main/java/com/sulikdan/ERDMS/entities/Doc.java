package com.sulikdan.ERDMS.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Document is main object representing any file received from user. Doesn't matter, if it's
 * jpg, tiff, png, etc .. all of them will be stored as object Document.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode // TODO in future think about better approach, lombok might not be the best tool
@QueryEntity
@Document(collection = "documents")
public class Doc {

  @Id
  @Builder.Default
  private String id = new ObjectId().toString();

  @JsonProperty("pages")
  private List<DocPage> docPageList;

  @JsonProperty("origName")
  private String nameOfFile;

  @JsonIgnore
  private byte[] documentAsBytes;


  @JsonProperty("documentPreview")
  private byte[] documentPreview;

  private DocType docType;

  @CreationTimestamp
  @Builder.Default
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern="dd/MM/yyyy HH:mm")
  private LocalDateTime createDateTime = LocalDateTime.now();

  @UpdateTimestamp
  @Builder.Default
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern="dd/MM/yyyy HH:mm")
  private LocalDateTime updateDateTime = LocalDateTime.now();

  private AsyncApiInfo asyncApiInfo;

  private DocConfig docConfig;

  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @Builder.Default
  private Boolean isShared = false;

  @DBRef(lazy = true)
  private User owner;

  public Doc() {}

  public Doc(
          String id, List<DocPage> docPageList, String nameOfFile, DocType docType, AsyncApiInfo asyncApiInfo, DocConfig docConfig) {
    this.id          = id;
    this.docPageList = docPageList;
    this.nameOfFile  = nameOfFile;
    this.docType      = docType;
    this.asyncApiInfo = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Doc(String nameOfFile, Byte[] documentFile, DocConfig docConfig) {
    this.id          = new ObjectId().toString();
    this.docPageList = new ArrayList<>();
    this.nameOfFile  = nameOfFile;
    this.documentAsBytes = ArrayUtils.toPrimitive(documentFile);

    this.docConfig = docConfig;

    this.asyncApiInfo = new AsyncApiInfo();
  }

  public Doc(
          String nameOfFile, byte[] documentAsBytes, DocType docType, AsyncApiInfo asyncApiInfo, DocConfig docConfig) {
    this.id          = new ObjectId().toString();
    this.docPageList = new ArrayList<>();
    this.nameOfFile  = nameOfFile;
    this.documentAsBytes = documentAsBytes;
    this.docType         = docType;
    this.asyncApiInfo    = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Doc(
          String id, List<DocPage> docPageList, String nameOfFile, byte[] documentAsBytes, DocType docType, AsyncApiInfo asyncApiInfo, DocConfig docConfig) {
    this.id          = id;
    this.docPageList = docPageList;
    this.nameOfFile  = nameOfFile;
    this.documentAsBytes = documentAsBytes;
    this.docType         = docType;
    this.asyncApiInfo    = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Doc(
          String id, List<DocPage> docPageList, String nameOfFile, byte[] documentAsBytes,byte[] documentPreview,
          DocType docType, LocalDateTime createDateTime, LocalDateTime updateDateTime,
          AsyncApiInfo asyncApiInfo, DocConfig docConfig,
          List<Tag> tags, Boolean isShared, User user) {
    this.id          = id != null ? id : new ObjectId().toString();
    this.docPageList = docPageList;
    this.nameOfFile  = nameOfFile;
    this.documentAsBytes = documentAsBytes;
    this.documentPreview = documentPreview;
    this.docType         = docType != null ? docType : getFileDocumentType(nameOfFile);
    this.asyncApiInfo    = asyncApiInfo != null ? asyncApiInfo : new AsyncApiInfo();
    this.docConfig = docConfig;
    this.createDateTime = createDateTime; //createDateTime != null ? createDateTime : LocalDateTime.now();
    this.updateDateTime = updateDateTime; //updateDateTime != null ? updateDateTime : LocalDateTime.now();
    this.tags = tags;
    this.isShared = isShared;
    this.owner = null;
  }



  // TODO this should be called elsewhere...
  private DocType getFileDocumentType(String nameOfFile){
    if( nameOfFile != null && nameOfFile.toLowerCase().endsWith(".pdf")){
      return DocType.PDF;
    }
    else {
      return DocType.IMG;
    }
  }

}
