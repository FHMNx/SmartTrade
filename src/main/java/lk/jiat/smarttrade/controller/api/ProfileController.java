package lk.jiat.smarttrade.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smarttrade.annotation.IsUser;
import lk.jiat.smarttrade.service.UserService;

@Path("/profiles")
public class ProfileController {

    @IsUser
    @Path("/update-profil")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(String jsonData, @Context HttpServletRequest request) {
        return Response.ok().entity("").build();
    }


    @IsUser
    @Path("/user-profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadUserProfile(@Context HttpServletRequest request) {
        String responseJson = new UserService().userProfile(request);
        return Response.ok().entity(responseJson).build();
    }

}
