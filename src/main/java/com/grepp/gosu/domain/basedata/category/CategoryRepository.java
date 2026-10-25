package com.grepp.gosu.domain.basedata.category;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNullOrderByIdAsc();

    List<Category> findByParentIdOrderByIdAsc(Long parentId);

    boolean existsByParentId(Long parentId);

    boolean existsByNameIgnoreCaseAndParentIsNullAndIdNot(String name, Long id);

    boolean existsByNameIgnoreCaseAndParentIdAndIdNot(String name, Long parentId, Long id);
}
