package org.example.lonatesting.repository;

import org.example.lonatesting.Entity.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Students, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Students> findByEmail(String email);

    Optional<Students> findByLastName(String lastName);

    Page<Students> findAll(Pageable pageable);

    List<Students> findByDepartmentId(Long departmentId);
}
