import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Main {

    private static final String PATH = "C://Users//exyst//IdeaProjects//JavaCore//filesTask1//Games//savegames";

    public static void main(String[] args) {
        GameProgress gp1 = new GameProgress((int) (Math.random() * 100), (int) (Math.random() * 10), (int) (Math.random() * 99), (Math.random() * 400));
        GameProgress gp2 = new GameProgress((int) (Math.random() * 100), (int) (Math.random() * 10), (int) (Math.random() * 99), (Math.random() * 400));
        GameProgress gp3 = new GameProgress((int) (Math.random() * 100), (int) (Math.random() * 10), (int) (Math.random() * 99), (Math.random() * 400));

        saveGame((PATH + "//save1.dat"), gp1);
        saveGame((PATH + "//save2.dat"), gp2);
        saveGame((PATH + "//save3.dat"), gp3);

        List<String> saves = new ArrayList<>();
        saves.add(PATH + "//save1.dat");
        saves.add(PATH + "//save2.dat");
        saves.add(PATH + "//save3.dat");

        zipFiles((PATH + "//zip.zip"), saves);

        //3 задача:
        openZip(PATH + "//zip.zip", PATH);

        System.out.println(openProgress(PATH + "//save1.dat"));
        System.out.println(openProgress(PATH + "//save2.dat"));
        System.out.println(openProgress(PATH + "//save3.dat"));
    }

    private static void saveGame(String path, GameProgress gameProgress) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles(String path, List<String> saves) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String way : saves) {
                try (FileInputStream fis = new FileInputStream(way)) {
                    File file = new File(way);
                    zout.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //удаление файлов
        for (String str : saves) {
            File file = new File(str);
            if (file.delete())
                System.out.println("Файл удален");
            else System.out.println("Файл не удален");
        }
    }

    //3 задача:
    private static void openZip(String path, String pathToDir) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(pathToDir + "//" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}