package openminder.emeal.domain.file;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadFile {

    private Long fileId;

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private Long size;

    /** Avatar DB foreign key */
    private String username;

    public UploadFile() {

    }

    public UploadFile(String fileName, String fileDownloadUri, String fileType, Long size, String username) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.username = username;
    }

}
