package openminder.emeal.service.post;

import lombok.RequiredArgsConstructor;
import openminder.emeal.domain.file.Picture;
import openminder.emeal.domain.file.UploadFile;
import openminder.emeal.domain.post.Menu;
import openminder.emeal.domain.post.Nutrient;
import openminder.emeal.domain.post.Post;
import openminder.emeal.domain.post.PostType;
import openminder.emeal.mapper.post.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    final PostRepository postRepository;

    public void uploadPost(Post post) {
        postRepository.insertPost(post);
    }

    public void uploadPicture(Picture picture) {
        postRepository.insertPicture(picture);
    }

    public void uploadMenu(Menu menu) {
        postRepository.insertMenu(menu);
    }

    public void uploadNutrient(Nutrient nutrient) {
        postRepository.insertNutrient(nutrient);
    }

    public Post findOnePost(Long postId) {
        return postRepository.selectOnePost(postId);
    }

    public List<Menu> findMenusByPostId(Long postId) {
        return postRepository.selectMenus(postId);
    }

    public List<Nutrient> findNutrientByUsername(String username) {
        return postRepository.selectNutrients(username);
    }

    public List<Nutrient> findOneDayNutrientByUsername(String username) {
        return postRepository.selectOneDayNutrient(username);
    }


    public List<Post> findPosts(String postState) {
        return postRepository.selectPosts(postState);
    }

    public List<Post> findRecentPosts(Long numPosts) { return postRepository.selectRecentPosts(numPosts); }

    public List<Post> findUserPosts(String username) {
        return postRepository.selectUserPosts(username);
    }

    public void updatePostAccountInfo(Post post) {
        postRepository.updateAccountInfo(post);
    }
}
