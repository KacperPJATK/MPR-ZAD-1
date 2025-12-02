package service;

public interface FileStorage {
    void write(String path, String content, boolean append);
}
