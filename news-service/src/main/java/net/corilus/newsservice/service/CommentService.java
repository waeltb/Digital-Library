package net.corilus.newsservice.service;


import net.corilus.newsservice.dto.CommentDto;
import net.corilus.newsservice.entity.Comment;

import java.util.List;

public interface CommentService {
String addComment(CommentDto commentDTO,Long idPublication);
void deleteComment(Long commentId);
List<CommentDto> getComments();
List<CommentDto> getCommentsById(Long idPublication);
String updateComment(CommentDto commentDTO,Long idComment);
Comment convertToEntity(CommentDto commentDto, Long idPublication);
CommentDto convertToDto(Comment comment);


}
