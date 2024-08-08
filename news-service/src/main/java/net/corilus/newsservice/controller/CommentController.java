package net.corilus.newsservice.controller;

import net.corilus.newsservice.dto.CommentDto;
import net.corilus.newsservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/addComment/{idPublication}")
    public String addComment(@RequestBody CommentDto commentDTO,@PathVariable("idPublication") Long idPublication) {
        return commentService.addComment(commentDTO, idPublication);
    }
    @DeleteMapping("/deleteComment")
    public void deleteComment(@RequestParam("commentId") Long commentId) {
        commentService.deleteComment(commentId);
    }
    @PutMapping("updateComment")
    public String updateComment(@ModelAttribute CommentDto commentDTO,@RequestParam("idComment") Long idComment) {
        return commentService.updateComment(commentDTO, idComment);
    }
    @GetMapping("/getCommentsById/{idPublication}")
    public List<CommentDto> getCommentsById(@PathVariable("idPublication") Long idPublication) {
       return commentService.getCommentsById(idPublication);
    }


    }
