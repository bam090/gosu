package com.grepp.efy.global.util;

import com.grepp.efy.global.exception.BusinessException;
import com.grepp.efy.global.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

// 페이지 번호(1부터), 크기, 정렬 조건을 검사해서 Pageable을 만든다 (BlogRest 기반)
// 잘못된 값이면 IllegalArgumentException 대신 BusinessException을 던져서 500이 아닌 400으로 응답한다
public final class PageableFactory {

    private PageableFactory() {
    }

    public static Pageable create(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                  List<String> sortableFields) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new BusinessException(ErrorCode.INVALID_PAGE_REQUEST_EXCEPTION);
        }
        if (!sortableFields.contains(sortBy)) {
            throw new BusinessException(ErrorCode.INVALID_PAGE_REQUEST_EXCEPTION);
        }
        if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
            throw new BusinessException(ErrorCode.INVALID_PAGE_REQUEST_EXCEPTION);
        }

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(direction, sortBy));
    }
}
