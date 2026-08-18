package filesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder extends FileSystemEntry {
    private final Map<String, FileSystemEntry> children;

    public Folder(String name) {
        super(name);
        this.children = new HashMap<>();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public boolean addChild(FileSystemEntry entry) {
        if (entry == null) {
            return false;
        }

        if (children.containsKey(entry.getName())) {
            return false;
        }

        children.put(entry.getName(), entry);
        entry.setParent(this);
        return true;
    }

    public FileSystemEntry removeChild(String name) {
        FileSystemEntry entry = children.remove(name);
        if (entry != null) {
            entry.setParent(null);
        }
        return entry;
    }

    public FileSystemEntry getChild(String name) {
        return children.get(name);
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    public List<FileSystemEntry> getChildren() {
        return new ArrayList<>(children.values());
    }
}
