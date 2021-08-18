package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<UploadedFile> getUserFiles(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    UploadedFile getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    UploadedFile getFileByName(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(UploadedFile uploadedFile);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(Integer fileId);
}
