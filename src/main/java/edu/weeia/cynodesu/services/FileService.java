package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.domain.ReceivedFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public interface FileService {
    public byte[] compress(MultipartFile file) throws IOException;
    public BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight);
    public byte[] getDefaultImage(Class<?> clazz) throws IOException;
    byte[] getPdfFileById(UUID fileId);
//    byte[] getFileData(Long id);
//    ReceivedFileDto getReceivedFileDto(Long id);
    public static ReceivedFile createReceivedFile(String fileName, ReceivedFile.FileGroup fileType, byte[] fileData) {
        var receivedFile = new ReceivedFile();
        receivedFile.setId(UUID.randomUUID());
        receivedFile.setData(fileData);
        receivedFile.setReceivedDate(Instant.now());
        receivedFile.setOriginalFileName(fileName);
        receivedFile.setStoredName(fileName);
        receivedFile.setFileGroup(fileType);
        return receivedFile;
    }


}
