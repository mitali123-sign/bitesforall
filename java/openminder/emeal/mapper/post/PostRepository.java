package openminder.emeal.mapper.post;

import openminder.emeal.domain.file.Picture;
import openminder.emeal.domain.post.Menu;
import openminder.emeal.domain.post.Nutrient;
import openminder.emeal.domain.post.Post;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PostRepository {

    int insertPost(Post post);

    int insertPicture(Picture picture);

    int insertMenu(Menu menu);

    int insertNutrient(Nutrient nutrient);

    Post selectOnePost(Long postId);

    List<Post> selectPosts(String postType);

    List<Post> selectRecentPosts(Long numPosts);

    List<Post> selectUserPosts(String username);

    List<Menu> selectMenus(Long postId);

    List<Nutrient> selectNutrients(String username);

    List<Nutrient> selectOneDayNutrient(String username);

    void updateAccountInfo(Post post);

}
