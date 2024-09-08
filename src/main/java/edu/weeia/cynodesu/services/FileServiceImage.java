package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.domain.BreedingFacility;
import edu.weeia.cynodesu.domain.Dog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class FileServiceImage implements FileService {
    @Override
    public byte[] compress(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = resize(bufferedImage,200, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        return baos.toByteArray();
    }

    @Override
    public BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    @Override
    public byte[] getDefaultImage(Class<?> clazz) throws IOException, IllegalArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Correctly using the static inner class with its factory method
        BufferedImage defaultImage = DefaultImageBuilder.forClass(clazz).build();

        // Resize the image if needed
        var scaledImage = resize(defaultImage, 200, 200);

        // Write the image to the output stream
        ImageIO.write(scaledImage, "jpg", baos);

        return baos.toByteArray();
    }

    // Static inner class for building default images
    private static class DefaultImageBuilder {

        // Static map to hold default image paths
        private static final Map<Class<?>, String> defaultImagesMap = new HashMap<>();

        static {
            // Initialize the map with default image paths
            defaultImagesMap.put(Dog.class, "/static/images/default-dog.png");
            defaultImagesMap.put(AppUser.class, "/static/images/default-user.jpg");
            defaultImagesMap.put(BreedingFacility.class, "/static/images/default-facility.jpg");
            // Add more mappings as needed
        }

        private Class<?> targetClass; // The class for which we want the default image

        // Private constructor to enforce usage of the static inner class
        private DefaultImageBuilder(Class<?> targetClass) {
            this.targetClass = targetClass;
        }

        // Factory method to create a new builder instance
        public static DefaultImageBuilder forClass(Class<?> targetClass) {
            return new DefaultImageBuilder(targetClass);
        }

        // Method to build and return the default image
        public BufferedImage build() throws IOException {
            String defaultImagePath = defaultImagesMap.getOrDefault(targetClass, "/static/images/default-generic.jpg");
            Objects.requireNonNull(defaultImagePath, "Default image path not found for class: " + targetClass);

            // Load the image from the path
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(defaultImagePath)));
        }
    }
}
