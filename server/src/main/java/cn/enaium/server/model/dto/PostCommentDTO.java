package cn.enaium.server.model.dto;

import lombok.Data;

/**
 * @author Enaium
 */
@Data
public class PostCommentDTO {
    private Long id;
    private Long postId;
    private String comment;
}
