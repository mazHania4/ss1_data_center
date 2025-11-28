package ss1.ong.datacenter.common.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import ss1.ong.datacenter.common.config.S3Config;

import java.io.IOException;
import java.util.UUID;


@Service
public class FilesService {

    private final S3Config s3Config;
    private final S3Client s3Client;

    public FilesService(S3Config s3Config) {
        this.s3Config = s3Config;
        this.s3Client = S3Client.builder()
                .region(Region.of(s3Config.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(s3Config.getAccessKey(), s3Config.getSecretKey())
                        )
                )
                .build();
    }

    /**
     * Sube un archivo al servidor de G3 y devuelve el path
     * */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String extension = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        // Generar nombre único
        String key = folder + "/" + UUID.randomUUID() + extension; // <-- aquí agregamos el folder

        // Subir a S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(s3Config.getBucketName())
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        // URL pública
        return "https://" + s3Config.getBucketName() + ".s3.amazonaws.com/" + key;
    }

}


