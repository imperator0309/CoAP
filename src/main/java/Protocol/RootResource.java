package Protocol;

import java.util.HashMap;

public final class RootResource implements Resource{
    private String name;
    private Resource parent;
    private final HashMap<String, Resource> children = new HashMap<>();

    private static final RootResource root = new RootResource("/");
    private RootResource(String name) {
        this.name = name;
    }

    public static RootResource createRoot() {
        return root;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        System.err.println("Permission Denied");
    }

    @Override
    public Resource getChild(String name) {
        return children.get(name);
    }

    @Override
    public Resource getParent() {
        return null;
    }

    @Override
    public void setParent(Resource parent) {
        System.err.println("Root can not have parent");
    }

    @Override
    public boolean isObservable() {
        return false;
    }

    @Override
    public void setObservable(boolean observable) {
        System.err.println("Root can not be observed");
    }
    @Override
    public void add(Resource child) {
        if (child == null || child.getName() == null) {
            System.err.println("Child name must not be null");
        } else {
            Resource parent = child.getParent();
            if (parent == this && children.get(child.getName()) == child) {
                return;
            }
            if (parent != null) {
                parent.delete(child.getName());
            }
            Resource previous = children.get(child.getName());
            if (previous != null && previous != child) {
                delete(previous.getName());
            }
            children.put(child.getName(), child);
            child.setParent(this);
        }
    }

    @Override
    public void delete(String childName) {
        Resource child = children.remove(childName);
        if (child != null) {
            child.setParent(null);
        }
    }
}

