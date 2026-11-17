package org.example.lonatesting.service;


import org.example.lonatesting.DTO.StudentRequest;
import org.example.lonatesting.DTO.StudentResponse;
import org.example.lonatesting.Entity.Students;
import org.example.lonatesting.exceptions.DuplicateResourceException;
import org.example.lonatesting.exceptions.ResourceNotFoundException;
import org.example.lonatesting.repository.StudentRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {

        if (studentRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "A student with this email already exists"
            );
        }

        Students student = new Students();
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());

        Students savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {

        Students student = findStudentOrThrow(id);

        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {

        Students student = findStudentOrThrow(id);

        boolean emailUsedByAnotherStudent =
                studentRepository.existsByEmailAndIdNot(
                        request.email(),
                        id
                );

        if (emailUsedByAnotherStudent) {
            throw new DuplicateResourceException(
                    "Another student is already using this email"
            );
        }

        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());

        Students updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {

        Students student = findStudentOrThrow(id);

        studentRepository.delete(student);
    }

    private Students findStudentOrThrow(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with ID: " + id
                ));
    }

    private StudentResponse mapToResponse(Students student) {

        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByEmail(String email) {

        Students student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with this email: " + email));

        return mapToResponse(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByLastName(String lastName){
        Students student = studentRepository.findByLastName(lastName)
                .orElseThrow(() -> new ResourceNotFoundException("Student by " + lastName + " is not found"));

        return mapToResponse(student);
    }

    public Page<StudentResponse> getAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable).map(this::mapToResponse);
    }


}
