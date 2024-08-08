package net.corilus.newsservice.repository;

import net.corilus.newsservice.entity.Comment;
import net.corilus.newsservice.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
List<Comment> findByPublication(Publication publication);

}
