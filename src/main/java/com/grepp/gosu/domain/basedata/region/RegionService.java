package com.grepp.gosu.domain.basedata.region;

import com.grepp.gosu.domain.basedata.region.dto.RegionRequest;
import com.grepp.gosu.domain.basedata.region.dto.RegionResponse;
import com.grepp.gosu.global.exception.BusinessException;
import com.grepp.gosu.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    public List<RegionResponse> getRegions(Long parentId) {
        List<Region> regions = parentId == null
                ? regionRepository.findByParentIsNullOrderByIdAsc()
                : regionRepository.findByParentIdOrderByIdAsc(parentId);
        return regions.stream().map(regionMapper::toResponse).toList();
    }

    @Transactional
    public RegionResponse createRegion(RegionRequest request) {
        Region parent = getParent(request.getParentId());
        checkDuplicate(request, 0L);

        Region entity = regionMapper.toEntity(request);
        entity.setParent(parent);
        Region saved = regionRepository.save(entity);
        return regionMapper.toResponse(saved);
    }

    @Transactional
    public RegionResponse updateRegion(Long id, RegionRequest request) {
        Region entity = getRegionEntity(id);
        if (Objects.equals(id, request.getParentId())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_EXCEPTION);
        }

        Region parent = getParent(request.getParentId());
        if (parent != null && regionRepository.existsByParentId(id)) {
            throw new BusinessException(ErrorCode.REGION_HAS_CHILDREN_EXCEPTION);
        }
        checkDuplicate(request, id);

        entity.setName(request.getName());
        entity.setParent(parent);
        entity.setCode(request.getCode());
        return regionMapper.toResponse(entity);
    }

    @Transactional
    public RegionResponse deleteRegion(Long id) {
        Region entity = getRegionEntity(id);
        if (regionRepository.existsByParentId(id)) {
            throw new BusinessException(ErrorCode.REGION_HAS_CHILDREN_EXCEPTION);
        }

        RegionResponse response = regionMapper.toResponse(entity);
        regionRepository.delete(entity);
        return response;
    }

    public Region getRegionEntity(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REGION_NOT_FOUND_EXCEPTION));
    }

    private Region getParent(Long parentId) {
        if (parentId == null) {
            return null;
        }

        Region parent = getRegionEntity(parentId);
        if (parent.getParent() != null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_EXCEPTION);
        }
        return parent;
    }

    private void checkDuplicate(RegionRequest request, Long id) {
        boolean duplicate = request.getParentId() == null
                ? regionRepository.existsByNameIgnoreCaseAndParentIsNullAndIdNot(request.getName(), id)
                : regionRepository.existsByNameIgnoreCaseAndParentIdAndIdNot(request.getName(), request.getParentId(), id);
        if (duplicate) throw new BusinessException(ErrorCode.DUPLICATE_REGION_EXCEPTION);

        if (regionRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new BusinessException(ErrorCode.DUPLICATE_REGION_CODE_EXCEPTION);
        }
    }
}
