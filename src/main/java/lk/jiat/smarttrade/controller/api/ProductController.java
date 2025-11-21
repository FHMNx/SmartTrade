package lk.jiat.smarttrade.controller.api;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smarttrade.dto.ProductDTO;
import lk.jiat.smarttrade.util.AppUtil;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.util.List;

@Path("/products")
public class ProductController {
    @Path("/save-product")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveProduct(@FormDataParam("product") String productJson,
                                @FormDataParam("images[]") List<FormDataBodyPart> images) {
        ProductDTO productDTO = AppUtil.GSON.fromJson(productJson, ProductDTO.class);
        System.out.println(productDTO.getTitle());
        return Response.ok().entity("").build();
    }
}
