package com.eurodyn.qlack.rulesdemo.web;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetDTO;
import com.eurodyn.qlack.rulesdemo.model.WorkingSet;
import com.eurodyn.qlack.rulesdemo.service.WorkingSetService;
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
@RequestMapping("/api/workingSets")
@RequiredArgsConstructor
public class WorkingSetController {

    @Autowired
    private final WorkingSetService workingSetService;

    @GetMapping("{workingSetId}")
    public WorkingSetDTO findById(@Valid @PathVariable String workingSetId) {
        return workingSetService.findById(workingSetId);
    }

    @GetMapping("/sorted")
    public List<WorkingSetDTO> findAll(
            @QuerydslPredicate(root = WorkingSet.class) Predicate predicate, Sort sort) {
        return workingSetService.findAll(predicate, sort);
    }

    @PostMapping
    public WorkingSetDTO upload(@Valid @RequestBody WorkingSetDTO workingSetDTO) {
        return workingSetService.save(workingSetDTO);
    }

    @DeleteMapping("{workingSetId}")
    public void delete(@Valid @PathVariable String workingSetId) {
        workingSetService.deleteById(workingSetId);
    }

}
