package com.threadjava.post;


import com.threadjava.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.threadjava.auth.TokenService.getUserId;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    @Autowired
    private PostsService postsService;
    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping
    public List<PostDetailsDto> get(@RequestParam(defaultValue="0") Integer from,
                                 @RequestParam(defaultValue="10") Integer count,
                                 @RequestParam(required = false) UUID userId) {
        return postsService.getAllPosts(from, count, userId);
    }

    @GetMapping("/{id}")
    public PostDetailsDto get(@PathVariable UUID id) {
        return postsService.getPostById(id);
    }

    @PostMapping
    public PostDetailsDto post(@RequestBody PostDetailsDto postDto) {
        var item = postsService.create(postDto, getUserId());
        template.convertAndSend("/topic/new_post", item);
        return item;
    }

    @PutMapping("/react")
    public Optional<ResponcePostReactionDto> setReaction(@RequestBody ReceivedPostReactionDto postReaction){
        var reaction = postsService.setReaction(getUserId(), postReaction);

        if (reaction.isPresent() && reaction.get().userId != getUserId()) {
            // notify a user if someone (not himself) liked his post
            template.convertAndSend("/topic/like", "Your post was liked!");
        }
        return reaction;
    }
}
