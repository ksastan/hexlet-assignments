package exercise;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    // Получение всех постов
    // Получение всех постов с пейджингом
    @GetMapping("/posts")
    public List<Post> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int limit) {
        int skip = (page - 1) * limit;
        return posts.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Получение поста по ID
    @GetMapping("/posts/{id}")
    public Optional<Post> getPostById(@PathVariable String id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    // Создание нового поста
    @PostMapping("/posts")
    public Post createPost(@RequestBody Post newPost) {
        posts.add(newPost);
        return newPost;
    }

    // Обновление поста по ID
    @PutMapping("/posts/{id}")
    public Optional<Post> updatePost(@PathVariable String id, @RequestBody Post updatedPost) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(post -> {
                    post.setTitle(updatedPost.getTitle());
                    post.setBody(updatedPost.getBody());
                    return post;
                });
    }

    // Удаление поста по ID
    @DeleteMapping("/posts/{id}")
    public Optional<Post> deletePost(@PathVariable String id) {
        Optional<Post> postToDelete = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
        postToDelete.ifPresent(posts::remove);
        return postToDelete;
    }
    // END
}
