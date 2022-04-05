package openminder.emeal.domain.file;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Picture {

    private Long pictureId;

    private String pictureName;

    private String pictureDownloadUri;

    private String pictureType;

    private Long size;

    private Long postId;

    public Picture() {

    }

    public Picture(String pictureName, String pictureDownloadUri, String pictureType, Long size, Long postId) {
        this.pictureName = pictureName;
        this.pictureDownloadUri = pictureDownloadUri;
        this.pictureType = pictureType;
        this.size = size;
        this.postId = postId;
    }

}
