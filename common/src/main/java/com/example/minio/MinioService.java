package com.example.minio;

import com.example.entity.dto.Base64Upload;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class MinioService {

    @Value("${spring.minio.endpoint}")
    private String endpoint;

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public void createBucket() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName).build());
        }
    }

    public String uploadBase64(Base64Upload dto) throws Exception {
        if (dto.getBase64().startsWith(endpoint + '/' + bucketName + '/')) {
            return dto.getBase64();
        }

        String[] base64Parts = dto.getBase64().split(",");
        byte[] bytes = Base64.getDecoder().decode(base64Parts.length > 1 ? base64Parts[1] : base64Parts[0]);
        InputStream stream = new ByteArrayInputStream(bytes);


        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(dto.getFileName())
                        .stream(stream, bytes.length, -1)
                        .contentType("image/" + getFileExtension(dto.getFileName()))
                        .build());

        return String.format("%s/%s/%s", endpoint, bucketName, dto.getFileName());
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }


    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
