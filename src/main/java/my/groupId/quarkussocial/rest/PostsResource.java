package my.groupId.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import my.groupId.quarkussocial.domain.model.Post;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.domain.repository.PostRepository;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.CreatePostResquest;
import my.groupId.quarkussocial.rest.dto.PostResponse;
import org.jboss.logging.annotations.Pos;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{idUser}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostsResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public  PostsResource(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("idUser") Long idUser, CreatePostResquest postResquest) {
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
    public Response listPosts(@PathParam("idUser") Long idUser) {
        User user = userRepository.findById((idUser));

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

//      postRepository.find(query: "select post from Post where user = :user");
        var query = postRepository.find("user",
                Sort.by("dateTime", Sort.Direction.Descending) , user);
        var list = query.list();

        var postResponseList = list.stream()
                .map(post -> PostResponse.fromEntity(post)) // .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
