package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;
import openminder.emeal.domain.account.Account;

@Getter @Setter
public class ThumbsUp {

    private Long thumbsUpId;

    /** Account : Post = N : 1 */
    private String username;

    /** ThumbsUp : Post = N : 1 */
    private Long postId;
}
