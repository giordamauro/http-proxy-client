package com.http.proxy;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/organizations/{organization}")
public interface Apis {

    @GET
    @Path("/apis")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getApis();

    @GET
    @Path("/apis/{apiName}")
    @Produces(MediaType.APPLICATION_JSON)
    ApiRevisions getApiRevisions(@PathParam("apiName") String apiName);

    @GET
    @Path("/apis/{apiName}/revisions/{revisionNumber}/policies")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getApiRevisionPolicies(@PathParam("apiName") String apiName, @PathParam("revisionNumber") int revisionNumber);

    @GET
    @Path("/apis/{apiName}/revisions/{revisionNumber}/proxies")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getApiRevisionProxies(@PathParam("apiName") String apiName, @PathParam("revisionNumber") int revisionNumber);

}
