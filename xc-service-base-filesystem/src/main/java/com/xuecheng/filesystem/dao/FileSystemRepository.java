package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-06 09:03
 */
public interface FileSystemRepository extends MongoRepository< FileSystem,String> {
}
