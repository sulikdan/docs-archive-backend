package com.sulikdan.ERDMS.services.statics;

import com.sulikdan.ERDMS.configurations.properties.OcrProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Daniel Šulik on 31-Jul-20
 *
 * <p>Class OcrRestApiSettings is used for .....
 */
@Component
public class OcrRestApiSettings {
  protected final String BASE_URI;

  protected final String SYNC;
  protected final String IMG_DOC_CONTRL;
  protected final String PDF_DOC_CONTRL;
  protected final String STRGE_DOC_CONTRL;

  protected final OcrProperties ocrProperties;

  public OcrRestApiSettings(OcrProperties ocrProperties) {
    this.ocrProperties = ocrProperties;

    SYNC = "/sync";
    IMG_DOC_CONTRL = "/documents";
    PDF_DOC_CONTRL = "/pdfs";
    STRGE_DOC_CONTRL = "/storage";

    BASE_URI =
        "http://" + this.ocrProperties.getAddress() + ":" + this.ocrProperties.getPort() + "/api/ocr";
  }

  protected String getImgUri() {
    return BASE_URI + IMG_DOC_CONTRL;
  }

  protected String getPdfUri() {
    return BASE_URI + PDF_DOC_CONTRL;
  }

  protected String getStrgeUri() {
    return BASE_URI + STRGE_DOC_CONTRL;
  }
}
