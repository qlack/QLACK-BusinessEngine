package com.eurodyn.qlack.be.forms.management.service;

import static com.eurodyn.qlack.be.forms.management.util.XMLUtil.addScriptToDocumentHead;
import static com.eurodyn.qlack.be.forms.management.util.XMLUtil.getDocumentFromInputStream;
import static com.eurodyn.qlack.be.forms.management.util.XMLUtil.getNodeString;

import com.eurodyn.qlack.be.forms.management.model.OrbeonFormData;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDataAttachment;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDataAttachmentPK;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinition;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionAttachment;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionAttachmentPK;
import com.eurodyn.qlack.be.forms.management.model.OrbeonFormDefinitionPK;
import com.eurodyn.qlack.be.forms.management.model.OrbeonIControlText;
import com.eurodyn.qlack.be.forms.management.model.OrbeonICurrent;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonFormData;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonFormDataAttachment;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonFormDefinition;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonFormDefinitionAttachment;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonIControlText;
import com.eurodyn.qlack.be.forms.management.model.QOrbeonICurrent;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataAttachmentRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionAttachmentRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonIControlTextRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonICurrentRepository;
import com.eurodyn.qlack.be.forms.management.util.XMLUtil;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author EUROPEAN DYNAMICS SA.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FormsService {

  private static final String JS_FUNCTION =
      "   function notifyParentWindow() {\n" +
          "      var id = ORBEON.xforms.Document.getValue(\"fr-parameters-instance-document\");\n" +
          "      parent.submitDone(id,'%s','%s');\n" +
          "    };\n";
  private static final String TITLE_CONTROL = "xh:head/xf:model[@id = 'fr-form-model']/xf:instance[@id = 'fr-form-metadata']/*/title";
  private static final String DESCRIPTION_CONTROL = "xh:head/xf:model[@id = 'fr-form-model']/xf:instance[@id = 'fr-form-metadata']/*/description";
  private static final String APP_NAME_CONTROL = "xh:head/xf:model[@id = 'fr-form-model']/xf:instance[@id = 'fr-form-metadata']/*/application-name";
  private static final String FORM_NAME_CONTROL = "xh:head/xf:model[@id = 'fr-form-model']/xf:instance[@id = 'fr-form-metadata']/*/form-name";

  private static final String ORBEON_RESERVED_APP_NAME = "orbeon";
  private static final String ORBEON_RESERVED_FORM_NAME = "builder";

  protected OrbeonFormDataRepository repository;
  protected OrbeonFormDataAttachmentRepository attachmentRepository;
  protected OrbeonFormDefinitionAttachmentRepository definitionAttachmentRepository;
  protected OrbeonFormDefinitionRepository definitionRepository;
  protected OrbeonIControlTextRepository iControlTextRepository;
  protected OrbeonICurrentRepository iCurrentRepository;

  @Autowired
  public FormsService(OrbeonFormDataRepository repository,
      OrbeonFormDataAttachmentRepository attachmentRepository,
      OrbeonFormDefinitionAttachmentRepository definitionAttachmentRepository,
      OrbeonFormDefinitionRepository definitionRepository,
      OrbeonIControlTextRepository iControlTextRepository,
      OrbeonICurrentRepository iCurrentRepository) {
    this.repository = repository;
    this.attachmentRepository = attachmentRepository;
    this.definitionAttachmentRepository = definitionAttachmentRepository;
    this.definitionRepository = definitionRepository;
    this.iControlTextRepository = iControlTextRepository;
    this.iCurrentRepository = iCurrentRepository;
  }

  /**
   * Finds form data based on criteria
   *
   * @param app The app name
   * @param form The form name
   * @param documentId The form document Id
   * @return an {@link OrbeonFormData} object or null if not found
   */
  public OrbeonFormData findFormData(String app, String form, String documentId) {
    return getOrbeonFormData(app, form, documentId);
  }

  /**
   * Find form data or form data drafts.
   * @param app The app name
   * @param form The form name
   * @param documentId The form document Id
   * @return an {@link OrbeonFormData} object or null if not found
   */
  private OrbeonFormData getOrbeonFormData(String app, String form, String documentId) {
    QOrbeonFormData qorbeonFormData = QOrbeonFormData.orbeonFormData;

    Iterable<OrbeonFormData> forms = repository.findAll(
        qorbeonFormData.documentId.eq(documentId)
            .and(qorbeonFormData.app.eq(app))
            .and(qorbeonFormData.form.eq(form)));

    Optional<OrbeonFormData> formFound = StreamSupport.stream(forms.spliterator(), false)
        .max(Comparator.comparing(OrbeonFormData::getLastModifiedTime));

    return formFound.orElse(null);
  }


  /**
   * Retrieves an attachment for a draft form definition or for a published form saved data.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param attachmentName The attachment name
   * @return the attachment file. If the attachment is not found null is returned
   */
  public OrbeonFormDataAttachment findAttachment(String app, String form, String documentId,
      String attachmentName) {
    QOrbeonFormDataAttachment qdataAttachment = QOrbeonFormDataAttachment.orbeonFormDataAttachment;
    Optional<OrbeonFormDataAttachment> attachment = attachmentRepository.findOne(
        qdataAttachment.orbeonFormDataAttachmentPK.app.eq(app)
            .and(qdataAttachment.orbeonFormDataAttachmentPK.form.eq(form))
            .and(qdataAttachment.documentId.eq(documentId))
            .and(qdataAttachment.orbeonFormDataAttachmentPK.fileName.eq(attachmentName)));

    return attachment.orElse(null);
  }

  /**
   * Retrieves an attachment for a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param attachmentName The attachment name
   * @return the attachment file if found. If the attachment is not found null is returned.
   */
  public OrbeonFormDefinitionAttachment findPublishedFormAttachment(String app, String form,
      String attachmentName) {

    QOrbeonFormDefinitionAttachment qdefinitionAttachment =
        QOrbeonFormDefinitionAttachment.orbeonFormDefinitionAttachment;

    Optional<OrbeonFormDefinitionAttachment> definitionAttachment = definitionAttachmentRepository
        .findOne(qdefinitionAttachment.orbeonFormDefinitionAttachmentPK.app.eq(app)
            .and(qdefinitionAttachment.orbeonFormDefinitionAttachmentPK.form.eq(form))
            .and(qdefinitionAttachment.orbeonFormDefinitionAttachmentPK.fileName
                .eq(attachmentName)));

    return definitionAttachment.orElse(null);
  }


  /**
   * Saves or updates an attachment for a draft form definition or for a published form saved data.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param attachmentName The attachment name
   */
  public void saveOrUpdateAttachment(String app, String form, String attachmentName, String documentId, InputStream inputStream) throws IOException {

    QOrbeonFormDataAttachment qExistingAttachment = QOrbeonFormDataAttachment.orbeonFormDataAttachment;
    Optional<OrbeonFormDataAttachment> existingAttachment = attachmentRepository
        .findOne(qExistingAttachment.documentId.eq(documentId)
            .and(qExistingAttachment.orbeonFormDataAttachmentPK.app.eq(app))
            .and(qExistingAttachment.orbeonFormDataAttachmentPK.form.eq(form))
            .and(qExistingAttachment.orbeonFormDataAttachmentPK.fileName.eq(attachmentName)));

    byte[] content = IOUtils.toByteArray(inputStream);

    if (existingAttachment.isPresent()) {
      attachmentRepository.delete(existingAttachment.get());
    } else {
      log.info("Updating attachment of form with ID {} and name {}", documentId, attachmentName);

      OrbeonFormDataAttachmentPK pk = new OrbeonFormDataAttachmentPK(app, form, attachmentName);
      OrbeonFormDataAttachment attachment = new OrbeonFormDataAttachment()
          .setOrbeonFormDataAttachmentPK(pk)
          .setDocumentId(documentId)
          .setFileContent(content)
          .setCreated(Instant.now())
          .setLastModifiedTime(Instant.now());

      attachmentRepository.save(attachment);
    }
  }

  /**
   * Saves an attachment for a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param attachmentName The attachment name
   */
  public void saveDefinitionAttachment(String app, String form, String attachmentName,
      InputStream inputStream) throws IOException {

    long currentFormVersion = findPublishedFormMostRecentVersion(app, form);

    byte[] content = IOUtils.toByteArray(inputStream);
    OrbeonFormDefinitionAttachmentPK pk = new OrbeonFormDefinitionAttachmentPK(app, form,
        attachmentName, increaseVersionByOne(currentFormVersion));

    log.info("Saving attachment of form definition with attachment name {}", attachmentName);
    OrbeonFormDefinitionAttachment attachment = new OrbeonFormDefinitionAttachment()
        .setOrbeonFormDefinitionAttachmentPK(pk)
        .setFileContent(content)
        .setCreated(Instant.now())
        .setLastModifiedTime(Instant.now());

    definitionAttachmentRepository.save(attachment);

  }

  /**
   * Saves the data the user fills in a rendered published form.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param documentId The unique form document Id
   * @param inputStream The request content as an inputStream
   */
  public void saveDataDocument(String app, String form, String documentId, InputStream inputStream)
      throws
      IOException {
    OrbeonFormData lastRecentForm = findMostRecentFormDraft(documentId);
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, "UTF-8");
    String content = writer.toString();

    log.info("Saving content of form version with ID {}", documentId);

    OrbeonFormData orbeonFormData = new OrbeonFormData();
    Instant now = Instant.now();

    orbeonFormData
        .setApp(app)
        .setForm(form)
        .setCreated(now)
        .setLastModifiedTime(now)
        .setDocumentId(documentId)
        .setXml(content)
        .setFormVersion(1L);

    OrbeonFormData savedFormData = repository.save(orbeonFormData);

    try {
      if (app.equals(ORBEON_RESERVED_APP_NAME) && form.equals(ORBEON_RESERVED_FORM_NAME)) {
        saveControlTextIndex(lastRecentForm, savedFormData);
      }
      saveCurrentFormIndex(documentId, savedFormData);

    } catch (ParserConfigurationException | SAXException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Saves the ControlText index table records for the current form. Important. This method uses
   * others that return result based on date fields (most recent records). When running together
   * with an external orbeon instance make sure timezones are aligned on the database.
   *
   * @param lastRecentForm the most recent draft form before the current being saved
   * @param savedFormData the saved form
   * @throws ParserConfigurationException when error occurs during parsing the xml document
   * @throws SAXException when error occurs during parsing the xml document
   * @throws IOException when error occurs during parsing the xml document
   */
  private void saveControlTextIndex(OrbeonFormData lastRecentForm,
      OrbeonFormData savedFormData) throws ParserConfigurationException, SAXException, IOException {

    if (lastRecentForm != null) {
      Iterable<OrbeonIControlText> controlsIndex = iControlTextRepository
          .findAll(QOrbeonIControlText.orbeonIControlText.dataId.eq(lastRecentForm.getId()));
      long size = StreamSupport.stream(controlsIndex.spliterator(), false).count();

      if (size > 0) {
        iControlTextRepository.deleteAll(controlsIndex);
      }
    }

    Document xmlDocument = getDocumentFromInputStream(savedFormData.getXml());
    Element metadataEl = XMLUtil.getFirstElementOccurrence(xmlDocument, "metadata");
    String title = XMLUtil.getFirstElementOccurrenceValue(metadataEl, "title");
    String description = XMLUtil.getFirstElementOccurrenceValue(metadataEl, "description");
    String appName = XMLUtil.getFirstElementOccurrenceValue(metadataEl, "application-name");
    String formName = XMLUtil.getFirstElementOccurrenceValue(metadataEl, "form-name");

    createOrbeonControlIndex(savedFormData.getId(), 1L, TITLE_CONTROL, title);
    createOrbeonControlIndex(savedFormData.getId(), 1L, DESCRIPTION_CONTROL, description);
    createOrbeonControlIndex(savedFormData.getId(), 1L, APP_NAME_CONTROL, appName);
    createOrbeonControlIndex(savedFormData.getId(), 1L, FORM_NAME_CONTROL, formName);
  }


  /**
   * Creates {@link OrbeonIControlText} object
   *
   * @param dataId the id of an {@link OrbeonFormData} object record
   * @param position the position of the control
   * @param controlText the control XPATH path in the form xml document
   * @param val the control value
   */
  private void createOrbeonControlIndex(long dataId, long position, String controlText,
      String val) {
    if (!val.isEmpty()) {
      OrbeonIControlText controlIndex = new OrbeonIControlText()
          .setControl(controlText)
          .setPos(position)
          .setVal(val)
          .setDataId(dataId);

      iControlTextRepository.save(controlIndex);
    }
  }

  /**
   * Saves the CurrentFormIndex table records for the current documentId.
   *
   * @param documentId the documentId of the current saved form
   * @param savedFormData the saved form
   */
  private void saveCurrentFormIndex(String documentId, OrbeonFormData savedFormData) {

    OrbeonICurrent currentForm = iCurrentRepository
        .findOne(QOrbeonICurrent.orbeonICurrent.documentId.eq(documentId)).orElse(null);

    if (currentForm != null) {
      iCurrentRepository.delete(currentForm);
    }

    OrbeonICurrent current = new OrbeonICurrent()
        .setApp(savedFormData.getApp())
        .setForm(savedFormData.getForm())
        .setCreated(savedFormData.getCreated())
        .setLastModifiedTime(savedFormData.getLastModifiedTime())
        .setDataId(savedFormData.getId())
        .setDocumentId(savedFormData.getDocumentId())
        .setFormVersion(savedFormData.getFormVersion())
        .setDraft("N");
    iCurrentRepository.save(current);
  }

  /**
   * Publishes a form by saving it as a published form definition.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @param inputStream The request content as an inputStream
   */
  public void saveDefinitionDocument(String app, String form, InputStream inputStream)
      throws
      IOException, ParserConfigurationException, SAXException {

    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, "UTF-8");
    String content = writer.toString();
    Document xmlDocument = getDocumentFromInputStream(content);
    Element metadataEl = (Element) xmlDocument.getElementsByTagName("metadata").item(0);
    Element headEl = (Element) xmlDocument.getElementsByTagName("xh:head").item(0);

    addScriptToDocumentHead(xmlDocument, headEl, String.format(JS_FUNCTION, app, form));

    long currentFormVersion = findPublishedFormMostRecentVersion(app, form);

    log.info("Saving content of form definition for {}/{} as form-version {}", app,
        form, currentFormVersion);

    OrbeonFormDefinitionPK orbeonFormDefinitionPK = new OrbeonFormDefinitionPK(app, form,
        increaseVersionByOne(currentFormVersion));

    OrbeonFormDefinition orbeonFormDefinition = new OrbeonFormDefinition()
        .setOrbeonFormDefinitionPK(orbeonFormDefinitionPK)
        .setCreated(Instant.now())
        .setLastModifiedTime(Instant.now())
        .setFormMetadata(getNodeString(metadataEl))
        .setXml(getNodeString(xmlDocument));

    definitionRepository.save(orbeonFormDefinition);

  }

  /**
   * Increases version number by one
   *
   * @param version current version number
   * @return the new version number
   */
  private long increaseVersionByOne(Long version) {
    return version == null || version == 0 ? 1 : version + 1;
  }


  /**
   * Retrieves the most recent published form XML definition based on criteria.
   *
   * @param app The app name the form belongs to
   * @param form The form name
   * @return a published form XML definition. If a definition is not found null is returned
   */
  public OrbeonFormDefinition findPublishedForm(String app, String form) {
    // Get the most recent published form
    Iterable<OrbeonFormDefinition> publishedForms = definitionRepository.findAll(
        QOrbeonFormDefinition.orbeonFormDefinition.orbeonFormDefinitionPK.app.eq(app)
            .and(QOrbeonFormDefinition.orbeonFormDefinition.orbeonFormDefinitionPK.form.eq(form)));

    Optional<OrbeonFormDefinition> formDefinitionFound = StreamSupport
        .stream(publishedForms.spliterator(), false)
        .max(Comparator.comparing(d -> d.getOrbeonFormDefinitionPK().getFormVersion()));

    return formDefinitionFound.orElse(null);
  }

  /**
   * Finds the most recent draft form definition
   *
   * @param documentId the form documentId
   * @return the form definition or null if not found
   */
  private OrbeonFormData findMostRecentFormDraft(String documentId) {
    return getOrbeonFormData(ORBEON_RESERVED_APP_NAME, ORBEON_RESERVED_FORM_NAME, documentId);
  }

  /**
   * Retrieves the most recent published form XML definition based on criteria.
   *
   * @param app The app name the form belongs to
   * @return a published form XML definition. If a definition is not found 0 is returned
   */
  public long findPublishedFormMostRecentVersion(String app, String form) {
    QOrbeonFormDefinition qorbeonFormDefinition = QOrbeonFormDefinition.orbeonFormDefinition;
    // Get the most recent published form
    Iterable<OrbeonFormDefinition> publishedForms = definitionRepository.findAll(
        qorbeonFormDefinition.orbeonFormDefinitionPK.app.eq(app)
            .and(qorbeonFormDefinition.orbeonFormDefinitionPK.form.eq(form)));

    return StreamSupport
        .stream(publishedForms.spliterator(), false)
        .map(OrbeonFormDefinition::getOrbeonFormDefinitionPK)
        .mapToLong(OrbeonFormDefinitionPK::getFormVersion)
        .max().orElse(0L);
  }

}
