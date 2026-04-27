package util;

import jakarta.servlet.http.Part;

/**
 * 画像ストレージへの操作を抽象化するインターフェースです。
 * これにより、Cloudinary以外のストレージへの切り替えやテスト時のモック化が容易になります。
 */
public interface ImageStorageProvider {
    /**
     * 画像をアップロードし、識別子を返します。
     */
    String upload(Part filePart);

    /**
     * 識別子を基に画像を削除します。
     */
    boolean delete(String identifier);

    /**
     * 識別子を基にリサイズされたURLを生成します。
     */
    String getResizedUrl(String identifier, int width, int height);
}
