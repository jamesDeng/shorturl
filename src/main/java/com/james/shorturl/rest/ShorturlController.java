/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.james.shorturl.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.james.shorturl.biz.ShorturlService;
import com.james.shorturl.rest.request.NewurlRequest;
import com.james.shorturl.rest.response.NewurlResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yougao.dw
 * @date 2021/05/23
 */
@RestController
public class ShorturlController {

    @Autowired
    private ShorturlService shorturlService;

    @RequestMapping(value = "/newurl", method = RequestMethod.POST)
    public NewurlResponse newurl(@RequestBody @Validated NewurlRequest request) {

        String shortURL = shorturlService.addShortUrl(request.getUrl());

        NewurlResponse response = new NewurlResponse();
        response.setUrl(request.getUrl());
        response.setShortenUrl(shortURL);
        return response;
    }


    @RequestMapping(value = "/{shortStr:[a-zA-Z0-9]{8}}")
    public ModelAndView redirect(@PathVariable String shortStr, HttpServletResponse response) throws IOException {

        String originaUrl = shorturlService.getOriginalUrl(shortStr);
        if(StringUtils.isBlank(originaUrl)){
            throw new ResourceNotFoundException();
        }
        response.sendRedirect(originaUrl);
        return null;
    }
}
