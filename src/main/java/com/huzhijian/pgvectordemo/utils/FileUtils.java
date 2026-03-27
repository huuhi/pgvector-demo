package com.huzhijian.pgvectordemo.utils;

import com.huzhijian.pgvectordemo.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.huzhijian.pgvectordemo.domain.KnowledgeFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/27
 * 说明:
 */
@Slf4j
@Component
public class FileUtils {
    private final String filePath;
    private final FilesService filesService;

    public FileUtils(@Value("${upload-path}") String filePath, FilesService filesService) {
        this.filePath = filePath;
        this.filesService = filesService;
    }

    public List<KnowledgeFiles> getFilesById(List<Long> ids){
        return filesService.listByIds(ids).stream().peek(f-> f.setFilePath(filePath+"/"+f.getFilePath())).toList();
    }


    public void uploadFils(MultipartFile[] files)  {
        if (files==null||files.length==0) return;
        List<KnowledgeFiles> knowledgeFilesList = new ArrayList<>();
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        for (MultipartFile file : files) {
            if (file.isEmpty()){
                continue;
            }
            String originalFilename = file.getOriginalFilename();
            KnowledgeFiles.KnowledgeFilesBuilder knowledgeFilesBuilder = KnowledgeFiles.builder()
                    .fileName(originalFilename)
                    .fileSize(file.getSize());
            String fileExtension= null;
            try {
                fileExtension = org.codelibs.jhighlight.tools.FileUtils.getExtension(originalFilename);
            } catch (IllegalArgumentException e) {
                log.error("获取文件后缀失败");
                knowledgeFilesBuilder.failReason("获取文件后缀失败");
            }
            String uniqueFilename= UUID.randomUUID()+(fileExtension!=null?"."+fileExtension:"");
            Path targetDir = Paths.get(filePath, dateDir);
            Path targetFile = targetDir.resolve(uniqueFilename);
            try {
                Files.createDirectories(targetDir);
                file.transferTo(targetFile.toFile());
            } catch (IOException e) {
                log.error("创建目录/保存文件失败");
                knowledgeFilesBuilder.failReason("创建目录/保存文件失败");
            }
//            相对路径~
            String relativePath = dateDir.replace("\\","/")+"/"+uniqueFilename;
            KnowledgeFiles knowledgeFile = knowledgeFilesBuilder.filePath(relativePath).build();
            knowledgeFilesList.add(knowledgeFile);
        }
        filesService.saveBatch(knowledgeFilesList);
    }

}
