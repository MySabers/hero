package com.wercent.hero.common.netity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和组之间的关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRelation {

    /**
     * 关系编号
     */
    private String id;

    /**
     * 组编号
     */
    private String groupId;

    /**
     * 用户编号
     */
    private String userId;

}
