package com.grepp.efy.domain.basedata.category;

import com.grepp.efy.domain.basedata.category.dto.CategoryResponse;
import com.grepp.efy.domain.basedata.category.dto.CategoryRequest;
import com.grepp.efy.global.exception.BusinessException;
import com.grepp.efy.global.exception.ErrorCode;
import java.util.Objects;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories(Long parentId) {
        List<Category> categories = parentId == null
                ? categoryRepository.findByParentIsNullOrderByIdAsc()
                : categoryRepository.findByParentIdOrderByIdAsc(parentId);
        return categories.stream().map(categoryMapper::toResponse).toList();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category parent = getParent(request.getParentId());
        checkDuplicate(request, 0L);

        Category entity = categoryMapper.toEntity(request);
        entity.setParent(parent);
        Category saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category entity = getCategoryEntity(id);
        if (Objects.equals(id, request.getParentId())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_EXCEPTION);
        }

        Category parent = getParent(request.getParentId());
        if (parent != null && categoryRepository.existsByParentId(id)) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_CHILDREN_EXCEPTION);
        }
        checkDuplicate(request, id);

        entity.setName(request.getName());
        entity.setParent(parent);
        return categoryMapper.toResponse(entity);
    }

    @Transactional
    public CategoryResponse deleteCategory(Long id) {
        Category entity = getCategoryEntity(id);
        if (categoryRepository.existsByParentId(id)) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_CHILDREN_EXCEPTION);
        }

        CategoryResponse response = categoryMapper.toResponse(entity);
        categoryRepository.delete(entity);
        return response;
    }

    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND_EXCEPTION));
    }

    private Category getParent(Long parentId) {
        if (parentId == null) {
            return null;
        }

        Category parent = getCategoryEntity(parentId);
        if (parent.getParent() != null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_EXCEPTION);
        }
        return parent;
    }

    private void checkDuplicate(CategoryRequest request, Long id) {
        boolean duplicate = request.getParentId() == null
                ? categoryRepository.existsByNameIgnoreCaseAndParentIsNullAndIdNot(request.getName(), id)
                : categoryRepository.existsByNameIgnoreCaseAndParentIdAndIdNot(request.getName(), request.getParentId(), id);
        if (duplicate) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_EXCEPTION);
        }
    }
}
