/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.james.shorturl.rest.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author yougao.dw
 * @date 2021/05/23
 */
@Data
public class NewurlRequest {
    @NotBlank
    private String url;
}
