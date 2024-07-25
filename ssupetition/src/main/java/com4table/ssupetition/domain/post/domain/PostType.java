package com4table.ssupetition.domain.post.domain;

import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
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
public class PostType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTypeId;

    @Enumerated(EnumType.STRING)
    private Type postTypeName;
}
