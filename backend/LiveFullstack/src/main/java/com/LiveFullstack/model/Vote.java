package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    private Long id;
    private Long topicId;
    private Long userId;
    private String option;
    private java.time.LocalDateTime createdAt;
}
