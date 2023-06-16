package me.pvaldes.blog.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import me.pvaldes.blog.controller.DTO.BlogpostRequest
import me.pvaldes.blog.entities.Blogpost
import me.pvaldes.blog.service.BlogpostService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@Tag(name = "Blogpost", description = "Blogpost APIs")
@RestController
@RequestMapping("/api/V1/blogpost")
class BlogpostController(val blogService: BlogpostService){

    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "Retrieve a blogpost by id",
        description = "Get a Blogpost object by specifying its id. The response is Blogpost object with id, title, description and published status.",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "202", description = "Successful Operation"),
        ApiResponse(responseCode = "404", description = "Blogpost not found"),
        ApiResponse(responseCode = "500", description = "Server error")
    ])
    @GetMapping("/{id}")
    fun getBlogpost(@PathVariable id:String): ResponseEntity<Blogpost> {
        logger.info("Get Blogpost for id [$id]")
        return ResponseEntity<Blogpost>(blogService.getBlogpost(id), HttpStatus.OK)
    }

    @Operation(
        summary = "Retrieve all blogposts",
        description = "Get all Blogpost objects. The response is a Blogpost object list.",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "202", description = "Successful Operation"),
        ApiResponse(responseCode = "404", description = "Blogposts not found"),
        ApiResponse(responseCode = "500", description = "Server error")
    ])
    @GetMapping
    fun getAllBlogposts():ResponseEntity<Iterable<Blogpost>> {
        logger.info("Get All Blogposts")
        return ResponseEntity<Iterable<Blogpost>>(blogService.getAllBlogposts(), HttpStatus.OK)
    }

    @Operation(
        summary = "Add a new blogpost",
        description = "Adds a new Blogpost object. The response is the added Blogpost object.",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "202", description = "Successful Operation"),
        ApiResponse(responseCode = "404", description = "Blogposts not found"),
        ApiResponse(responseCode = "500", description = "Server error")
    ])
    @PostMapping
    fun addBlogpost(@RequestBody blogpostRequest: BlogpostRequest): ResponseEntity<Blogpost>{
        logger.info("Add new Blogpost")
        return ResponseEntity<Blogpost>(blogService.addBlogpost(blogpostRequest), HttpStatus.OK)
    }

    @Operation(
        summary = "Update a blogpost by id",
        description = "Updates a Blogpost object by specifying its id. The response is the updated Blogpost object.",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "202", description = "Successful Operation", useReturnTypeSchema = true),
        ApiResponse(responseCode = "404", description = "Blogposts not found"),
        ApiResponse(responseCode = "500", description = "Server error")
    ])
    @PutMapping("/{id}")
    fun updateBlogpost(@PathVariable id: String, @RequestBody blogpostRequest : BlogpostRequest) : ResponseEntity<Blogpost> {

        val response = blogService.updateBlogpost(id, blogpostRequest)

        return if (response == null) {
            ResponseEntity<Blogpost>(null, HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity<Blogpost>(response, HttpStatus.OK)
        }
    }

    @Operation(
        summary = "Delete a blogpost by id",
        description = "Deletes a Blogpost object by specifying its id. The response is the updated Blogpost object.",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "202", description = "Successful Operation", useReturnTypeSchema = true),
        ApiResponse(responseCode = "404", description = "Blogposts not found"),
        ApiResponse(responseCode = "500", description = "Server error")
    ])
    @DeleteMapping("/{id}")
    fun deleteBlogPost(@PathVariable id: String): ResponseEntity<String> {
        logger.info("Delete Blogpost [${id}]")
        val response = blogService.deleteBlogpost(id)
        return if (response == null) {
            ResponseEntity<String>(null, HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity<String>(response, HttpStatus.OK)
        }
    }
}