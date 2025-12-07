package com.company.testing.doubles;

import service.FileStorage;
@SuppressWarnings("all")
/**
 * Mock FileStorage weryfikujący, czy zapis nastąpił na oczekiwanej scieżce
 * i z właściwą flagą append.
 */
public class FileStorageMock implements FileStorage {
    private final String expectedPath;
    private final boolean expectedAppend;
    private int callCount = 0;

    public FileStorageMock(String expectedPath, boolean expectedAppend) {
        this.expectedPath = expectedPath;
        this.expectedAppend = expectedAppend;
    }

    @Override
    public void write(String path, String content, boolean append) {
        callCount++;
        if (!expectedPath.equals(path)) {
            throw new AssertionError("Unexpected path: " + path);
        }
        if (expectedAppend != append) {
            throw new AssertionError("Unexpected append flag");
        }
    }

    public void verify() {
        if (callCount != 1) {
            throw new AssertionError("Expected one write, got: " + callCount);
        }
    }
}
