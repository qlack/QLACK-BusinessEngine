package com.eurodyn.qlack.rulesdemo.web;

import com.eurodyn.qlack.rulesdemo.dto.RuleVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.RuleVersion;
import com.eurodyn.qlack.rulesdemo.service.RuleVersionService;
import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@Transactional
@RequestMapping("/api/ruleVersions")
@RequiredArgsConstructor
public class RuleVersionController {

    @Autowired
    private final RuleVersionService ruleVersionService;

    @GetMapping("{versionId}")
    public RuleVersionDTO findById(@Valid @PathVariable String versionId) {
        return ruleVersionService.findById(versionId);
    }

    @GetMapping("/rule/{ruleId}")
    public List<RuleVersionDTO> findByRule(@Valid @PathVariable String ruleId) {
        return ruleVersionService.findByRule(ruleId);
    }

    @GetMapping("/sorted")
    public List<RuleVersionDTO> findAll(
            @QuerydslPredicate(root = RuleVersion.class) Predicate predicate, Sort sort) {
        return ruleVersionService.findAll(predicate, sort);
    }

    @PostMapping public RuleVersionDTO upload(@Valid @RequestBody RuleVersionDTO ruleVersionDTO) {
        if (ruleVersionService.isXmlWellFormed(ruleVersionDTO.getDmnXml())) {
            return ruleVersionService.save(ruleVersionDTO);
        } else {
            throw new IllegalArgumentException("The Xml is not well formed");
        }
    }

    @PostMapping("/rule/{ruleId}")
    public RuleVersionDTO saveXml(@Valid @PathVariable String ruleId, @RequestBody String xml)
        throws NotFoundException {
        return ruleVersionService.saveXml(ruleId, xml);
    }

    @DeleteMapping("{versionId}") public void delete(@Valid @PathVariable String versionId) {
        ruleVersionService.deleteById(versionId);
    }

}
