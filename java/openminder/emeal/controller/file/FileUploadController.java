package openminder.emeal.controller.file;

import lombok.RequiredArgsConstructor;
import openminder.emeal.domain.file.UploadFile;
import openminder.emeal.service.account.AccountService;
import openminder.emeal.service.file.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    final FileStorageService service;
    final AccountService accountService;


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        System.out.println("username: " + username);
        String fileName = service.storeFile(file);

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();

        System.out.println("fileName: " + fileName);

        String fileDownloadUri = "/downloadFile/" + fileName;

        UploadFile uploadFile = new UploadFile(fileName, fileDownloadUri, file.getContentType(), file.getSize(), username);
        accountService.updateAvatar(uploadFile);

//        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return fileDownloadUri;
    }

    /** Not used in uploading Avatar file */
//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

    @GetMapping("downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

}
