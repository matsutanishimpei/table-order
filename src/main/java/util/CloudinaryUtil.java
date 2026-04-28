package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * Cloudinaryを使用した画像ストレージの実装クラスです。
 */
@Slf4j
public final class CloudinaryUtil implements ImageStorageProvider {
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
                    log.warn("Cloudinary properties are missing in database.properties. "
                            + "Image features will be disabled.");
                }
            } else {
                log.warn("database.properties not found. Cloudinary will not be initialized.");
            }
        } catch (IOException e) {
            log.error("Cloudinary initialization failed due to I/O error", e);
        }
    }

    private CloudinaryUtil() {
        // シングルトン
    }

    @SuppressFBWarnings("MS_EXPOSE_REP")
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

            Map<String, Object> uploadResult = toTypedMap(cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "quality", "auto",
                    "fetch_format", "auto"
            )));

            String publicId = (String) uploadResult.get("public_id");
            Object version = uploadResult.get("version");
            return "v" + version + "/" + publicId;

        } catch (IOException e) {
            log.error("Cloudinary upload failed due to I/O error", e);
            return null;
        } catch (Exception e) {
            log.error("Cloudinary upload failed", e);
            return null;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
                }
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
            if (publicId == null) {
                return false;
            }

            Map<String, Object> result = toTypedMap(cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap()));
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            log.error("Cloudinary delete failed due to I/O error", e);
            return false;
        } catch (Exception e) {
            log.error("Cloudinary delete failed", e);
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
                .transformation(new Transformation<>()
                        .width(width)
                        .height(height)
                        .crop("fill")
                        .gravity("auto")
                        .quality("auto")
                        .fetchFormat("auto"))
                .generate(sourcePath);
    }

    private static String extractPublicId(String identifier) {
        if (identifier == null) {
            return null;
        }
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

    /**
     * raw型のMapを型安全な {@code Map<String, Object>} に変換します。
     * 外部ライブラリ（Cloudinary SDK）の古い設計に起因する警告をこのメソッド内に閉じ込めます。
     *
     * @param rawMap 変換前のMap
     * @return 型安全なMap
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> toTypedMap(Map<?, ?> rawMap) {
        return (Map<String, Object>) rawMap;
    }
}
