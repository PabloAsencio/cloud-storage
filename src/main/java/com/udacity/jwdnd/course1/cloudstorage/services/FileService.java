package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<UploadedFile> getUserFiles(Integer userid) {
        return fileMapper.getUserFiles(userid);
    }

    public UploadedFile getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public UploadedFile getFileByName(String filename, Integer userid) {
        return fileMapper.getFileByName(filename, userid);
    }

    public void createFile(UploadedFile uploadedFile) {
        fileMapper.insert(uploadedFile);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

}
