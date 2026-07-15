package com.LiveFullstack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebateTopic {

    private Long id;
    private String title;
    private String description;
    private String optionA;
    private String optionB;
    private Boolean active;
    private java.time.LocalDateTime createdAt;
}
