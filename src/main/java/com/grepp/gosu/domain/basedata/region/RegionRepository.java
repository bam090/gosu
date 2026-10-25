package com.grepp.gosu.domain.basedata.region;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByParentIsNullOrderByIdAsc();

    List<Region> findByParentIdOrderByIdAsc(Long parentId);

    boolean existsByParentId(Long parentId);

    boolean existsByNameIgnoreCaseAndParentIsNullAndIdNot(String name, Long id);

    boolean existsByNameIgnoreCaseAndParentIdAndIdNot(String name, Long parentId, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);
}
