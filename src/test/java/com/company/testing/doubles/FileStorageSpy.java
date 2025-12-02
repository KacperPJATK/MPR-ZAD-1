package com.company.testing.doubles;

import service.FileStorage;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/**
 * Szpieg FileStorage zbierajacy wszystkie proby zapisu, aby testy mogly sprawdzic
 * sciezki, zawartosc oraz tryb append dla wywolan.
 */
public class FileStorageSpy implements FileStorage {
    private final List<Write> writes = new ArrayList<>();

    @Override
    public void write(String path, String content, boolean append) {
        writes.add(new Write(path, content, append));
    }

    public List<Write> writes() {
        return new ArrayList<>(writes);
    }

    public record Write(String path, String content, boolean append) {
    }
}
