package com.sulikdan.ERDMS.services.ocr;

import com.sulikdan.ERDMS.entities.Doc;

/**
 * Created by Daniel Šulik on 22-Jul-20
 *
 * <p>Class OCRService is service layer interface for communication with OCR API.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
public interface OCRService {

  Doc extractTextFromDoc(Doc doc);

}
