import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AudioFileLoader {

    private String filePath;
    private long fileSize;
    private byte[] audioData;
    private String fileName;

    public AudioFileLoader(String filePath) {
        this.filePath = filePath;
        this.fileName = new File(filePath).getName();
        System.out.println("Создан объект AudioFileLoader для файла: " + fileName);
    }

    public void load() throws IOException {
        Path path = Paths.get(filePath);
        this.audioData = Files.readAllBytes(path);
        this.fileSize = audioData.length;
        System.out.println("Загружено " + fileSize + " байт");
    }

    public void showFirstBytes(int count) {
        if(audioData == null) {
            System.out.println("Файд не загружен");
            return;
        }

        int bytesToShow = Math.min(count, audioData.length);
        byte[] firstBytes = Arrays.copyOf(audioData, bytesToShow);

        System.out.print("Первые " + bytesToShow + " байт (НЕХ): ");
        for (byte b : firstBytes) {
            System.out.print(String.format("%02X", b & 0xFF));
        }
        System.out.println();
    }
    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public byte[] getAudioData(){
        return audioData;
    }
}
