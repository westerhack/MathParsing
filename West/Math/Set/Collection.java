package West.Math.Set;
import West.Math.MathObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.AbstractList;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.75
 */ 
public class Collection<E> extends AbstractList<E> implements MathObject{

    protected ArrayList<E> elements;

    // empty
    public Collection(){
        this(new ArrayList<E>());
    }

    public Collection(ArrayList<E> eles){
        elements = eles;
    }

    // returns a Collection
    public Collection addE(Object pObj){
        add((E)pObj);
        return this;
    }

    public Collection<E> addAllE(Object pObj){
        addAll(pObj);
        return this;
    }
    // adds elements. does not check to make sure they are actual valid elements
    @Override
    public boolean add(E pObj){
        return elements.add(pObj); //  might throw exception
    }

    @Override
    public void add(int pos, E pObj){
        elements.add(pos, pObj); //  might throw exception
    }

    public boolean addAll(Object pObj){
        assert pObj instanceof java.lang.Iterable || pObj instanceof Object[]: "Cannot addAll:" +pObj.getClass();
        if(pObj instanceof Object[])
            for(E ele : (E[])pObj)
                addE(ele);
        else
            ((java.lang.Iterable)pObj).forEach(this::addE);
        return true;
    }


    public ArrayList<E> elements(){ return elements; }
    public Collection setElements(ArrayList<E> pElements){
        elements = pElements;
        return this;
    }
    public int size(){ return elements.size();}
    public E get(int pPos){ return elements.get(stdPos(pPos));}
    public E set(int pPos, E pEle){
        return elements.set(stdPos(pPos), pEle);
    }
    public int stdPos(int pPos){return pPos < 0 ? size() + pPos : pPos;}
    public E pop(){return remove(-1);}
    public E pop(int pPos){return remove(pPos);}
    public E remove(int pPos){ return elements.remove(stdPos(pPos)); }

    public boolean isEmpty(){ return elements.isEmpty();}
    public List<E> subList(int start){
        return subList(start, size());
    }
    public List<E> subList(int start, int end){
        return elements.subList(stdPos(start), stdPos(end));
    }
    
    public void prepend(E obj){
        elements = new ArrayList<E>(){{add(obj); addAll(elements);}};
    }

    public Collection<E> empty(){
        elements = new ArrayList<E>();
        return this;
    }
    //  THIS ∪ PRGROUP
    public Collection<E> union(Collection<E> pCollection){
        return copy().addAllE(pCollection);
    }

    //  THIS ∩ PGROUP
    public Collection<E> intersect(Collection<E> pCollection){
        Collection<E> ret = new Collection<E>();
        for(E d : this)
            if(pCollection.contains(d))
                ret.add(d);
        return ret;
    }
    
    //  ¬ THIS
    public Collection<E> not(Collection<E> universe){
        return new Collection<E>(){{
            for(E e : universe)
                if(!contains(e))
                    add(e);
        }};
    }

    public boolean isUnique(){
        Collection<E> ms = copy();
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }

    public Collection<Integer> enumeration(){ // should be integer, but its this so it works with other things.
        int size = size();
        return new Collection<Integer>(){{
            for(int i = 0; i < size; i++)
                add(i);
        }};
    }

    public boolean contains(Object obj){
        return elements.contains(obj);
    }

    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) new Iter();
    }

    @Override
    public void forEach(Consumer<? super E> action){
        for(E e : elements)
            action.accept(e);
    }

    public Collection<E> sort(){
        assert size() > 0;
        assert get(0) instanceof java.lang.Comparable;
        E[] ele = (E[])elements.toArray();
        java.util.Arrays.sort(ele);
        return new Collection<E>().addAllE(ele);
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
        return elements.toString();
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

        int columns = 5; //  amount of columns
        int spacing = 10; //  amount of spaces. Note that there will be an additional one between columns
        for(E e : this)
            if(e.toString().length() > spacing)
                spacing = e.toString().length();
        String spaces = "";
        for(int i = 0; i < spacing; i++) spaces += " ";
        ret += indent(idtLvl + 1) + "Elements:";
        for(int i = 0; i < elements.size(); i++){
            if(i % columns == 0){
                if(i > elements.size() - columns - 1){
                    ret += "\n" + indentE(idtLvl + 2);
                } else {
                    ret += "\n" + indent(idtLvl + 2);
                }
            }
            ret += (get(i) + spaces).substring(0, spacing ) + ((i + 1) % columns == 0 ? "" : " # ");
        }
        return ret + "\n" + indentE(idtLvl + 1);

    }

    @Override
    public Collection<E> copy(){
        return new Collection<E>().addAllE(elements); // NOT SO SURE THAT THIS IS COPYING ELEMENTS...
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