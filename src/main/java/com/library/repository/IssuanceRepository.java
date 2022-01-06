package com.library.repository;

import com.library.domain.Issuance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    void deleteById(Long id);
    List<Issuance> findByStudentUsn(String usn);
    Issuance getById(Long id);
    Issuance getByBookId(Long id);

}
