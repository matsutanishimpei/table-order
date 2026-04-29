package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CloudinaryUtilTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private Part filePart;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        CloudinaryUtil.setCloudinary(cloudinary);
    }

    @AfterEach
    void tearDown() throws Exception {
        CloudinaryUtil.setCloudinary(null);
        closeable.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testUpload_Success() throws IOException {
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getSubmittedFileName()).thenReturn("test.jpg");
        when(filePart.getInputStream()).thenReturn(new ByteArrayInputStream("fake-data".getBytes()));
        
        when(cloudinary.uploader()).thenReturn(uploader);
        
        Map<String, Object> uploadResult = ObjectUtils.asMap(
                "public_id", "test_id",
                "version", 12345L
        );
        when(uploader.upload(any(), anyMap())).thenReturn(uploadResult);

        String result = CloudinaryUtil.uploadStatic(filePart);

        assertEquals("v12345/test_id", result);
    }

    @Test
    void testUpload_NullPart() {
        assertNull(CloudinaryUtil.uploadStatic(null));
    }

    @Test
    void testUpload_EmptyPart() {
        when(filePart.getSize()).thenReturn(0L);
        assertNull(CloudinaryUtil.uploadStatic(filePart));
    }

    @Test
    void testUpload_CloudinaryNull() {
        CloudinaryUtil.setCloudinary(null);
        when(filePart.getSize()).thenReturn(100L);
        assertNull(CloudinaryUtil.uploadStatic(filePart));
    }

    @Test
    void testUpload_IOException() throws IOException {
        when(filePart.getSize()).thenReturn(100L);
        when(filePart.getSubmittedFileName()).thenReturn("test.jpg");
        when(filePart.getInputStream()).thenThrow(new IOException("test error"));
        
        String result = CloudinaryUtil.uploadStatic(filePart);
        assertNull(result);
    }

    @Test
    void testDelete_Success() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(eq("test_id"), anyMap())).thenReturn(ObjectUtils.asMap("result", "ok"));

        boolean result = CloudinaryUtil.deleteByIdentifier("v12345/test_id");

        assertTrue(result);
        verify(uploader).destroy(eq("test_id"), anyMap());
    }

    @Test
    void testDelete_Failure() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), anyMap())).thenReturn(ObjectUtils.asMap("result", "not found"));

        boolean result = CloudinaryUtil.deleteByIdentifier("v12345/test_id");

        assertFalse(result);
    }

    @Test
    void testDelete_InvalidIdentifier() {
        assertFalse(CloudinaryUtil.deleteByIdentifier(null));
        assertFalse(CloudinaryUtil.deleteByIdentifier(""));
    }

    @Test
    void testDelete_CloudinaryNull() {
        CloudinaryUtil.setCloudinary(null);
        assertFalse(CloudinaryUtil.deleteByIdentifier("test"));
    }

    @Test
    void testGetResizedUrl() {
        Url urlMock = mock(Url.class);
        when(cloudinary.url()).thenReturn(urlMock);
        when(urlMock.transformation(any())).thenReturn(urlMock);
        when(urlMock.generate(anyString())).thenReturn("http://res.cloudinary.com/resized");

        String result = CloudinaryUtil.staticGetResizedUrl("v12345/test_id", 100, 100);

        assertEquals("http://res.cloudinary.com/resized", result);
    }

    @Test
    void testGetResizedUrl_CloudinaryNull() {
        CloudinaryUtil.setCloudinary(null);
        String identifier = "test_id";
        assertEquals(identifier, CloudinaryUtil.staticGetResizedUrl(identifier, 100, 100));
    }

    @Test
    void testExtractPublicId() {
        assertEquals("test_id", CloudinaryUtil.extractPublicId("v12345/test_id"));
        assertEquals("test_id", CloudinaryUtil.extractPublicId("v12345/test_id.jpg"));
        assertEquals("test_id", CloudinaryUtil.extractPublicId("http://res.cloudinary.com/demo/image/upload/v12345/test_id.jpg"));
        assertEquals("test_id", CloudinaryUtil.extractPublicId("test_id"));
        assertNull(CloudinaryUtil.extractPublicId(null));
    }
}
