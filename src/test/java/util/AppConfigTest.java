package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class AppConfigTest {

    @Test
    void environmentVariableOverridesPropertyFile() {
        Properties properties = new Properties();
        properties.setProperty("db.mysql.pass", "file-secret");

        String value = AppConfig.resolve(
                properties, Map.of("DB_MYSQL_PASS", "environment-secret"),
                "db.mysql.pass", "default-secret");

        assertEquals("environment-secret", value);
    }

    @Test
    void propertyFileIsUsedWhenEnvironmentVariableIsMissing() {
        Properties properties = new Properties();
        properties.setProperty("db.mysql.user", "file-user");

        String value = AppConfig.resolve(
                properties, Map.of(), "db.mysql.user", "default-user");

        assertEquals("file-user", value);
    }

    @Test
    void defaultValueIsUsedWhenNoConfigurationExists() {
        String value = AppConfig.resolve(
                new Properties(), Map.of(), "db.mysql.user", "default-user");

        assertEquals("default-user", value);
    }

    @Test
    void propertyNameIsConvertedToEnvironmentVariableName() {
        assertEquals("CLOUDINARY_API_SECRET",
                AppConfig.toEnvironmentVariable("cloudinary.api_secret"));
    }
}
