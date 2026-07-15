package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiContent {

    private Long id;
    private Long streamId;
    private String contentType;
    private String content;
    private java.time.LocalDateTime createdAt;
}
