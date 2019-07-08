package com.kmilanez.kalah.gateway.integration.service.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResponseForLoginService {
    private IntegrationUser user;
}
