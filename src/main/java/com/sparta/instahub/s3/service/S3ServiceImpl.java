package com.sparta.instahub.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.instahub.s3.entity.Image;
import com.sparta.instahub.s3.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    // 파일을 S3에 업로드하고 파일 URL을 반환
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

        String fileUrl = getFileUrl(fileName);

        // 이미지 엔티티를 생성하여 데이터베이스에 저장
        Image image = new Image(fileName, fileUrl);
        imageRepository.save(image);

        return fileUrl;
    }
    // 파일을 S3에서 삭제하고 데이터베이스에서도 삭제하는 메서드
    public void deleteFile(String fileUrl) {
        Image image = imageRepository.findByUrl(fileUrl);
        if (image != null) {
            amazonS3.deleteObject(bucketName, image.getName());
            imageRepository.delete(image);
        }
    }

    // 파일명을 생성하는 메서드
    public String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    // 파일 URL을 반환하는 메서드
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
