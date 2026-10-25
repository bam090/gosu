package com.grepp.gosu.domain.basedata.region;

import com.grepp.gosu.domain.basedata.region.dto.RegionResponse;
import com.grepp.gosu.domain.basedata.region.dto.RegionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Tag(name = "지역", description = "지역 조회 및 관리자 지역 등록·수정·삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "지역 목록 조회")
    @SecurityRequirements
    @GetMapping("/public/regions")
    public ResponseEntity<List<RegionResponse>> getRegions(@RequestParam(required = false) Long parentId) {
        List<RegionResponse> response = regionService.getRegions(parentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "지역 등록")
    @PostMapping("/admin/regions")
    public ResponseEntity<RegionResponse> createRegion(@Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.createRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "지역 수정")
    @PutMapping("/admin/regions/{id}")
    public ResponseEntity<RegionResponse> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.updateRegion(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "지역 삭제")
    @DeleteMapping("/admin/regions/{id}")
    public ResponseEntity<RegionResponse> deleteRegion(@PathVariable Long id) {
        RegionResponse response = regionService.deleteRegion(id);
        return ResponseEntity.ok(response);
    }
}
