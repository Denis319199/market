package com.db.endpoint;

import com.db.model.Image;
import com.db.repo.ImagesRepo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImagesController {
    private final ImagesRepo imagesRepo;

    @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage() {
        return imagesRepo.findAll().get(0).getImage();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createImage(@RequestParam MultipartFile file) throws IOException {
        Image toBeSaved = new Image();
        toBeSaved.setImage(file.getBytes());
        imagesRepo.save(toBeSaved);
    }
}
