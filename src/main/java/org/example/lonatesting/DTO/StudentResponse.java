package org.example.lonatesting.DTO;

public record StudentResponse(Long id,
                              String firstName,
                              String lastName,
                              String email,
                              Long departmentId,
                              String departmentName) {
}
