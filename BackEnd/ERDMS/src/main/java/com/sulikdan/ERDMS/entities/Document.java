package com.sulikdan.ERDMS.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.nio.file.Path;
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
public class Document {

  private String id;
  @JsonProperty("pages")
  private List<Page> pageList;
  @JsonProperty("origName")
  private String nameOfFile;
  private Path filePath;
  private Byte[] documentFile;
  private byte[] documentAsBytes;
  private DocumentType documentType;

  private AsyncApiInfo asyncApiInfo;

  private DocConfig docConfig;

  public Document() {
  }

  public Document(
          String id, List<Page> pageList, String nameOfFile, Path filePath, Byte[] documentFile,
          DocumentType documentType, AsyncApiInfo asyncApiInfo, DocConfig docConfig) {
    this.id           = id;
    this.pageList     = pageList;
    this.nameOfFile   = nameOfFile;
    this.filePath     = filePath;
    this.documentFile = documentFile;
    this.documentType = documentType;
    this.asyncApiInfo = asyncApiInfo;
    this.docConfig    = docConfig;
  }

  public Document(String nameOfFile, Path filePath, Byte[] documentFile, DocConfig docConfig) {
    this.id = new ObjectId().toString();
    this.pageList = new ArrayList<>();
    this.nameOfFile = nameOfFile;
    this.filePath = filePath;
    this.documentFile = documentFile;

    this.docConfig = docConfig;
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
    this.docConfig    = docConfig;
  }

  public Document(
          String id, List<Page> pageList, String nameOfFile, Path filePath, Byte[] documentFile, byte[] documentAsBytes,
          DocumentType documentType, AsyncApiInfo asyncApiInfo, DocConfig docConfig) {
    this.id              = id;
    this.pageList        = pageList;
    this.nameOfFile      = nameOfFile;
    this.filePath        = filePath;
    this.documentFile    = documentFile;
    this.documentAsBytes = documentAsBytes;
    this.documentType    = documentType;
    this.asyncApiInfo    = asyncApiInfo;
    this.docConfig       = docConfig;
  }
}
