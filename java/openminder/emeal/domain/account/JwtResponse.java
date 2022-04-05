package openminder.emeal.domain.account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String fileDownloadUri;
    private String userId;
    private String goal;
    private Long height;
    private Long weight;
    private Long age;

    public JwtResponse(String accessToken, String fileDownloadUri, String userId, String goal, Long height, Long weight, Long age) {
        this.accessToken = accessToken;
        this.fileDownloadUri = fileDownloadUri;
        this.userId = userId;
        this.goal = goal;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

}
