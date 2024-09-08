package edu.weeia.cynodesu.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ReceivedFile {

    @Id
    @Column(columnDefinition = "uuid")
    UUID id;

    Instant receivedDate;
    String originalFileName;

    String storedName;

    //TODO: Look into possibility to make it configurable from app.properties
    @Enumerated(EnumType.STRING)
    FileGroup fileGroup;

    public ReceivedFile(FileGroup group, String originalFileName, String storedName) {
        this.fileGroup = group;
        this.originalFileName = originalFileName;
        this.storedName = storedName;
        this.id = UUID.randomUUID();
    }

    public enum FileGroup {
        NOTE_ATTACHMENT,
        PICTURE
    }
}