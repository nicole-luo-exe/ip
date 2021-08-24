import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class FileManager {

    private File f;

    public FileManager(String pathname) {
        f = new File(pathname);
    }

    public ArrayList<Task> loadInTasks() throws IOException {
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
            return new ArrayList<>();
        } else {
            Scanner scanner = new Scanner(f);
            ArrayList<Task> res = new ArrayList<>();
            while (scanner.hasNextLine()) {
                Task t = parse(scanner.nextLine());
                res.add(t);
            }
            return res;
        }
    }
    public void saveTask(Task task) throws IOException {
        FileWriter fw = new FileWriter(f, true);
        if (f.length() != 0) {
            fw.write(System.lineSeparator());
        }
        fw.write(task.toString());
        fw.close();
    }

    public void deleteTask(Task task) throws IOException {
        rewriteLine(task.toString(), "");
    }

    public void markDone(Task task) throws IOException {
        rewriteLine(task.getTaskDesc(),
                task.toString());
    }

    private void rewriteLine(String target, String replace)
            throws IOException {
        File temp = File.createTempFile("temp",".txt", f.getParentFile());
        Scanner scanner = new Scanner(f);
        FileWriter tempWriter = new FileWriter(temp, true);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(target)) {
                if (replace.isEmpty()) continue;
                tempWriter.write(replace);
            } else {
                tempWriter.write(line);
            }
            if (scanner.hasNextLine()) tempWriter.write(System.lineSeparator());
        }
        tempWriter.close();
        f.delete();
        temp.renameTo(f);
    }

    private static Task parse(String s) throws IllegalArgumentException {
        char taskType = s.charAt(1);
        String desc = s.substring(7);
        boolean isDone = s.charAt(4) == 'X';
        switch (taskType) {
            case 'T':
                return new Todo(desc, isDone);
            case 'D':
                return Deadline.createDeadline(desc, isDone);
            case 'E':
                return Event.createEvent(desc, isDone);
            default:
                throw new IllegalArgumentException("File improperly formatted");
        }
    }



}
