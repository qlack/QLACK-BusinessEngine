package com.eurodyn.qlack.rulesdemo.mapper;

import com.eurodyn.qlack.rulesdemo.dto.CategoryDTO;
import com.eurodyn.qlack.rulesdemo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CategoryMapper extends BaseMapper<CategoryDTO, Category> {
}
