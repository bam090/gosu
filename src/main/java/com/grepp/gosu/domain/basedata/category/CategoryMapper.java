package com.grepp.gosu.domain.basedata.category;

import com.grepp.gosu.domain.basedata.category.dto.CategoryResponse;
import com.grepp.gosu.domain.basedata.category.dto.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    CategoryResponse toResponse(Category category);
}
