package com4table.ssupetition.domain.base_time;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTimeEntity {

    // Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 조회한 Entity 값을 변경할 떄 시간이 자동 저장됩니다.
    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;
}
