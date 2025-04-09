package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.dto.ReaderDTO;
import org.acme.entity.Reader;

@ApplicationScoped
public class ReaderService {

    @Transactional
    public Reader createReader(ReaderDTO dto){
        boolean exists = Reader.find("CPF", dto.CPF).firstResultOptional().isPresent();
        if(exists) {
            throw new IllegalArgumentException("Reader already exists");
        }

        Reader reader = new Reader();

        reader.name = dto.name;
        reader.CPF = dto.CPF;
        reader.email = dto.email;

        reader.persist();
        return reader;
    }

    @Transactional
    public Reader updateReader(Long id, ReaderDTO dto) {
        Reader reader = Reader.findById(id);
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found");
        }

        reader.name = dto.name;
        reader.CPF = dto.CPF;
        reader.email = dto.email;

        reader.persist();
        return reader;
    }
}
