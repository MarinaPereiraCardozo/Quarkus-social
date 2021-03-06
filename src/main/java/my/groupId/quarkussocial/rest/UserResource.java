package my.groupId.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.CreateUserRequest;
import my.groupId.quarkussocial.rest.dto.ResponseError;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new user")
    @APIResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = User.class)))
    public Response createUser(CreateUserRequest userRequest){

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user); //usado para salvar no db
//        outros exemplos
//        user.delete();
//        User.count();
//        User.delete(query:"delete from User where age < 18")

        return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();
    }

    @GET
    @Operation(summary = "List all users")
    @APIResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = User.class)))
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Transactional
    @Operation(summary = "Delete an user")
    @APIResponse(
            responseCode = "204",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = User.class)))
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id){

        User user = repository.findById(id);

        if(user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Transactional
    @Operation(summary = "Update an user")
    @APIResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = User.class)))
    @Path("{id}")
    public Response putUser(@PathParam("id") Long id, CreateUserRequest userData){

        User user = repository.findById(id);

        if(user != null) {
            user.setAge(userData.getAge());
            user.setName(userData.getName());
            return Response.status(Response.Status.OK).entity(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
