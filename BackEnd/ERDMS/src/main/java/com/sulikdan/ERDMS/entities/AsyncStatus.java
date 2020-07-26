package com.sulikdan.ERDMS.entities;

/**
 * Class AsyncStatus is used for saving current document status:
 *
 * <p>
 *
 * <ul>
 *   <li>WAITING_TO_SEND - was not yet sent OR processed by OCR
 *   <li>PROCESSING - was sent to OCR and have to be retrieved to see current status, if it was
 *       completed
 *   <li>COMPLETED - the document was wholy scanned by OCR and data are all retrieved.
 * </ul>
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
public enum AsyncStatus {
  WAITING_TO_SEND,
  PROCESSING,
  COMPLETED
}
