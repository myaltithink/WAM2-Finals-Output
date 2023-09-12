package wam2.finals.filevault;

public class PasswordObj {

    private int ID;
    private String fileName;
    private String extension;
    private String dateAdded;

    public PasswordObj(int ID, String fileName, String extension) {
        this.ID = ID;
        this.fileName = fileName;
        this.extension = extension;
    }

    public PasswordObj(String fileName, String extension) {
        this.fileName = fileName;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getID() {
        return ID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
