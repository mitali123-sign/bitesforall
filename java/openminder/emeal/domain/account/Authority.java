package openminder.emeal.domain.account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Authority {

    private Long authorityId;

    private AuthorityName authorityName;

    private String username;

    public Authority() {
    }

    public Authority(AuthorityName authorityName, String username) {
        this.authorityName = authorityName;
        this.username = username;
    }
}
