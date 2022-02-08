package my.groupId.quarkussocial.rest;

import io.quarkus.panache.common.Sort;
import my.groupId.quarkussocial.domain.model.Post;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.domain.repository.FollowerRepository;
import my.groupId.quarkussocial.domain.repository.PostRepository;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.CreatePostRequest;
import my.groupId.quarkussocial.rest.dto.PostResponse;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{idUser}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostsResource {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostsResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("idUser") Long idUser, CreatePostRequest postResquest) {
        User user = userRepository.findById((idUser));

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(postResquest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(
            @PathParam("idUser") Long idUser,
            @HeaderParam("followerId") Long followerId) {

        User user = userRepository.findById((idUser));
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(followerId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("You forgot the header followerId").build();
        }

        User follower = userRepository.findById((followerId));
        if(follower == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Inexistent followerId").build();
        }

        boolean follows = followerRepository.follows(follower, user);
        if(!follows){
            return Response.status(Response.Status.FORBIDDEN).entity("You can't see this post").build();
        }

//      postRepository.find("select post from Post where user = :user");
        var query = postRepository.find("user",
                Sort.by("dateTime", Sort.Direction.Descending) , user);
        var list = query.list();

        var postResponseList = list.stream()
                .map(post -> PostResponse.fromEntity(post)) // .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}