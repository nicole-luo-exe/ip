package ligma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ligma.task.Task;

/**
 * This class represents the storage for a particular
 * Ligma program. The file f is where all data is written to
 * and read from during the execution of the Ligma program.
 */
public class Storage {
    private File f;

    public Storage(String pathname) {
        f = new File(pathname);
    }

    /**
     * Loads in tasks previously stored in the file.
     *
     * @return              arrayList of tasks as read from file
     * @throws IOException  if there was an error in referencing files
     */
    public ArrayList<Task> load() throws IOException {
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
            return new ArrayList<>();
        } else {
            Scanner scanner = new Scanner(f);
            ArrayList<Task> res = new ArrayList<>();
            while (scanner.hasNextLine()) {
                Task t = Parser.parseFileContent(scanner.nextLine());
                res.add(t);
            }
            return res;
        }
    }

    /**
     * Saves tasks to file.
     *
     * @param task          task to be saved to file
     * @throws IOException  if there was an error in writing to file
     */
    public void saveTask(Task task) throws IOException {
        FileWriter fw = new FileWriter(f, true);
        if (f.length() != 0) {
            fw.write(System.lineSeparator());
        }
        fw.write(task.toString());
        fw.close();
    }

    /**
     * Deletes task from file.
     *
     * @param task          task to be deleted from file
     * @throws IOException  if there was an error in writing to file
     */
    public void deleteTask(Task task) throws IOException {
        rewriteLine(task.toString(), "");
    }

    /**
     * Updates completion status of task in file.
     *
     * @param task          task to be updated
     * @throws IOException  if there was an error in writing to file
     */
    public void markDone(Task task) throws IOException {
        rewriteLine(task.getTaskDesc(),
                task.toString());
    }

    private void rewriteLine(String target, String replace)
            throws IOException {
        File temp = File.createTempFile("temp", ".txt", f.getParentFile());
        Scanner scanner = new Scanner(f);
        FileWriter tempWriter = new FileWriter(temp, true);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(target)) {
                if (replace.isEmpty()) {
                    continue;
                }
                tempWriter.write(replace);
            } else {
                tempWriter.write(line);
            }
            if (scanner.hasNextLine()) {
                tempWriter.write(System.lineSeparator());
            }
        }
        tempWriter.close();
        f.delete();
        temp.renameTo(f);
    }

}
