package org.b3log.symphony.service;

import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.util.Symphonys;
import org.lokra.seaweedfs.core.FileSource;
import org.lokra.seaweedfs.core.FileTemplate;
import org.lokra.seaweedfs.core.file.FileHandleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Create Time: 2019年05月20日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class SeaweedFSUtil {

    private static FileSource fileSource;
    public static final String FILE_URL = "file_url";
    private static final int MAX_NAME_LENGTH = 32;
    private static final Logger logger = LoggerFactory.getLogger(SeaweedFSUtil.class);


    private void initialConfig() throws IOException {
        fileSource.setHost(Symphonys.WEEDFS_IP);
        fileSource.setPort(Symphonys.WEEDFS_PORT);
        fileSource.startup();
    }

    public FileSource initialFileSource() throws IOException {
        if (fileSource == null) {
            fileSource = new FileSource();
            initialConfig();
        }
        return fileSource;
    }

    /**
     * 上传文件流到服务器
     *
     * @param fileName
     * @param in
     * @throws IOException
     */
    public Map<String, String> uploadFile(String fileName, InputStream in) throws IOException {
        initialFileSource();
        if (fileName.length() > MAX_NAME_LENGTH) {
            String suffix = fileName.substring(fileName.lastIndexOf(",") + 1);
            fileName = fileName.substring(0, MAX_NAME_LENGTH) + suffix;
        }
        Map<String, String> resultMap = new HashMap<>();
        fileName = fileName.replaceAll(" ", "");
        FileTemplate template = new FileTemplate(fileSource.getConnection());
        FileHandleStatus fileHandleStatus = template.saveFileByStream(fileName, in);
        String fileUrl = template.getFileUrl(fileHandleStatus.getFileId());
        logger.debug("SeaweedFSUtil.uploadFile FILE_URL={}", fileUrl);
        fileUrl = fileUrl.replace(",", "/");
        resultMap.put(FILE_URL, fileUrl.replace(",", "/") + "/" + getFileName(fileName));
        return resultMap;
    }

    private String getFileName(String path) {
        if (!path.contains("/")) {
            return path;
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
