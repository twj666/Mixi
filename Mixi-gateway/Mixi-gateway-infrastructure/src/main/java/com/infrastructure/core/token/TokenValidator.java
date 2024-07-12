package com.infrastructure.core.token;

import com.mixi.common.pojo.ApiInfo;
import com.infrastructure.pojo.UserInfo;
import org.springframework.web.server.ServerWebExchange;

/**
 * 描述: 定义了Token验证的标准方法
 * @author suifeng
 * 日期: 2024/7/12
 */
public interface TokenValidator {


    /**
     * 从请求头中提取Token。
     */
    String extractTokenFromHeader(ServerWebExchange exchange);

    /**
     * 验证Token是否合法。
     */
    boolean isTokenValid(String token);

    /**
     * 从Token中提取用户信息。
     */
    UserInfo extractUserInfoFromToken(String token);

    /**
     * 检查用户是否具有所需的角色权限。
     */
    boolean hasRequiredRoles(int[] userRoles, int[] apiRoles);
}