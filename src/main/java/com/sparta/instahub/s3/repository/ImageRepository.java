package com.sparta.instahub.s3.repository;

import com.sparta.instahub.s3.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUrl(String url);
}
