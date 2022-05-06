package com.tree.clouds.assessment.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class FastDFSUtil {

    /**
     * JAVA 运行时的临时目录
     */
    public static final String TMP_HOME = System.getProperty("java.io.tmpdir");

    @Autowired
    private FastFileStorageClient storageClient;

    public Map<String, String> uploadFile(byte[] bytes, String fileName) {
        InputStream is = new ByteArrayInputStream(bytes);
        return uploadFile(is, bytes.length, fileName, null);
    }

    public Map<String, String> uploadFile(File file, String fileName) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return uploadFile(fis, file.length(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> uploadFile(InputStream inputStream, long fileSize, String fileExtName,
                                          Set<MateData> metaDataSet) {
        StorePath storePath = storageClient.uploadFile(inputStream, fileSize, fileExtName, metaDataSet);
        Map<String, String> map = new HashMap<>(4);
        map.put("groupName", storePath.getGroup());
        map.put("fileId", storePath.getPath());
        return map;
    }

    public Map<String, String> uploadFile(MultipartFile file, String fileName) throws IOException {
        StorePath storePath = storageClient.uploadFile((InputStream) file.getInputStream(), file.getSize(), fileName,
                null);
        Map<String, String> map = new HashMap<>(4);
        map.put("groupName", storePath.getGroup());
        map.put("fileId", storePath.getPath());
        return map;
    }

    /**
     * 验证文件是否存在
     *
     * @param groupName
     * @param fileId
     * @return
     */
    public boolean validFile(String groupName, String fileId) {
        FileInfo fileInfo = null;
        try {
            fileInfo = storageClient.queryFileInfo(groupName, fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (null != fileInfo) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件
     *
     * @param groupName
     * @param fileId
     * @return
     * @throws IOException
     */
    public byte[] getBytes(String groupName, String fileId) {

        InputStream ins = storageClient.downloadFile(groupName, fileId, new DownloadCallback<InputStream>() {
            @Override
            public InputStream recv(InputStream ins) throws IOException {
                return ins;
            }

        });
        try {
            byte[] bytes = IoUtil.readBytes(ins);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getFile(String groupName, String fileId) {
        return getFile(groupName, fileId, null);
    }

    public File getFile(String groupName, String fileId, String path) {
        byte[] bytes = getBytes(groupName, fileId);
        String filePath = "";
        if (StrUtil.isEmpty(path)) {
            filePath = TMP_HOME + System.currentTimeMillis();
        } else {
            filePath = path;
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public int deleteFile(String groupname, String fileId) {
        try {
            storageClient.deleteFile(groupname, fileId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除本地文件
     */
    public void deleteFile(File file) {
        try {
            if (FileUtil.exist(file)) {
                FileUtil.del(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("临时文件:" + FileUtil.mainName(file) + "删除失败");
        }
    }


}
