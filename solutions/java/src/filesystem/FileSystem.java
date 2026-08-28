package filesystem;

import java.util.List;

public class FileSystem {
    private final Folder root;

    public FileSystem() {
        this.root = new Folder("/");
    }

    public File createFile(String path, String content) {
        // e.g. path = "/home/user/notes.txt", "/notes.txt" ; content = "hello world"
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot create file at root");
        }

        Folder parent = resolveParent(path);
        String fileName = extractName(path);

        if (parent.hasChild(fileName)) {
            throw new IllegalStateException("Entry already exists: " + fileName);
        }

        File file = new File(fileName, content);
        parent.addChild(file);
        return file;
    }

    public Folder createFolder(String path) {
        if (path.equals("/")) {
            throw new IllegalStateException("Root already exists");
        }

        Folder parent = resolveParent(path);
        String folderName = extractName(path);

        if (parent.hasChild(folderName)) {
            throw new IllegalStateException("Entry already exists: " + folderName);
        }

        Folder folder = new Folder(folderName);
        parent.addChild(folder);
        return folder;
    }

    public void delete(String path) {
        // eg "/home/notes.txt", "/nonexistent"
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot delete root");
        }

        Folder parent = resolveParent(path);
        String name = extractName(path);

        FileSystemEntry removed = parent.removeChild(name);
        if (removed == null) {
            throw new IllegalArgumentException("Entry not found: " + path);
        }
    }

    public List<FileSystemEntry> list(String path) {
        // eg. path = "/home/user"
        FileSystemEntry entry = resolvePath(path);

        if (!entry.isDirectory()) {
            throw new IllegalArgumentException("Cannot list a file");
        }

        return ((Folder) entry).getChildren();
    }

    public FileSystemEntry get(String path) {
        return resolvePath(path);
    }

    public void rename(String path, String newName) {
        // eg. path="/home/user/notes.txt" newName="new_notes.txt"
        if (path.equals("/")) {
            throw new IllegalArgumentException("Cannot rename root");
        }

        if (newName == null || newName.isEmpty() || newName.contains("/")) {
            throw new IllegalArgumentException("Invalid name");
        }

        Folder parent = resolveParent(path);
        String oldName = extractName(path);

        if (!parent.hasChild(oldName)) {
            throw new IllegalArgumentException("Entry not found");
        }

        if (parent.hasChild(newName)) {
            throw new IllegalStateException("Entry already exists: " + newName);
        }

        FileSystemEntry entry = parent.removeChild(oldName);
        entry.setName(newName);
        parent.addChild(entry);
    }

    public void move(String srcPath, String destPath) {
        // eg. move("/home/user/notes.txt", "/home/notes.txt") ; move("/home", "/home/user/stuff") ; move("/home/abc", "/new_home)
        if (srcPath.equals("/")) {
            throw new IllegalArgumentException("Cannot move root");
        }

        Folder srcParent = resolveParent(srcPath);
        String srcName = extractName(srcPath);
        FileSystemEntry entry = srcParent.getChild(srcName);

        if (entry == null) {
            throw new IllegalArgumentException("Source not found: " + srcPath);
        }

        Folder destParent = resolveParent(destPath);
        String destName = extractName(destPath);

        if (entry.isDirectory()) {
            Folder current = destParent;
            while (current != null) {
                if (current == entry) {
                    throw new IllegalArgumentException("Cannot move folder into itself");
                }
                current = current.getParent();
            }
        }

        if (destParent.hasChild(destName)) {
            throw new IllegalStateException("Destination already exists: " + destPath);
        }
        // move("/home/abc", "/new_home)
        srcParent.removeChild(srcName);
        entry.setName(destName);
        destParent.addChild(entry);
    }

    private FileSystemEntry resolvePath(String path) {
        // eg. path = "/home/user", "/"
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute");
        }

        if (path.equals("/")) {
            return root;
        }

        String[] parts = path.substring(1).split("/");
        FileSystemEntry current = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Invalid path: consecutive slashes");
            }

            if (!current.isDirectory()) {
                throw new IllegalArgumentException("Not a directory");
            }

            FileSystemEntry child = ((Folder) current).getChild(part);
            if (child == null) {
                throw new IllegalArgumentException("Path not found: " + path);
            }

            current = child;
        }

        return current;
    }

    private Folder resolveParent(String path) {
        // eg. path = "/home/user/notes.txt", "/home"
        if (path.equals("/")) {
            throw new IllegalArgumentException("Root has no parent");
        }

        int lastSlash = path.lastIndexOf("/");
        String parentPath = lastSlash == 0 ? "/" : path.substring(0, lastSlash);

        FileSystemEntry parent = resolvePath(parentPath);

        if (!parent.isDirectory()) {
            throw new IllegalArgumentException("Parent is not a directory");
        }

        return (Folder) parent;
    }

    private String extractName(String path) {
        int lastSlash = path.lastIndexOf("/");
        return path.substring(lastSlash + 1);
    }
}

