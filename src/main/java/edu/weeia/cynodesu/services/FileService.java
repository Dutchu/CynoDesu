package edu.weeia.cynodesu.services;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FileService {
    public byte[] compress(MultipartFile file) throws IOException;
    public BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight);
    public byte[] getDefaultImage(Class<?> clazz) throws IOException;
}
