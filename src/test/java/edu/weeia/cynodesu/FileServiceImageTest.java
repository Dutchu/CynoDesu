package edu.weeia.cynodesu;

import edu.weeia.cynodesu.services.FileServiceImage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceImageTest {

    @MockBean
    private FileServiceImage fileServiceImage;

    @Test
    public void testGetDefaultImage() throws IOException {
        // Arrange
        InputStream mockInputStream = new ClassPathResource("/static/default-dog.jpg").getInputStream();
        Mockito.when(fileServiceImage.getClass().getResourceAsStream("/static/default-dog.jpg")).thenReturn(mockInputStream);

        // Act
        byte[] result = fileServiceImage.getDefaultImage();

        // Assert
        assertNotNull(result, "The returned byte array should not be null");
        assertTrue(result.length > 0, "The returned byte array should not be empty");
    }
}