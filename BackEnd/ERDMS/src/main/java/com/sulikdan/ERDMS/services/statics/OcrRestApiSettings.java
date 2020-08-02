package com.sulikdan.ERDMS.services.statics;

/**
 * Created by Daniel Šulik on 31-Jul-20
 * <p>
 * Class OcrRestApiSettings is used for .....
 */
public class OcrRestApiSettings {
    protected static final String BASE_URI="http://localhost:8080/ocr";
    protected static final String SYNC = "/sync";
    protected static final String IMG_DOC_CONTRL="/document";
    protected static final String PDF_DOC_CONTRL="/pdf";
    protected static final String STRGE_DOC_CONTRL="/storage";

    protected String getImgUri(){
        return BASE_URI + IMG_DOC_CONTRL;
    }

    protected String getPdfUri(){
        return BASE_URI + PDF_DOC_CONTRL;
    }

    protected String getStrgeUri(){
        return BASE_URI + STRGE_DOC_CONTRL;
    }

}
