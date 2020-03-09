package com.tingyu.xblog.app.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.exception.NotFoundException;
import com.tingyu.xblog.app.exception.ServiceException;
import com.tingyu.xblog.app.model.dto.BackupDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostDetailDTO;
import com.tingyu.xblog.app.model.entity.Post;
import com.tingyu.xblog.app.model.entity.Tag;
import com.tingyu.xblog.app.model.support.XBlogConst;
import com.tingyu.xblog.app.security.service.OneTimeTokenService;
import com.tingyu.xblog.app.service.BackupService;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.PostService;
import com.tingyu.xblog.app.service.PostTagService;
import com.tingyu.xblog.app.utils.XBlogUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Backup service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-26
 */
@Service
@Slf4j
public class BackupServiceImpl implements BackupService {

    private static final String BACKUP_RESOURCE_BASE_URI = "/api/admin/backups/halo";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final PostService postService;

    private final PostTagService postTagService;

    private final OptionService optionService;

    private final OneTimeTokenService oneTimeTokenService;

    private final XBlogProperties XBlogProperties;

    public BackupServiceImpl(PostService postService,
                             PostTagService postTagService,
                             OptionService optionService,
                             OneTimeTokenService oneTimeTokenService,
                             XBlogProperties XBlogProperties) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.optionService = optionService;
        this.oneTimeTokenService = oneTimeTokenService;
        this.XBlogProperties = XBlogProperties;
    }

    /**
     * Sanitizes the specified file name.
     *
     * @param unSanitized the specified file name
     * @return sanitized file name
     */
    public static String sanitizeFilename(final String unSanitized) {
        return unSanitized.
            replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5\\.)]", "").
            replaceAll("[\\?\\\\/:|<>\\*\\[\\]\\(\\)\\$%\\{\\}@~\\.]", "").
            replaceAll("\\s", "");
    }

    @Override
    public BasePostDetailDTO importMarkdown(MultipartFile file) throws IOException {

        // Read markdown content.
        String markdown = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);

        // TODO sheet import

        return postService.importMarkdown(markdown, file.getOriginalFilename());
    }

    @Override
    public JSONObject exportHexoMDs() {
        final JSONObject ret = new JSONObject();
        final List<JSONObject> posts = new ArrayList<>();
        ret.put("posts", (Object) posts);
        final List<JSONObject> passwords = new ArrayList<>();
        ret.put("passwords", (Object) passwords);
        final List<JSONObject> drafts = new ArrayList<>();
        ret.put("drafts", (Object) drafts);

        List<Post> postList = postService.listAll();
        Map<Integer, List<Tag>> talMap = postTagService.listTagListMapBy(postList.stream().map(Post::getId).collect(Collectors.toList()));
        for (Post post : postList) {
            final Map<String, Object> front = new LinkedHashMap<>();
            final String title = post.getTitle();
            front.put("title", title);
            final String date = DateFormatUtils.format(post.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            front.put("date", date);
            front.put("updated", DateFormatUtils.format(post.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            final List<String> tags = talMap.get(post.getId()).stream().map(Tag::getName).collect(Collectors.toList());
            if (tags.isEmpty()) {
                tags.add("halo");
            }
            front.put("tags", tags);
            front.put("permalink", "");
            final JSONObject one = new JSONObject();
            one.put("front", new Yaml().dump(front));
            one.put("title", title);
            one.put("content", post.getOriginalContent());
            one.put("created", post.getCreateTime().getTime());

            if (StringUtils.isNotBlank(post.getPassword())) {
                passwords.add(one);
            } else if (post.getDeleted()) {
                drafts.add(one);
            } else {
                posts.add(one);
            }
        }

        return ret;
    }

    @Override
    public void exportHexoMd(List<JSONObject> posts, String dirPath) {
        posts.forEach(post -> {
            final String filename = sanitizeFilename(post.optString("title")) + ".md";
            final String text = post.optString("front") + "---" + LINE_SEPARATOR + post.optString("content");

            try {
                final String date = DateFormatUtils.format(post.optLong("created"), "yyyyMM");
                final String dir = dirPath + File.separator + date + File.separator;
                new File(dir).mkdirs();
                FileUtils.writeStringToFile(new File(dir + filename), text, "UTF-8");
            } catch (final Exception e) {
                log.error("Write markdown file failed", e);
            }
        });
    }

    @Override
    public BackupDTO zipWorkDirectory() {
        // Zip work directory to temporary file
        try {
            // Create zip path for halo zip
            String haloZipFileName = XBlogConst.HALO_BACKUP_PREFIX +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-")) +
                IdUtil.simpleUUID().hashCode() + ".zip";
            // Create halo zip file
            Path haloZipPath = Files.createFile(Paths.get(XBlogProperties.getBackupDir(), haloZipFileName));

            // Zip halo
            com.tingyu.xblog.app.utils.FileUtils.zip(Paths.get(this.XBlogProperties.getWorkDir()), haloZipPath);

            // Build backup dto
            return buildBackupDto(haloZipPath);
        } catch (IOException e) {
            throw new ServiceException("Failed to backup halo", e);
        }
    }

    @Override
    public List<BackupDTO> listHaloBackups() {
        // Ensure the parent folder exist
        Path backupParentPath = Paths.get(XBlogProperties.getBackupDir());
        if (Files.notExists(backupParentPath)) {
            return Collections.emptyList();
        }

        // Build backup dto
        try (Stream<Path> subPathStream = Files.list(backupParentPath)) {
            return subPathStream
                .filter(backupPath -> StringUtils.startsWithIgnoreCase(backupPath.getFileName().toString(), XBlogConst.HALO_BACKUP_PREFIX))
                .map(this::buildBackupDto)
                .sorted((leftBackup, rightBackup) -> {
                    // Sort the result
                    if (leftBackup.getUpdateTime() < rightBackup.getUpdateTime()) {
                        return 1;
                    } else if (leftBackup.getUpdateTime() > rightBackup.getUpdateTime()) {
                        return -1;
                    }
                    return 0;
                }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch backups", e);
        }
    }

    @Override
    public void deleteHaloBackup(String fileName) {
        Assert.hasText(fileName, "File name must not be blank");

        Path backupRootPath = Paths.get(XBlogProperties.getBackupDir());

        // Get backup path
        Path backupPath = backupRootPath.resolve(fileName);

        // Check directory traversal
        com.tingyu.xblog.app.utils.FileUtils.checkDirectoryTraversal(backupRootPath, backupPath);

        try {
            // Delete backup file
            Files.delete(backupPath);
        } catch (NoSuchFileException e) {
            throw new NotFoundException("The file " + fileName + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to delete backup", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        Assert.hasText(fileName, "Backup file name must not be blank");

        Path backupParentPath = Paths.get(XBlogProperties.getBackupDir());

        try {
            if (Files.notExists(backupParentPath)) {
                // Create backup parent path if it does not exists
                Files.createDirectories(backupParentPath);
            }

            // Get backup file path
            Path backupFilePath = Paths.get(XBlogProperties.getBackupDir(), fileName).normalize();

            // Check directory traversal
            com.tingyu.xblog.app.utils.FileUtils.checkDirectoryTraversal(backupParentPath, backupFilePath);

            // Build url resource
            Resource backupResource = new UrlResource(backupFilePath.toUri());
            if (!backupResource.exists()) {
                // If the backup resource is not exist
                throw new NotFoundException("The file " + fileName + " was not found");
            }
            // Return the backup resource
            return backupResource;
        } catch (MalformedURLException e) {
            throw new NotFoundException("The file " + fileName + " was not found", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to create backup parent path: " + backupParentPath, e);
        }
    }

    /**
     * Builds backup dto.
     *
     * @param backupPath backup path must not be null
     * @return backup dto
     */
    private BackupDTO buildBackupDto(@NonNull Path backupPath) {
        Assert.notNull(backupPath, "Backup path must not be null");

        String backupFileName = backupPath.getFileName().toString();
        BackupDTO backup = new BackupDTO();
        try {
            backup.setDownloadUrl(buildDownloadUrl(backupFileName));
            backup.setDownloadLink(backup.getDownloadUrl());
            backup.setFilename(backupFileName);
            backup.setUpdateTime(Files.getLastModifiedTime(backupPath).toMillis());
            backup.setFileSize(Files.size(backupPath));
        } catch (IOException e) {
            throw new ServiceException("Failed to access file " + backupPath, e);
        }

        return backup;
    }

    /**
     * Builds download url.
     *
     * @param filename filename must not be blank
     * @return download url
     */
    @NonNull
    private String buildDownloadUrl(@NonNull String filename) {
        Assert.hasText(filename, "File name must not be blank");

        // Composite http url
        String backupUri = BACKUP_RESOURCE_BASE_URI + XBlogUtils.URL_SEPARATOR + filename;

        // Get a one-time token
        String oneTimeToken = oneTimeTokenService.create(backupUri);

        // Build full url
        return XBlogUtils.compositeHttpUrl(optionService.getBlogBaseUrl(), backupUri)
            + "?"
            + XBlogConst.ONE_TIME_TOKEN_QUERY_NAME
            + "=" + oneTimeToken;
    }

}
