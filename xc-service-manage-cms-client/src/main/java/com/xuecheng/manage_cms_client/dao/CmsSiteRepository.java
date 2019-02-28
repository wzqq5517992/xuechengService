package com.xuecheng.manage_cms_client.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**站点数据层
 * @author wzq.Jolin
 * @company none
 * @create 2019-02-01 13:58
 */

public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {

}
