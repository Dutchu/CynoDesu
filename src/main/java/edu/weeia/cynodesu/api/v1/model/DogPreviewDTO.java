package edu.weeia.cynodesu.api.v1.model;

import edu.weeia.cynodesu.domain.DogStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class DogPreviewDTO {

    private Long id;

    private String name;

    private DogStatus status;

    private String content;

    private Long userId;

    private String username;

    private Instant createdDate;

    private List<FileInfo> files = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class FileInfo {
        UUID id;
        String name;
    }

}