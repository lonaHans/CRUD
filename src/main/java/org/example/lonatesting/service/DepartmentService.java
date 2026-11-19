package org.example.lonatesting.service;

import org.example.lonatesting.DTO.DepartmentRequest;
import org.example.lonatesting.DTO.DepartmentResponse;
import org.example.lonatesting.Entity.Department;
import org.example.lonatesting.exceptions.ResourceNotFoundException;
import org.example.lonatesting.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request){
        Department department =  new Department();
        department.setName(request.name());
        department.setDescription(request.description());

        Department savedDepartment = departmentRepository.save(department);
        return mapToResponse(savedDepartment);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments(){

        return departmentRepository.findAll().stream().map(this::mapToResponse).toList();

    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found: " + id
                        ));

        return mapToResponse(department);
    }

    private DepartmentResponse mapToResponse(Department department){
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription()

        );
    }
}
