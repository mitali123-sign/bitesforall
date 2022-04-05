package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Menu {

    private Long menuId;

    private String menuName;

    /** Menu : Nutrient = 1 : 1 */
    private Nutrient nutrient;

    /** Menu : Post = N : 1 */
    private Long postId;

    public Menu() {

    }

    public Menu(String menuName, Long postId) {
        this.menuName = menuName;
        this.postId = postId;
    }
}
