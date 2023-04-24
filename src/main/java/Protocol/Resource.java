package Protocol;

public interface Resource {

    public String getName();
    public void setName(String name);
    public Resource getChild(String name);
    public Resource getParent();
    public void setParent(Resource parent);
    public boolean isObservable();
    public void setObservable(boolean observable);
    public void add(Resource child);
    public void delete(String childName);
    public int hashCode();
}
