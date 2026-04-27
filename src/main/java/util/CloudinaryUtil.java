package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Cloudinaryを使用した画像ストレージの実装クラスです。
 */
public class CloudinaryUtil implements ImageStorageProvider {
    private static Cloudinary cloudinary;
    private static final CloudinaryUtil INSTANCE = new CloudinaryUtil();

    static {
        try (InputStream is = CloudinaryUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
                
                String cloudName = props.getProperty("cloudinary.cloud_name");
                String apiKey = props.getProperty("cloudinary.api_key");
                String apiSecret = props.getProperty("cloudinary.api_secret");

                if (cloudName != null && apiKey != null && apiSecret != null) {
                    cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret,
                        "secure", true
                    ));
                } else {
                    System.err.println("[WARN] Cloudinary properties are missing in database.properties. Image features will be disabled.");
                }
            } else {
                System.err.println("[WARN] database.properties not found. Cloudinary will not be initialized.");
            }
        } catch (Exception e) {
            System.err.println("[WARN] Cloudinary initialization failed: " + e.getMessage());
        }
    }

    public static CloudinaryUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public String upload(Part filePart) {
        if (filePart == null || filePart.getSize() == 0 || cloudinary == null) {
            return null;
        }

        File tempFile = null;
        try {
            tempFile = File.createTempFile("upload_", "_" + filePart.getSubmittedFileName());
            try (InputStream input = filePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }

            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "quality", "auto",
                "fetch_format", "auto"
            ));

            String publicId = (String) uploadResult.get("public_id");
            Object version = uploadResult.get("version");
            return "v" + version + "/" + publicId;

        } catch (Exception e) {
            System.err.println("[ERROR] Cloudinary upload failed: " + e.getMessage());
            return null;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Override
    public boolean delete(String identifier) {
        if (identifier == null || identifier.isEmpty() || cloudinary == null) {
            return false;
        }

        try {
            String publicId = extractPublicId(identifier);
            if (publicId == null) return false;

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            System.err.println("[ERROR] Cloudinary delete failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getResizedUrl(String identifier, int width, int height) {
        return staticGetResizedUrl(identifier, width, height);
    }

    public static String staticGetResizedUrl(String identifier, int width, int height) {
        if (identifier == null || identifier.isEmpty() || cloudinary == null) {
            return identifier;
        }

        String sourcePath = identifier;
        if (identifier.contains("/upload/")) {
            sourcePath = identifier.split("/upload/")[1];
        }

        return cloudinary.url()
            .transformation(new Transformation()
                .width(width)
                .height(height)
                .crop("fill")
                .gravity("auto")
                .quality("auto")
                .fetchFormat("auto"))
            .generate(sourcePath);
    }

    private static String extractPublicId(String identifier) {
        if (identifier == null) return null;
        String path = identifier;
        if (identifier.contains("/upload/")) {
            path = identifier.split("/upload/")[1];
        }
        if (path.startsWith("v") && path.contains("/")) {
            path = path.substring(path.indexOf("/") + 1);
        }
        if (path.contains(".")) {
            path = path.substring(0, path.lastIndexOf("."));
        }
        return path;
    }

    public static String uploadStatic(Part filePart) {
        return INSTANCE.upload(filePart);
    }

    public static boolean deleteByIdentifier(String identifier) {
        return INSTANCE.delete(identifier);
    }
}
