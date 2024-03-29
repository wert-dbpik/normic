package ru.wert.normic.entities.db_connection.files;


import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.poi.util.IOUtils;
import retrofit2.Call;
import retrofit2.Response;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;


import java.io.*;
import java.nio.file.Files;

@Slf4j
public class FilesService implements IFilesService {

    private static FilesService instance;
    private FilesApiInterface api;

    private FilesService() {
        api = RetrofitClient.getInstance().getRetrofit().create(FilesApiInterface.class);
    }

    public FilesApiInterface getApi() {
        return api;
    }

    public static FilesService getInstance() {
        if (instance == null)
            return new FilesService();
        return instance;
    }

    /**
     * @param path     - Папка хранения (ex: normic)
     * @param fileName Имя файла (ex: constants)
     * @param ext      расширение (ex: properties)
     * @param destDir  папка для временного хранения (ex: tempDir)
     * @param destFileName   начало наименования файла (ex: normic)
     * @return
     */
    @Override
    public boolean download(String path, String fileName, String ext, String destDir, String destFileName) {
        //ext уже с точкой
        try {
            Call<ResponseBody> call = api.download(path, fileName + ext);
            Response<ResponseBody> r = call.execute();
            if (r.isSuccessful()) {
                InputStream inputStream = r.body().byteStream();
                try (OutputStream outputStream = new FileOutputStream(destDir + "/" + destFileName + ext)) {
                    IOUtils.copy(inputStream, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean upload(String fileNameForSaving, String directoryName, File initialFile) throws IOException {
        log.debug("upload : Загружаем в БД чертеж {}", fileNameForSaving);
        byte[] draftBytes = Files.readAllBytes(initialFile.toPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), draftBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileNameForSaving, requestBody);
        try {
            Call<Void> call = api.upload(directoryName, body);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
