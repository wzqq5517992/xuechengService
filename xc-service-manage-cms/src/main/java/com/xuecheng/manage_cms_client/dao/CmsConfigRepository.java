package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-02-18 17:27
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
