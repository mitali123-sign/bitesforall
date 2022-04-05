package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;
import openminder.emeal.domain.account.Account;

@Getter @Setter
public class Comment {

    private Long commentId;

    private String content;

    /** Comment : Account = N : 1 */
    private String username;

    /** Comment : Post = N : 1 */
    private Long postId;
}
