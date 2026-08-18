package filesystem;

public abstract class FileSystemEntry {
    private String name;
    private Folder parent;

    public FileSystemEntry(String name) {
        this.name = name;
        this.parent = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public String getPath() {
        if (parent == null) {
            return name;
        }

        String parentPath = parent.getPath();
        if (parentPath.equals("/")) {
            return "/" + name;
        }
        return parentPath + "/" + name;
    }

    public abstract boolean isDirectory();
}
