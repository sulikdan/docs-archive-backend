package com.sulikdan.ERDMS.entities;

/**
 * Class AsyncStatus is used for saving current document status:
 *
 * <p>
 *
 * <ul>
 *   <li>WAITING_TO_SEND - was not yet sent OR processed by a OCR.
 *   <li>MANUAL_SENDING - will be sent immediately to the worker queue.
 *   <li>PROCESSING - was sent to OCR and have to be retrieved to see current status, if it was
 *       completed.
 *   <li>SCANNED - the document was wholy scanned by OCR and data are available.
 *   <li>RESOURCE_TO_CLEAN - the document was received, but the resources sent were not deleted.
 *   <li>COMPLETED - the document was downloaded from the OCR.
 *   <li>FAILED - repeatedly occurred a problem or an error, while trying to scan file.
 * </ul>
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
public enum AsyncApiState {
  WAITING_TO_SEND,
  MANUAL_SENDING,
  PROCESSING,
  SCANNED,
  RESOURCE_TO_CLEAN,
  COMPLETED,
  FAILED
}
