package com.wercent.hero.common.netity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户之间关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRelation {

    /**
     * 关系编号
     */
    private String relationId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 目标用户编号
     */
    private String targetUserId;

    /**
     * 备注
     */
    private String remarks;
}
