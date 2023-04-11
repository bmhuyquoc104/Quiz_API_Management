package com.example.quiz_api_management.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class PaginationReturn {
    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
    private boolean success;
    private Object data;
    private int currentPage;
    private int totalPages;
    private int numberElementsInPage;
    private long totalElements;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
}
