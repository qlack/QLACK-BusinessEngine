package com.eurodyn.qlack.rulesdemo.web;

import com.eurodyn.qlack.rulesdemo.dto.RuleDTO;
import com.eurodyn.qlack.rulesdemo.model.Rule;
import com.eurodyn.qlack.rulesdemo.service.RuleService;
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
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    @Autowired
    private final RuleService ruleService;

    @GetMapping("{ruleId}")
    public RuleDTO findById(@Valid @PathVariable String ruleId) {
        return ruleService.findById(ruleId);
    }

    @GetMapping("/sorted")
    public List<RuleDTO> findAll(
            @QuerydslPredicate(root = Rule.class) Predicate predicate, Sort sort) {
        return ruleService.findAll(predicate, sort);
    }

    @PostMapping
    public RuleDTO upload(@Valid @RequestBody RuleDTO ruleDTO) {
        return ruleService.save(ruleDTO);
    }

    @DeleteMapping("{ruleId}")
    public void delete(@Valid @PathVariable String ruleId) {
        ruleService.deleteById(ruleId);
    }

}
