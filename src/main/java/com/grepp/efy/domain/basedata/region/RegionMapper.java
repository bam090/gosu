package com.grepp.efy.domain.basedata.region;

import com.grepp.efy.domain.basedata.region.dto.RegionResponse;
import com.grepp.efy.domain.basedata.region.dto.RegionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    Region toEntity(RegionRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    RegionResponse toResponse(Region region);
}
