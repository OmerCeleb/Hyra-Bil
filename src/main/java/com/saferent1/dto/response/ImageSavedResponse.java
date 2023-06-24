package com.saferent1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
public class ImageSavedResponse extends SfResponse {

    private String imageId;

    public ImageSavedResponse(String imageId, String message, boolean succes) {
        super(message, succes);
        this.imageId = imageId;
    }
}
