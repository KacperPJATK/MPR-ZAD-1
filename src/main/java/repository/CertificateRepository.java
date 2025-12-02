package repository;

import model.Certification;

import java.time.LocalDate;
import java.util.List;

public interface CertificateRepository {
    List<Certification> findExpiringBetween(LocalDate from, LocalDate to);
}
