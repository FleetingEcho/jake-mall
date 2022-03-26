
package com.jake.mall.api.admin;

import com.jake.mall.common.Constants;
import com.jake.mall.config.annotation.TokenToAdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.jake.mall.entity.AdminUserToken;
import com.jake.mall.util.FilterUtils;
import com.jake.mall.util.Result;
import com.jake.mall.util.AppRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value = "v1", tags = "Admin File Upload API")
@RequestMapping("/manage-api/v1")
public class AdminUploadAPI {

    private static final Logger logger = LoggerFactory.getLogger(AdminUploadAPI.class);

    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    /**
     * Image upload
     */
    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ApiOperation(value = "Single image upload", notes = "file Name \"file\"")
    public Result<Object> upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file, @TokenToAdminUser AdminUserToken adminUser) throws URISyntaxException {
        logger.info("adminUser:{}", adminUser.toString());
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //create file name
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //create file
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("Folder creation failed, path is:" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            Result<Object> resultSuccess = AppRes.ok();
            resultSuccess.setData(FilterUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return AppRes.error("File upload failed");
        }
    }

    /**
     * upload multiple images
     */
    @RequestMapping(value = "/upload/files", method = RequestMethod.POST)
    @ApiOperation(value = "Multi-image upload", notes = "Multi-image upload")
    public Result<Object> uploadV2(HttpServletRequest httpServletRequest, @TokenToAdminUser AdminUserToken adminUser) throws URISyntaxException {
        logger.info("adminUser:{}", adminUser.toString());
        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int total = 0;
            while (iter.hasNext()) {
                if (total > 5) {
                    return AppRes.error("Upload up to 5 images");
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return AppRes.error("Parameter exceptions");
        }
        if (multipartFiles.size() > 5) {
            return AppRes.error("Upload up to 5 images");
        }
        List<String> fileNames = new ArrayList<>(multipartFiles.size());
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            assert fileName != null;
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //generate file name
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
            File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
            //create file
            File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
            try {
                if (!fileDirectory.exists()) {
                    if (!fileDirectory.mkdir()) {
                        throw new IOException("Folder creation failed, path is:" + fileDirectory);
                    }
                }
                multipartFile.transferTo(destFile);
                fileNames.add(FilterUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return AppRes.error("File upload failed");
            }
        }
        Result<Object> resultSuccess = AppRes.ok();
        resultSuccess.setData(fileNames);
        return resultSuccess;
    }

}
