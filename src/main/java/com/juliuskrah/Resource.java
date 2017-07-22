package com.juliuskrah;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "resource")
public class Resource {
    private Long id;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}