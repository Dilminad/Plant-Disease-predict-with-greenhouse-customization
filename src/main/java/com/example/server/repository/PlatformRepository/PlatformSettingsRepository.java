package com.example.server.repository.PlatformRepository;




import com.example.server.model.Commission.PlatformSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PlatformSettingsRepository extends MongoRepository<PlatformSettings, String> {
    Optional<PlatformSettings> findById(String id);
}