package com.danichagas.qrcode.generator.storage;

public interface StoragePort {
    String uploadFile(byte[] fileData, String fileName, String contentType);
}