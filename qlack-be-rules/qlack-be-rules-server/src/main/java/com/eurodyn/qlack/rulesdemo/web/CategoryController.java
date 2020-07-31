package com.eurodyn.qlack.rulesdemo.web;

import com.eurodyn.qlack.rulesdemo.dto.CategoryDTO;
import com.eurodyn.qlack.rulesdemo.model.Category;
import com.eurodyn.qlack.rulesdemo.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @GetMapping("{categoryId}")
    public CategoryDTO findById(@Valid @PathVariable String categoryId) {
        return categoryService.findById(categoryId);
    }

    @GetMapping("/sorted")
    public List<CategoryDTO> findAll(
            @QuerydslPredicate(root = Category.class) Predicate predicate, Sort sort) {
        return categoryService.findAll(predicate, sort);
    }

    @PostMapping
    public CategoryDTO upload(@Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.save(categoryDTO);
    }

    @DeleteMapping("{categoryId}")
    public void delete(@Valid @PathVariable String categoryId) {
        categoryService.deleteById(categoryId);
    }

}
