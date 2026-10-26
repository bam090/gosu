package com.grepp.efy.domain.basedata.category.docs;

import com.grepp.efy.domain.basedata.category.dto.CategoryRequest;
import com.grepp.efy.domain.basedata.category.dto.CategoryResponse;
import java.util.List;
import com.grepp.efy.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "카테고리", description = "카테고리 조회 및 관리자 등록·수정·삭제 API")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 목록 조회",
            description = "로그인 없이 호출할 수 있습니다. parentId를 생략하면 대분류, 지정하면 해당 항목의 소분류 목록을 ID 오름차순으로 반환합니다. 하위 항목이 없으면 빈 배열을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공. 목록 또는 빈 배열 반환")
    @ApiResponse(responseCode = "400", description = "parentId가 올바른 숫자 형식이 아님",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "상위 ID 형식 오류", value = """
                            {
                              "message": "경로 또는 쿼리 값의 타입이 올바르지 않습니다."
                            }
                            """)
            }))
    @SecurityRequirements
    ResponseEntity<List<CategoryResponse>> getCategories(Long parentId);

    @Operation(summary = "카테고리 등록",
            description = "관리자만 호출할 수 있습니다. parentId가 없으면 대분류, 있으면 해당 대분류 아래 소분류를 등록합니다. 같은 상위 항목의 이름은 대소문자를 구분하지 않고 중복을 검사합니다.")
    @ApiResponse(responseCode = "201", description = "등록 성공. 생성된 카테고리 반환")
    @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 허용되지 않는 계층 관계",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이름 누락", value = """
                            {
                              "message": "입력값이 올바르지 않습니다.",
                              "errors": {
                                "name": "카테고리 이름을 입력해 주세요."
                              }
                            }
                            """),
                    @ExampleObject(name = "계층 오류", value = """
                            {
                              "message": "입력값이 올바르지 않습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "401", description = "인증 정보가 없거나 유효하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "인증 실패", value = """
                            {
                              "message": "인증에 실패했습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "403", description = "관리자 권한이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "권한 부족", value = """
                            {
                              "message": "접근 권한이 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "404", description = "대상 또는 상위 카테고리가 존재하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "카테고리 없음", value = """
                            {
                              "message": "카테고리를 찾을 수 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "409", description = "동일 상위 항목의 이름 중복",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이름 중복", value = """
                            {
                              "message": "같은 상위 항목에 이미 사용 중인 카테고리 이름입니다."
                            }
                            """)
            }))
    ResponseEntity<CategoryResponse> createCategory(CategoryRequest request);

    @Operation(summary = "카테고리 수정",
            description = "관리자만 호출할 수 있습니다. 이름과 상위 항목를 수정합니다. parentId를 생략하거나 null로 보내면 최상위 항목이 됩니다. 자기 자신을 부모로 지정하거나 3단계 계층을 만들 수 없으며, 하위 항목이 있는 최상위 항목을 다른 항목 아래로 이동할 수 없습니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공. 수정된 카테고리 반환")
    @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 허용되지 않는 계층 관계",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이름 누락", value = """
                            {
                              "message": "입력값이 올바르지 않습니다.",
                              "errors": {
                                "name": "카테고리 이름을 입력해 주세요."
                              }
                            }
                            """),
                    @ExampleObject(name = "계층 오류", value = """
                            {
                              "message": "입력값이 올바르지 않습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "401", description = "인증 정보가 없거나 유효하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "인증 실패", value = """
                            {
                              "message": "인증에 실패했습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "403", description = "관리자 권한이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "권한 부족", value = """
                            {
                              "message": "접근 권한이 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "404", description = "대상 또는 상위 카테고리가 존재하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "카테고리 없음", value = """
                            {
                              "message": "카테고리를 찾을 수 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "409", description = "이름 중복 또는 하위 항목이 있는 항목의 이동",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이름 중복", value = """
                            {
                              "message": "같은 상위 항목에 이미 사용 중인 카테고리 이름입니다."
                            }
                            """),
                    @ExampleObject(name = "하위 항목 존재", value = """
                            {
                              "message": "하위 카테고리가 있어 처리할 수 없습니다."
                            }
                            """)
            }))
    ResponseEntity<CategoryResponse> updateCategory(
            Long id,
            CategoryRequest request
    );

    @Operation(summary = "카테고리 삭제",
            description = "관리자만 호출할 수 있습니다. 하위 항목이 없는 카테고리를 물리 삭제하고 삭제한 항목을 반환합니다. 하위 항목이나 다른 데이터의 외래 키 참조가 있으면 삭제할 수 없습니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공. 삭제한 카테고리 반환")
    @ApiResponse(responseCode = "400", description = "id가 올바른 숫자 형식이 아님",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "ID 형식 오류", value = """
                            {
                              "message": "경로 또는 쿼리 값의 타입이 올바르지 않습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "401", description = "인증 정보가 없거나 유효하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "인증 실패", value = """
                            {
                              "message": "인증에 실패했습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "403", description = "관리자 권한이 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "권한 부족", value = """
                            {
                              "message": "접근 권한이 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "404", description = "삭제할 카테고리가 존재하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "카테고리 없음", value = """
                            {
                              "message": "카테고리를 찾을 수 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "409", description = "하위 항목 또는 다른 데이터의 참조가 존재함",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "하위 항목 존재", value = """
                            {
                              "message": "하위 카테고리가 있어 처리할 수 없습니다."
                            }
                            """),
                    @ExampleObject(name = "참조 데이터 존재", value = """
                            {
                              "message": "데이터 무결성 제약 조건 위반입니다."
                            }
                            """)
            }))
    ResponseEntity<CategoryResponse> deleteCategory(Long id);
}
