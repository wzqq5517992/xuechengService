package com.xuecheng.manage_cms_client.serivce;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


/** 页面page的service
 * @author wzq.Jolin
 * @company none
 * @create 2019-02-27 13:58
 */
@Service
public class PageService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;



    /**
     * 保存html文件到服务器物理路径
     * @param pageId
     */
    public void  savePageToServerPath(String pageId){

        //1.根据pageId查询对应的cmsPage
        CmsPage cmsPage = this.findCmsPageById(pageId);
        //2.得到html文件id,从cmsPage中获取htmlFileId
        String htmlFileId = cmsPage.getHtmlFileId();
        //3.从gridFS中下载文件的输入流
        InputStream inputStream = this.getFileById(htmlFileId);
        if(inputStream == null){
            LOGGER.error("getFileById InputStream is null ,htmlFileId:{}",htmlFileId);
            return ;
        }
        //4.将html文件保存到服务器的物理路径上
        //4.1得到站点id
        String siteId = cmsPage.getSiteId();
        //4.2得到站点的信息
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        //得到站点的物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //得到页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //将inputStream流保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            //将物理路径放到输出流中
            fileOutputStream = new FileOutputStream(new File(pagePath));
            //输入流写到输出流中
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件id从GridFS中查询文件内容
     * @param htmlFileId
     * @return
     */
    public InputStream getFileById(String htmlFileId){
        //使用gridFsTemplate工具类 查询文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        //  gridFSBucket工具类来打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据页面(pageId)查询页面信息
     * @param pageId
     * @return
     */
    public CmsPage findCmsPageById(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 根据站点id查询站点信息
     * @param siteId
     * @return
     */
    public CmsSite findCmsSiteById(String siteId){
        Optional<CmsSite> cmsSite = cmsSiteRepository.findById(siteId);
        if(cmsSite.isPresent()){
            return cmsSite.get();
        }
        return null;
    }


}
