package net.corilus.newsservice.service;

import net.corilus.newsservice.dto.CommentDto;
import net.corilus.newsservice.dto.ImmutableCommentDto;
import net.corilus.newsservice.entity.Comment;
import net.corilus.newsservice.entity.Publication;
import net.corilus.newsservice.repository.CommentRepository;
import net.corilus.newsservice.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    PublicationRepository publicationRepository;
    @Autowired
    CommentRepository commentRepository;
    @Override
    public String addComment(CommentDto commentDTO, Long idPublication) {
        Comment comment =convertToEntity(commentDTO, idPublication);
        commentRepository.save(comment);
        return "OK";
    }

    @Override
    public void deleteComment(Long commentId) {
Comment comment = commentRepository.findById(commentId).orElse(null);
commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> getComments() {
        return List.of();
    }

    @Override
    public List<CommentDto> getCommentsById(Long idPublication) {
Publication publication = publicationRepository.findById(idPublication).orElse(null);
List<Comment> comments = commentRepository.findByPublication(publication);
        return comments.stream()
                .map(comment -> {
                    return convertToDto(comment);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String updateComment(CommentDto commentDTO, Long idComment) {
        Comment comment = commentRepository.findById(idComment).orElse(null);
        comment.setDescription(commentDTO.getDescription());
        comment.setCreationdate(new Date());
        commentRepository.save(comment);
        return "OK";
    }

    @Override
    public Comment convertToEntity(CommentDto commentDto, Long idPublication) {
        Publication publication = publicationRepository.findById(idPublication).orElse(null);
        return Comment.builder()
                .description(commentDto.getDescription())
                .creationdate(new Date())
                .publication(publication)
                .build();    }

    @Override
    public CommentDto convertToDto(Comment comment) {
        return ImmutableCommentDto.builder()
                .description(comment.getDescription())
                .creationdate(comment.getCreationdate())
                .author(comment.getAuthorId())
                .id(comment.getId())
                .build();
    }
}
