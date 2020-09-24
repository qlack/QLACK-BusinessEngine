package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.RuleVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.QRuleVersion;
import com.eurodyn.qlack.rulesdemo.model.RuleVersion;
import javax.transaction.Transactional;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor public class RuleVersionService
    extends BaseService<RuleVersionDTO, RuleVersion> {

    public List<RuleVersionDTO> findByRule(String ruleId) {
        List<RuleVersionDTO> ruleVersionDTOList = new ArrayList<>();
        repository.findAll(QRuleVersion.ruleVersion.rule.id.eq(ruleId))
            .forEach(ruleVersion -> ruleVersionDTOList.add(mapper.map(ruleVersion)));

        return ruleVersionDTOList;
    }

    public boolean isXmlWellFormed(String dmnXml) {
        try {
            InputStream targetStream = new ByteArrayInputStream(dmnXml.getBytes());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dBuilder.parse(targetStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
