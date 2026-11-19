package org.example.lonatesting.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "A description is required")
        @Size(max = 100, message = "The description must not exceed 100 characters")
        String description

        ) {
}
