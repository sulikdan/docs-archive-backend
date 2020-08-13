package com.sulikdan.ERDMS.entities;

import java.io.File;

/**
 * Class DocumentJob is used for .....
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 25-Jul-20
 */
public class DocumentJob {

  private String id;
  private String nameOfFile;
  private Document document;
  private File storedFile;
  private AsyncApiInfo asyncApiInfo;
}
