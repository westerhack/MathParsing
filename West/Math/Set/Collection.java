package West.Math.Set;
import West.Math.MathObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.75
 */ 
public class Collection<E> extends java.util.ArrayList<E> implements MathObject{

    protected ArrayList<E> elements;

    //empty
    public Collection(){
        this(new ArrayList<E>());
    }


    public Collection(ArrayList<E> pElements){
        elements = new ArrayList<E>();
        add(pElements);
    }

    public Collection(E[] pElements) {
        elements = new ArrayList<E>();
        add(pElements);
    }

    public Collection(Collection<E> pCollection) {
        elements = new ArrayList<E>();
        add(pCollection);
    }

    //add an array of elements
    public Collection<E> add(E[] pElements){
        assert pElements != null : "pElements cannot be null!";
        assert pElements.length != 0 : "pElements's length can't be 0";
        for(E e : pElements)
            elements.add(e);
        return this;
    }

    // add an element
    public boolean add(E pElement){
        assert pElement != null : "pElement cannot be null!";
        elements.add(pElement);
        return true; //false of it cannot add. used for subsets.
    }

    public Collection<E> add(ArrayList<E> pElements){ //used so you can build elements
        assert pElements != null : "pElements cannot be null!";
        elements.addAll(pElements);

        return this;
    }

    public Collection<E> add(Collection<E> pCollection){ //used so you can build elements
        assert pCollection != null : "pCollection cannot be null!";
        elements.addAll(pCollection.copy().elements());
        return this;
    }

    public ArrayList<E> elements(){ return elements; }
    public int size(){ return elements.size();}
    public E get(int pPos){ return elements.get(pPos);}
    public E set(int pPos, E pEle){
        E ret = get(pPos);
        elements.set(pPos, pEle);
        return ret;
    }
    public E pop(){return remove(size() - 1);}
    public E pop(int pPos){return remove(pPos);}
    public E remove(int pPos){
        E ret = elements.get(pPos);
        elements.remove(pPos);
        return ret;
    }

    public void prepend(E obj){
        elements = new ArrayList<E>(){{add(obj); addAll(elements);}};
        // elements.remove(pPos);
    }

    // THIS ∪ PRGROUP
    public Collection<E> union(Collection<E> pCollection){
        return copy().add(pCollection);
    }

    // THIS ∩ PGROUP
    public Collection<E> intersect(Collection<E> pCollection){
        Collection<E> ret = new Collection<E>();
        for(E d : this)
            if(pCollection.contains(d))
                ret.add(d);
        return ret;
    }
    
    // ¬ THIS
    public Collection<E> not(Collection<E> universe){
        return new Collection<E>(){{
            for(E e : universe)
                if(!contains(e))
                    add(e);
        }};
    }

    public boolean isUnique(){
        Collection<E> ms = new Collection<E>(this);
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }

    public Collection<Integer> enumeration(){ //should be integer, but its this so it works with other things.
        int size = size();
        return new Collection<Integer>(){{
            for(int i = 0; i < size; i++)
                add(i);
        }};
    }
    public boolean contains(Object obj){
        return elements.contains(obj);
    }
    public Iterator<E> iterator() {
        return (Iterator<E>) new Iter();
    }
    private class Iter implements Iterator<E> {
        private int i = 0;

        public boolean hasNext() {
            return i < Collection.this.size();
        }
        public E next() {
            assert hasNext() : "there should be a next one.";
            return Collection.this.get(i++);
        }
    }


    @Override
    public String toString() {
        return "Collection: elements = " + elements;
    }

    @Override
    public String toFancyString(int idtLvl) {
        if(elements.size() == 0)
            return indent(idtLvl) + "Collection: Empty";
        return toFullString(idtLvl);
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Collection:\n";

        final int columns = 5; // amount of columns
        final int spacing = 20; // amount of spaces. Note that there will be an additional one between columns
        String spaces = "";
        for(int i = 0; i < spacing; i++) spaces += " ";

        ret += indent(idtLvl + 1) + "Elements:";
        for(int i = 0; i < elements.size(); i++){
            if(i % columns == 0)
                ret += "\n" + indent(idtLvl + 2);
            ret += "'"+(get(i) + spaces).substring(0, spacing) + "', ";
        }
        return ret;

    }

    @Override
    public Collection<E> copy(){
        return new Collection<E>(elements);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Collection))
            return false;
        if(pObj == this)
            return true;
        Collection<E> pelements = (Collection<E>)pObj;
        if(size() != pelements.size())
            return false;
        for(int i = 0; i < elements.size(); i++)
            if(!get(i).equals(pelements.get(i)))
                return false;
        return true;

    }
}