package com.tingyu.xblog.app.handler.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import com.tingyu.xblog.app.exception.FileOperationException;
import com.tingyu.xblog.app.model.entity.Attachment;
import com.tingyu.xblog.app.model.enums.AttachmentType;
import com.tingyu.xblog.app.model.support.UploadResult;

import java.util.Collection;
import java.util.LinkedList;

/**
 * File handler manager.
 *
 * @author johnniang
 * @date 2019-03-27
 */
@Slf4j
@Component
public class FileHandlers {

    /**
     * File handler container.
     */
    private final Collection<FileHandler> fileHandlers = new LinkedList<>();

    public FileHandlers(ApplicationContext applicationContext) {
        // Add all file handler
        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
        log.info("Registered {} file handler(s)", fileHandlers.size());
    }

    /**
     * Uploads files.
     *
     * @param file           multipart file must not be null
     * @param attachmentType attachment type must not be null
     * @return upload result
     * @throws FileOperationException throws when fail to delete attachment or no available file handler to upload it
     */
    @NonNull
    public UploadResult upload(@NonNull MultipartFile file, @NonNull AttachmentType attachmentType) {
        Assert.notNull(attachmentType, "Attachment type must not be null");

        return upload(file, attachmentType.name());
    }

    /**
     * Uploads files.
     *
     * @param file multipart file must not be null
     * @param type store type
     * @return upload result
     * @throws FileOperationException throws when fail to delete attachment or no available file handler to upload it
     */
    @NonNull
    public UploadResult upload(@NonNull MultipartFile file, @Nullable String type) {
        Assert.notNull(file, "Multipart file must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(type)) {
                return fileHandler.upload(file);
            }
        }

        throw new FileOperationException("No available file handlers to upload the file").setErrorData(type);
    }

    /**
     * Deletes attachment.
     *
     * @param attachment attachment detail must not be null
     * @throws FileOperationException throws when fail to delete attachment or no available file handler to delete it
     */
    public void delete(@NonNull Attachment attachment) {
        Assert.notNull(attachment, "Attachment must not be null");

        delete(attachment.getType().name(), attachment.getFileKey());
    }

    /**
     * Deletes attachment.
     *
     * @param key file key
     * @throws FileOperationException throws when fail to delete attachment or no available file handler to delete it
     */
    public void delete(@Nullable String type, @NonNull String key) {
        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(type)) {
                // Delete the file
                fileHandler.delete(key);
                return;
            }
        }

        throw new FileOperationException("No available file handlers to delete the file").setErrorData(type);
    }

    /**
     * Adds file handlers.
     *
     * @param fileHandlers file handler collection
     * @return current file handlers
     */
    @NonNull
    public FileHandlers addFileHandlers(@Nullable Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            this.fileHandlers.addAll(fileHandlers);
        }
        return this;
    }
}
