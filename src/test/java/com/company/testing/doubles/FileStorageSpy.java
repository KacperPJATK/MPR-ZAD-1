package com.company.testing.doubles;

import service.FileStorage;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/**
 * Szpieg FileStorage zbierający wszystkie próby zapisu, aby testy mogły sprawdzić
 * scieżki, zawartość oraz tryb append dla wywołań.
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
