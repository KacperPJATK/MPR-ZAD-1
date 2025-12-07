package com.company.testing.doubles;

import model.Certification;
import repository.CertificateRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
/**
 * Stub CertificateRepository oparty na liście w pamieci, pozwalający testom przypomnien
 * sterować, które certyfikaty uznane sa za wygasające.
 */
public class CertificateRepositoryStub implements CertificateRepository {
    private final List<Certification> certifications;

    public CertificateRepositoryStub(List<Certification> certifications) {
        this.certifications = new ArrayList<>(certifications);
    }

    @Override
    public List<Certification> findExpiringBetween(LocalDate from, LocalDate to) {
        List<Certification> result = new ArrayList<>();
        for (Certification certification : certifications) {
            LocalDate expiry = certification.getExpiryDate();
            boolean withinRange = (expiry.isEqual(from) || expiry.isAfter(from))
                    && (expiry.isEqual(to) || expiry.isBefore(to));
            if (withinRange) {
                result.add(certification);
            }
        }
        return result;
    }
}
