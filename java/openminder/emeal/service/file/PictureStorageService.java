package openminder.emeal.service.file;

import openminder.emeal.config.file.FileStorageProperties;
import openminder.emeal.exception.FileDownloadException;
import openminder.emeal.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PictureStorageService {

    private final Path pictureStorageLocation;

    @Autowired
    public PictureStorageService(FileStorageProperties fileStorageProperties) {
        this.pictureStorageLocation = Paths.get(fileStorageProperties.getUploadPictureDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.pictureStorageLocation);
        } catch (Exception e) {
            throw new FileUploadException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storePicture(MultipartFile picture) {
        String pictureName = StringUtils.cleanPath(picture.getOriginalFilename());

        try {
            if (pictureName.contains("..")) {
                throw new FileUploadException("Sorry! Filename contains invalid path sequence " + pictureName);
            }
            Path targetLocation = this.pictureStorageLocation.resolve(pictureName);
            Files.copy(picture.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return pictureName;
        } catch (Exception e) {
            throw new FileUploadException("Could not store file " + pictureName + ". Please try again", e);
        }
    }

    public Resource loadFileAsResource(String pictureName) {
        try {
            Path picturePath = this.pictureStorageLocation.resolve(pictureName).normalize();
            Resource resource = new UrlResource(picturePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
//                throw new FileDownloadException("File not found: " + pictureName);
                return new UrlResource("http://192.168.0.12:8080/downloadPicture/mike.png");
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException("File not found => " + pictureName, e);
        }
    }
}
