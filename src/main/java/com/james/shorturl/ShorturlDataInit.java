/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.james.shorturl;

import com.james.shorturl.biz.ShorturlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 用于初始换 Sequence
 * @author yougao.dw
 * @date 2021/05/23
 */
@Component
public class ShorturlDataInit implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ShorturlDataInit.class);
    @Autowired
    private ShorturlService shorturlService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("..................init sequence..................");
        shorturlService.initSequence();
    }
}
