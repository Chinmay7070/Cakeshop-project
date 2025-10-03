package com.nt.CakeModel;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CakeForm {
    private String name;
    private double price;
    private String description;
    private MultipartFile image;
}

