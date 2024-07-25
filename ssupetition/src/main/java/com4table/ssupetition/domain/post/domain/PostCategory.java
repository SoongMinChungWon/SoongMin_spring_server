package com4table.ssupetition.domain.post.domain;

import com4table.ssupetition.domain.post.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCategoryId;

    @Enumerated(EnumType.STRING)
    private Category postCategoryName;

}
