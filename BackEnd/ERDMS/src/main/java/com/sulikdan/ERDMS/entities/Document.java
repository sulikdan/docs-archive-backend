package com.sulikdan.ERDMS.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.nio.file.Path;
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
@EqualsAndHashCode // TODO in future think about better approach, lombok might not be the best tool
// for this
@org.springframework.data.mongodb.core.mapping.Document
public class Document {

  @Builder.Default
  private String id = new ObjectId().toString();

  @JsonProperty("pages")
  private List<Page> pageList;

  @JsonProperty("origName")
  private String nameOfFile;

  private Path filePath;
  private Byte[] documentFile;
  private byte[] documentAsBytes;
  private DocumentType documentType;

  @CreationTimestamp
  //  @Temporal(TemporalType.TIMESTAMP)
  //  @CreatedDate
  @Builder.Default
  private LocalDateTime createDateTime = LocalDateTime.now();

  @UpdateTimestamp
  //  @Temporal(TemporalType.TIMESTAMP)
  @Builder.Default
  private LocalDateTime updateDateTime = LocalDateTime.now();

  private AsyncApiInfo asyncApiInfo;

  private DocConfig docConfig;

  public Document() {}

  public Document(
      String id,
      List<Page> pageList,
      String nameOfFile,
      Path filePath,
      Byte[] documentFile,
      DocumentType documentType,
      AsyncApiInfo asyncApiInfo,
      DocConfig docConfig) {
    this.id = id;
    this.pageList = pageList;
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;
    this.documentType = documentType;
    this.asyncApiInfo = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Document(String nameOfFile, Path filePath, Byte[] documentFile, DocConfig docConfig) {
    this.id = new ObjectId().toString();
    this.pageList = new ArrayList<>();
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;
    this.documentAsBytes = ArrayUtils.toPrimitive(documentFile);

    this.docConfig = docConfig;

    this.asyncApiInfo = new AsyncApiInfo();
  }

  public Document(
      String nameOfFile,
      Path filePath,
      Byte[] documentFile,
      byte[] documentAsBytes,
      DocumentType documentType,
      AsyncApiInfo asyncApiInfo,
      DocConfig docConfig) {
    this.id = new ObjectId().toString();
    this.pageList = new ArrayList<>();
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;
    this.documentAsBytes = documentAsBytes;
    this.documentType = documentType;
    this.asyncApiInfo = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Document(
      String id,
      List<Page> pageList,
      String nameOfFile,
      Path filePath,
      Byte[] documentFile,
      byte[] documentAsBytes,
      DocumentType documentType,
      AsyncApiInfo asyncApiInfo,
      DocConfig docConfig) {
    this.id = id;
    this.pageList = pageList;
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;
    this.documentAsBytes = documentAsBytes;
    this.documentType = documentType;
    this.asyncApiInfo = asyncApiInfo;
    this.docConfig = docConfig;
  }

  public Document(
      String id,
      List<Page> pageList,
      String nameOfFile,
      Path filePath,
      Byte[] documentFile,
      byte[] documentAsBytes,
      DocumentType documentType,
      LocalDateTime createDateTime,
      LocalDateTime updateDateTime,
      AsyncApiInfo asyncApiInfo,
      DocConfig docConfig) {
    this.id = id != null ? id : new ObjectId().toString();
    this.pageList = pageList;
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;
    this.documentAsBytes = documentAsBytes;
    this.documentType = documentType != null ? documentType : getFileDocumentType(nameOfFile);
    this.asyncApiInfo = asyncApiInfo != null ? asyncApiInfo : new AsyncApiInfo();
    this.docConfig = docConfig;
    this.createDateTime = createDateTime; //createDateTime != null ? createDateTime : LocalDateTime.now();
    this.updateDateTime = updateDateTime; //updateDateTime != null ? updateDateTime : LocalDateTime.now();
  }

// TODO this should be called elsewhere...
  private DocumentType getFileDocumentType(String nameOfFile){
    if( nameOfFile.toLowerCase().endsWith(".pdf")){
      return DocumentType.PDF;
    }
    else {
      return DocumentType.IMG;
    }
  }

}
