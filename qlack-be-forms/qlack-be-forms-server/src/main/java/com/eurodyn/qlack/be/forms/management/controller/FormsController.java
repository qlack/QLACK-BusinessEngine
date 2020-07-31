package com.eurodyn.qlack.be.forms.management.controller;

import com.eurodyn.qlack.be.forms.management.model.OrbeonFormData;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDataAttachment;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinition;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionAttachment;
import com.eurodyn.qlack.be.forms.management.service.FormsService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * REST Persistence API for Orbeon integration
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Slf4j
@Validated
@RestController
@Transactional
@RequestMapping("/api/forms")
public class FormsController {

    private static final String EMPTY_FORM_ELEMENT = "<form></form>";

  private final FormsService formsService;

  @Autowired
  public FormsController(FormsService formsService) {
    this.formsService = formsService;
  }

  /**
   * @return Returns an empty form element to indicate that no templates are available under the `libary`
   * reserved space.
   */
  @GetMapping({"/form/{app}", "/form/{app}/{form}"})
  public ResponseEntity<String> searchLibraryForm() {
        return ResponseEntity.status(HttpStatus.OK).body(EMPTY_FORM_ELEMENT);
  }

  /**
   * Retrieves stored form data.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @return a stored form with its data. If a form is not found a 404 error code is returned
   */
  @GetMapping("/crud/{app}/{form}/data/{documentId}/data.xml")
  public ResponseEntity<String> findForm(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("documentId") String documentId) {

    OrbeonFormData fd = formsService.findFormData(app, form, documentId);
    return fd != null ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_XML)
        .body(fd.getXml())
        : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }


  /**
   * Retrieves an attachment for a draft form definition or for a published form saved data.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param attachmentName The attachment name
   * @return the attachment file if found. If the attachment is not found a 404 error code is
   * returned
   */
  @GetMapping(value = "/crud/{app}/{form}/data/{documentId}/{attachmentName:[0-9a-z]+.bin}", produces = "application/octet-stream")
  public ResponseEntity<byte[]> getAttachment(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("documentId") String documentId,
      @PathVariable("attachmentName") String attachmentName) {
    OrbeonFormDataAttachment attachment;

    try {
      attachment = formsService.findAttachment(app, form, documentId, attachmentName);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return attachment != null ? ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(attachment.getFileContent()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  /**
   * Stores or updates an attachment for a draft form definition or for a published form saved
   * data.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param attachmentName The attachment name
   * @param httpRequest The HTTP request
   * @return Http status 200 (OK) if attachment is stored, if not Http status 500 (Internal Server
   * Error)
   */
  @PutMapping(value = "/crud/{app}/{form}/data/{documentId}/{attachmentName:[0-9a-z]+.bin}")
  public ResponseEntity<String> storeAttachment(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("documentId") String documentId,
      @PathVariable("attachmentName") String attachmentName,
      HttpServletRequest httpRequest) {
    try {
      formsService.saveOrUpdateAttachment(app, form, attachmentName, documentId,
          httpRequest.getInputStream());
    } catch (IOException ioe) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }


  /**
   * Retrieves a published form XML definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @return a published form XML definition. If a definition is not found Http status 404 (Not
   * Found) is returned
   */
  @GetMapping("/crud/{app}/{form}/form/form.xhtml")
  public ResponseEntity<String> findPublishedForm(@PathVariable("app") String app,
      @PathVariable("form") String form) {

    OrbeonFormDefinition od = formsService.findPublishedForm(app, form);

    return od != null ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_XML)
        .body(od.getXml()) :
        ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  /**
   * Saves the data the user fills in a rendered published form.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param httpRequest The HTTP request
   * @return Http status 200 (OK) if data is stored, if not Http status 500 (Internal Server Error)
   */
  @PutMapping("/crud/{app}/{form}/data/{documentId}/data.xml")
  public ResponseEntity<String> savePublishedFormData(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("documentId") String documentId,
      HttpServletRequest httpRequest) {

    try {
      formsService.saveDataDocument(app, form, documentId, httpRequest.getInputStream());
    } catch (IOException ioException) {
      log.error(ioException.toString());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * Publishes a form by saving it as a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @return Http status 200 (OK) if form is stored, if not Http status 500 (Internal Server Error)
   */
  @PutMapping(value = "/crud/{app}/{form}/form/form.xhtml", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
  public ResponseEntity<String> savePublishedForm(@PathVariable("app") String app,
      @PathVariable("form") String form,
      HttpServletRequest httpRequest) {
    try {
      formsService.saveDefinitionDocument(app, form, httpRequest.getInputStream());
    } catch (IOException | ParserConfigurationException | SAXException exception) {
      log.error(exception.toString());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * Retrieves an attachment for a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param attachmentName The attachment name
   * @return the attachment file if found. If the attachment is not found a 404 error code is
   * returned. If an error occurs a 500 error code (Internal Server Error)
   */
  @GetMapping(value = "/crud/{app}/{form}/form/{attachmentName:[0-9a-z]+.bin}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> retrievePublishedFormAttachment(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("attachmentName") String attachmentName) {
    OrbeonFormDefinitionAttachment attachment;

    try {
      attachment = formsService.findPublishedFormAttachment(app, form, attachmentName);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return attachment != null ? ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(attachment.getFileContent()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  /**
   * Stores an attachment for a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param attachmentName The attachment name
   * @param httpRequest The HTTP request
   * @return Http status 200 (OK) if attachment is stored, if not Http status 500 (Internal Server
   * Error)
   */
  @PutMapping(value = "/crud/{app}/{form}/form/{attachmentName:[0-9a-z]+.bin}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
  public ResponseEntity<String> storePublishedFormAttachment(@PathVariable("app") String app,
      @PathVariable("form") String form,
      @PathVariable("attachmentName") String attachmentName,
      HttpServletRequest httpRequest) {
    try {
      formsService.saveDefinitionAttachment(app, form, attachmentName,
          httpRequest.getInputStream());
    } catch (IOException ioException) {
      log.error(ioException.getMessage());
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
