package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;
import openminder.emeal.domain.account.Account;
import openminder.emeal.domain.file.Picture;
import openminder.emeal.domain.file.UploadFileResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class Post {

    private Long postId;

    private String content;

    private String userId;

    private PostType postType;

    private MealType mealType;

    private LocalDateTime insertTime;

    private LocalDateTime updateTime;

    private String avatarDownloadUri;

    /** Post : Account = N : 1 */
    private String username;

    /** Post : Picture = 1 : N */
    private List<Picture> pictures;

    /** Post : Menu = 1 : N */
    private List<Menu> menus;

    /** Post : ThumbsUp = 1 : N */
    private List<ThumbsUp> thumbsUpList;

    /** Post : Comment = 1 : N */
    private List<Comment> comments;

}
