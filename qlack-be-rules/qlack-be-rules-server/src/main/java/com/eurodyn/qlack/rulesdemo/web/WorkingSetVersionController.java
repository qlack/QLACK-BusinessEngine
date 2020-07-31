package com.eurodyn.qlack.rulesdemo.web;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.RuleVersion;
import com.eurodyn.qlack.rulesdemo.service.WorkingSetVersionService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@Transactional
@RequestMapping("/api/workingSetVersions")
@RequiredArgsConstructor
public class WorkingSetVersionController {

    @Autowired
    private final WorkingSetVersionService workingSetVersionService;

    @GetMapping("{versionId}")
    public WorkingSetVersionDTO findById(@Valid @PathVariable String versionId) {
        return workingSetVersionService.findById(versionId);
    }

    @GetMapping("/sorted")
    public List<WorkingSetVersionDTO> findAll(
            @QuerydslPredicate(root = RuleVersion.class) Predicate predicate, Sort sort) {
        return workingSetVersionService.findAll(predicate, sort);
    }

    @PostMapping
    public WorkingSetVersionDTO upload(@RequestBody WorkingSetVersionDTO ruleVersionDTO) {
        return workingSetVersionService.save(ruleVersionDTO);
    }

    @DeleteMapping("{versionId}")
    public void delete(@Valid @PathVariable String versionId) {
        workingSetVersionService.deleteById(versionId);
    }

}
