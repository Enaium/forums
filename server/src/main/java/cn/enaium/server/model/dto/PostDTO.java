package cn.enaium.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Enaium
 */
@Data
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Integer category;
}
