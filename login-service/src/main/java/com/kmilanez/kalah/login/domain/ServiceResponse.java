package com.kmilanez.kalah.login.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmilanez.kalah.login.domain.entity.User;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    private ServiceResponse(User user) {
        this.user = user;
    }

    private ServiceResponse(String token) {
        this.token = token;
    }

    /* ==========================================
     * FACTORIES FOR CONSTRUCTING SPECIFIC RESPONSES
     * ========================================== */
    public static ServiceResponse replyUser(User user) {
        return new ServiceResponse(user);
    }

    public static ServiceResponse replyToken(String token) {
        return new ServiceResponse(token);
    }

}
