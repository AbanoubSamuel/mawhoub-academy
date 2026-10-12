package com.ficklebytes.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CloudinaryUtil {

  // cloudinary://<api_key>:<api_secret>@<cloud_name>
  private static final Cloudinary c = new Cloudinary(System.getenv("CLOUDINARY_URL"));

  public String UploadImage(File image, String publicId) throws IOException {
    Map params = ObjectUtils.asMap("public_id", publicId, "overwrite"
        , true, "resource_type", "image");
    Map resultMap = c.uploader().upload(image, params);
    return (String) resultMap.get("url");
  }

}
