package com.personal.assistant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserDto {
    private String id;
    private String name;
    private long contactNumber;
    private String email;
    private String password;
    private String address; 
    private String status;
}
