package com.grepp.gosu.domain.basedata.region;

import com.grepp.gosu.domain.basedata.region.docs.RegionControllerDocs;

import com.grepp.gosu.domain.basedata.region.dto.RegionResponse;
import com.grepp.gosu.domain.basedata.region.dto.RegionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegionController implements RegionControllerDocs {

    private final RegionService regionService;

    @GetMapping("/public/regions")
    @Override
    public ResponseEntity<List<RegionResponse>> getRegions(@RequestParam(required = false) Long parentId) {
        List<RegionResponse> response = regionService.getRegions(parentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/regions")
    @Override
    public ResponseEntity<RegionResponse> createRegion(@Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.createRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/admin/regions/{id}")
    @Override
    public ResponseEntity<RegionResponse> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.updateRegion(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/regions/{id}")
    @Override
    public ResponseEntity<RegionResponse> deleteRegion(@PathVariable Long id) {
        RegionResponse response = regionService.deleteRegion(id);
        return ResponseEntity.ok(response);
    }
}
