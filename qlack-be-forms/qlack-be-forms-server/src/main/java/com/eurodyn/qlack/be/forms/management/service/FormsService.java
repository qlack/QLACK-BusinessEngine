package com.eurodyn.qlack.be.forms.management.service;

import com.eurodyn.qlack.be.forms.management.model.*;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataAttachmentRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDataRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionAttachmentRepository;
import com.eurodyn.qlack.be.forms.management.repository.OrbeonFormDefinitionRepository;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Optional;

import static com.eurodyn.qlack.be.forms.management.util.XMLUtil.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FormsService {

    private static final String JS_FUNCTION =
            "   function sendDataToService() {\n" +
                    "      var id = ORBEON.xforms.Document.getValue(\"fr-parameters-instance-document\");\n" +
                    "      parent.submitDone(id);\n" +
                    "    };\n";

    protected OrbeonFormDataRepository repository;
    protected OrbeonFormDataAttachmentRepository attachmentRepository;
    protected OrbeonFormDefinitionAttachmentRepository definitionAttachmentRepository;
    protected OrbeonFormDefinitionRepository definitionRepository;

    @Autowired
    public FormsService(OrbeonFormDataRepository repository, OrbeonFormDataAttachmentRepository attachmentRepository, OrbeonFormDefinitionAttachmentRepository definitionAttachmentRepository, OrbeonFormDefinitionRepository definitionRepository) {
        this.repository = repository;
        this.attachmentRepository = attachmentRepository;
        this.definitionAttachmentRepository = definitionAttachmentRepository;
        this.definitionRepository = definitionRepository;
    }

    public OrbeonFormData findForm(String app, String form, String docId) {
        Optional<OrbeonFormData> formFound = repository.findOne(
                QOrbeonFormData.orbeonFormData.app.eq(app)
                        .and(QOrbeonFormData.orbeonFormData.form.eq(form))
                        .and(QOrbeonFormData.orbeonFormData.documentId.eq(docId))
                        .and(QOrbeonFormData.orbeonFormData.lastModifiedTime.goe(
                                JPAExpressions.select(QOrbeonFormData.orbeonFormData.lastModifiedTime.max())
                                        .from(QOrbeonFormData.orbeonFormData)
                                        .where(QOrbeonFormData.orbeonFormData.documentId.eq(docId)))));

        return formFound.orElse(null);
    }

    public OrbeonFormDataAttachment retrieveAttachment(String app, String form, String documentId, String attachmentName) {
        Optional<OrbeonFormDataAttachment> attachment = attachmentRepository.findOne(
                QOrbeonFormDataAttachment.orbeonFormDataAttachment.app.eq(app)
                        .and(QOrbeonFormDataAttachment.orbeonFormDataAttachment.form.eq(form))
                        .and(QOrbeonFormDataAttachment.orbeonFormDataAttachment.documentId.eq(documentId))
                                .and(QOrbeonFormDataAttachment.orbeonFormDataAttachment.fileName.eq(attachmentName)));

        return attachment.orElse(null);
    }

    public OrbeonFormDefinitionAttachment retrievePublishedFormAttachment(String app, String form, String attachmentName) {

        Optional<OrbeonFormDefinitionAttachment> definitionAttachment = definitionAttachmentRepository.findOne(
                QOrbeonFormDefinitionAttachment.orbeonFormDefinitionAttachment.app.eq(app)
                        .and(QOrbeonFormDefinitionAttachment.orbeonFormDefinitionAttachment.form.eq(form))
                        .and(QOrbeonFormDefinitionAttachment.orbeonFormDefinitionAttachment.fileName.eq(attachmentName)));

        return definitionAttachment.orElse(null);
    }

    public void saveOrUpdateAttachment(String app, String form, String attachmentName, Long formVersion, String documentId, InputStream inputStream) throws IOException {

        QOrbeonFormDataAttachment qExistingAttachment = QOrbeonFormDataAttachment.orbeonFormDataAttachment;
        Optional<OrbeonFormDataAttachment> existingAttachment = attachmentRepository.findOne(qExistingAttachment.documentId.eq(documentId)
                .and(qExistingAttachment.app.eq(app))
                .and(qExistingAttachment.form.eq(form)));

        byte[] content = IOUtils.toByteArray(inputStream);

        if (existingAttachment.isPresent()) {
            log.info("Updating attachment of form with ID {}", documentId);
            OrbeonFormDataAttachment updatedAttachment = existingAttachment.get();
            updatedAttachment
                    .setFormVersion(formVersion)
                    .setFileName(attachmentName)
                    .setFileContent(content)
                    .setLastModifiedTime(Instant.now());
            // TODO - Set user
//            .setLastModifiedBy();
        } else {
            log.info("Saving attachment of form with ID {}", documentId);

            OrbeonFormDataAttachment attachment = new OrbeonFormDataAttachment();
            attachment.setApp(app)
                    .setForm(form)
                    .setFormVersion(formVersion)
                    .setDocumentId(documentId)
                    .setFileName(attachmentName)
                    .setFileContent(content)
                    .setCreated(Instant.now())
                    .setLastModifiedTime(Instant.now());

            attachmentRepository.save(attachment);
        }


    }

    public void saveOrUpdateDefinitionAttachment(String app, String form, String attachmentName, InputStream inputStream) throws IOException {

        QOrbeonFormDefinitionAttachment qExistingAttachment = QOrbeonFormDefinitionAttachment.orbeonFormDefinitionAttachment;

        Optional<OrbeonFormDefinitionAttachment> existingAttachment = definitionAttachmentRepository.findOne(qExistingAttachment.fileName.eq(attachmentName)
                .and(qExistingAttachment.app.eq(app))
                .and(qExistingAttachment.form.eq(form)));

        byte[] content = IOUtils.toByteArray(inputStream);

        if (existingAttachment.isPresent()) {
            log.info("Updating attachment of form definition with attachment name {}", attachmentName);
            OrbeonFormDefinitionAttachment updatedAttachment = existingAttachment.get();
            updatedAttachment
                    .setFileName(attachmentName)
                    .setFileContent(content)
                    .setLastModifiedTime(Instant.now());
        } else {
            log.info("Saving attachment of form definition with attachment name {}", attachmentName);
            OrbeonFormDefinitionAttachment attachment = new OrbeonFormDefinitionAttachment()
                    .setApp(app)
                    .setForm(form)
                    .setFileName(attachmentName)
                    .setFileContent(content)
                    .setCreated(Instant.now())
                    .setLastModifiedTime(Instant.now());

            // TODO - Handle the version
//        attachment.setFormVersion(formVersion);
            // TODO - Set user or leave null
//        attachment.setLastModifiedBy("admin")

            definitionAttachmentRepository.save(attachment);
        }
    }

    public void saveDataDocument(String app, String form, String documentId, InputStream inputStream) throws
            IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String content = writer.toString();

        log.info("Saving content of form version with ID {}", documentId);

        OrbeonFormData orbeonFormData = new OrbeonFormData();
        Instant now = Instant.now();

        orbeonFormData
                .setId(1L)
                .setApp(app)
                .setForm(form)
                .setLastModifiedTime(now)
                .setDocumentId(documentId)
                .setXml(content)
                .setFormVersion(1L);

        repository.save(orbeonFormData);
    }

    public void saveOrUpdateDefinitionDocument(String app, String form, InputStream inputStream) throws
            IOException, ParserConfigurationException, SAXException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String content = writer.toString();
        Document xmlDocument = getDocumentFromInputStream(content);
        Element metadataEl = (Element) xmlDocument.getElementsByTagName("metadata").item(0);
        Element headEl = (Element) xmlDocument.getElementsByTagName("xh:head").item(0);

        addScriptToDocumentHead(xmlDocument, headEl, JS_FUNCTION);


        QOrbeonFormDefinition qExistingDefinition = QOrbeonFormDefinition.orbeonFormDefinition;

        Optional<OrbeonFormDefinition> existingDefinition = definitionRepository.findOne(
                qExistingDefinition.app.eq(app)
                        .and(qExistingDefinition.form.eq(form)));


        if (existingDefinition.isPresent()) {
            OrbeonFormDefinition updatedDefinition = existingDefinition.get();
            log.info("Updating content of form definition for {}/{} as form-version {}", app, form, increaseVersionByOne(updatedDefinition.getFormVersion()));

            updatedDefinition
                    .setLastModifiedTime(Instant.now())
                    .setFormMetadata(getNodeString(metadataEl))
                    .setXml(getNodeString(xmlDocument))
                    .setFormVersion(increaseVersionByOne(updatedDefinition.getFormVersion()));

            definitionRepository.save(updatedDefinition);
        } else {

            log.info("Saving content of form definition for {}/{} as form-version {}", app, form, 1L);

            OrbeonFormDefinition orbeonFormDefinition = new OrbeonFormDefinition()
                    .setApp(app)
                    .setForm(form)
                    .setCreated(Instant.now())
//                    .setLastModifiedBy("admin")
                    .setLastModifiedTime(Instant.now())
                    .setFormVersion(1L)
                    .setFormMetadata(getNodeString(metadataEl))
                    .setXml(getNodeString(xmlDocument));

            definitionRepository.save(orbeonFormDefinition);
        }

    }



    private long increaseVersionByOne(Long version) {
        return version == null || version == 0 ? 1 : version + 1;
    }

    public OrbeonFormDefinition findPublishedForm(String app, String form) {
        // Get the most recent published form
        Optional<OrbeonFormDefinition> formDefinitionFound = definitionRepository.findOne(
                QOrbeonFormDefinition.orbeonFormDefinition.app.eq(app)
                        .and(QOrbeonFormDefinition.orbeonFormDefinition.form.eq(form))
                        .and(QOrbeonFormDefinition.orbeonFormDefinition.lastModifiedTime.goe(
                                JPAExpressions.select(QOrbeonFormDefinition.orbeonFormDefinition.lastModifiedTime.max())
                                        .from(QOrbeonFormDefinition.orbeonFormDefinition)
                                        .where(QOrbeonFormDefinition.orbeonFormDefinition.app.eq(app)
                                                .and(QOrbeonFormDefinition.orbeonFormDefinition.form.eq(form))))));

        return formDefinitionFound.orElse(null);
    }

    public OrbeonFormDefinition findPublishedForm(String app) {
        // Get the most recent published form
        Optional<OrbeonFormDefinition> formDefinitionFound = definitionRepository.findOne(
                QOrbeonFormDefinition.orbeonFormDefinition.app.eq(app)
                        .and(QOrbeonFormDefinition.orbeonFormDefinition.lastModifiedTime.goe(
                                JPAExpressions.select(QOrbeonFormDefinition.orbeonFormDefinition.lastModifiedTime.max())
                                        .from(QOrbeonFormDefinition.orbeonFormDefinition)
                                        .where(QOrbeonFormDefinition.orbeonFormDefinition.app.eq(app)))));

        return formDefinitionFound.orElse(null);
    }

}
