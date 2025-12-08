package service;

import java.io.IOException;

public interface FileStorage {
    void write(String path, String content, boolean append) throws IOException;
}
