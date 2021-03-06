package West.Math.Display;

import West.Math.MathObject;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Equation;
import West.Math.Set.NumberCollection;
import West.Math.Set.Collection;
import java.util.List;
import West.Math.Display.GraphComponents;
import West.Math.Complex;
 
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.*;

import javax.accessibility.*;
 
import java.awt.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;


/**
 * Define
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.2
 */
public class Grapher extends JPanel implements MathObject {

    /** TODO: JAVADOC */
    public static final Color[] COLORS = new Color[]{
        Color.BLUE, Color.RED, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.GREEN, Color.YELLOW};

    /** TODO: JAVADOC */
    protected JLayeredPane layeredPane;

    /*
    TODO: JAVADOC
    Inner to outermost
    Complex -> An axis (x / y) -> whole data set (both axis) -> multiple data sets
    */
    protected List<List<NumberCollection<Complex>>> numcs;

    /** TODO: JAVADOC */
    protected EquationSystem equationsToGraph;

    /** TODO: JAVADOC */
    protected EquationSystem equationsToUse;

    /** TODO: JAVADOC */
    protected GraphComponents components;

    /** TODO: JAVADOC */
    protected List<DisplayComponent> displays;

    /** TODO: JAVADOC */
    public Grapher() {
        this(null, null, null, new GraphComponents());
    }

    /** TODO: JAVADOC */
    public Grapher(NumberCollection<Complex> pnumc1, NumberCollection<Complex> pnumc2) {
        this(null, null, new Collection<List<NumberCollection<Complex>>>(){{
            add(new Collection<NumberCollection<Complex>>());
            get(-1).add(pnumc1);
            get(-1).add(pnumc2);
        }}, new GraphComponents());
    }

    /** TODO: JAVADOC */
    public Grapher(final EquationSystem pEqSysToGraph,
                   final EquationSystem pEqSysToUse,
                   List<List<NumberCollection<Complex>>> pNumberCollections,
                   GraphComponents pGraph) {
        numcs = pNumberCollections;
        equationsToGraph = pEqSysToGraph;
        equationsToUse = pEqSysToUse;
        if(pEqSysToGraph == null)
            equationsToGraph = new EquationSystem();
        if(pEqSysToUse == null)
            equationsToUse = pEqSysToGraph;
        if(pNumberCollections == null)
            numcs = new Collection<List<NumberCollection<Complex>>>();
        components = pGraph;
        displays = new Collection<DisplayComponent>();
        displays.add(new DisplayComponent(this)); // adds axis
        for(int i = 0; i < equationsToGraph.size(); i++){
            displays.add(new DisplayComponent(this, equationsToGraph.equations().get(i),
                    equationsToUse.clone()/*.add(equationsToGraph)*/, COLORS[i % COLORS.length]));
        }
        for(int i = 0; i < numcs.size(); i++){
            displays.add(new DisplayComponent(this, numcs.get(i), COLORS[i % COLORS.length]));
        }
        graphSetup();
    }

    /** TODO: JAVADOC */
    private void graphSetup() {

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
         // Create and set up the layered pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(components.winBounds()[0] + 10, components.winBounds()[1] + 10));
        //  layeredPane.setBorder(BorderFactory.createTitledBorder("Graph"));
  
        for (int i = 0; i < displays.size(); i++) {
            JLabel label = createDisplay(i);
            layeredPane.add(label, new Integer(i));
        }


        // Add control pane and layered pane to this JPanel.
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(layeredPane);
    }

    /** TODO: JAVADOC */
    private JLabel createDisplay(int i) {
        JLabel label = displays.get(i);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(5, 0, components.winBounds()[0], components.winBounds()[1]);

        //  label.setBounds(5, 20, components.winBounds()[0], components.winBounds()[1] - 20);
        return label;
    }

    /** TODO: JAVADOC */
    public void graph() {
        // Create and set up the window.
        String title = "Graph of ";
        if(equationsToGraph.size() + numcs.size() > 3) {
            title += "A lot of stuff";
        } else if(equationsToGraph.size() + numcs.size() == 0) {
            title += "Nothing...? Lol why graph that.";
        } else if(equationsToGraph.size() + numcs.size() == 1) {
            title += equationsToGraph.size() == 1 ? equationsToGraph.equations().get(0) : numcs.get(0);
        } else {
            for(int i = 0; i < equationsToGraph.size(); i++) { //  for each loop will crash if equation's size is 0.
                title += equationsToGraph.equations().get(i) + ", ";
            }
            for(int i =0; i < numcs.size(); i++) {
                 title += numcs.get(i) + ", ";   
            }
            title = title.substring(0, title.length() - 2);

        }
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        this.setOpaque(true); 
        frame.setContentPane(this);
 
        frame.pack();
        frame.setVisible(true);
    }

    /** TODO: JAVADOC */
    public List<List<NumberCollection<Complex>>> numcs() { return numcs; }

    /** TODO: JAVADOC */
    public EquationSystem equationsToGraph() { return equationsToGraph; }

    /** TODO: JAVADOC */
    public EquationSystem equationsToUse() { return equationsToUse; }
    /** TODO: JAVADOC */
    public GraphComponents components() { return components; }

    @Override
    public String toString() {
        //  String ret = "Graph of ";
        //  if(numcs == null && equationsToGraph == null || (numcs.size() == 0 && equationsToGraph.size() == 0)) {
        //      return "Empty Graph"; 
        //  } else if(equationsToGraph.size() + numcs.size() == 1) {
        //      return ret + (equationsToGraph.size() == 1 ? equationsToGraph.equations().get(0) : numcs.get(0));
        //  } else {
        //      for(int i = 0; i < equationsToGraph.size(); i++) { //  for each loop will crash if equation's size is 0.
        //          ret += equationsToGraph.equations().get(i) + ", ";
        //      }
        //      for(int i =0; i < numcs.size(); i++) {
        //           ret += numcs.get(i) + ", ";   
        //      }
        //      return ret.substring(0, ret.length() - 2);

        //  }
        return "";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Grapher:";
        ret += "\n" + indent(idtLvl + 1) + "NumberCollections:";
        for(List<NumberCollection<Complex>> sa : numcs){
            assert sa.size() == 2 && sa.get(0).size() == sa.get(1).size() : sa;
            for(NumberCollection<Complex> s : sa)
                ret += "\n" + s.toFancyString(idtLvl + 2);
        }
        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Equations to Graph:\n" + equationsToGraph.toFancyString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Graphing Components:\n" + components.toFancyString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Displays:";
        if(displays.size() == 0)
            ret += "\n" + indent(idtLvl + 2) + "null";
        for(DisplayComponent d : displays)
            ret += "\n" + d.toFancyString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 1);
        return ret;
    }


    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Grapher:";
        ret += "\n" + indent(idtLvl + 1) + "NumberCollections:";

        for(List<NumberCollection<Complex>> sa : numcs){
            assert sa.size() == 2 && sa.get(0).size() == sa.get(1).size();
            for(NumberCollection<Complex> s : sa)
                ret += "\n" + s.toFullString(idtLvl + 2);
        }

        if(numcs == null || numcs.size() == 0)
            ret += "\n" + indent(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Equations to Graph:";

        if(equationsToGraph != null){
            ret += "\n" + equationsToGraph.toFullString(idtLvl + 2);
        }

        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Equations to Use:";

        if(equationsToUse != null){
            ret += "\n" + equationsToUse.toFullString(idtLvl + 2);
        }

        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) +"Graphing Components:\n" + components.toFullString(idtLvl + 2);

        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Displays:";
        if(displays.size() == 0)
            ret += "\n" + indent(idtLvl + 2);
        for(DisplayComponent d : displays)
            ret += "\n" + d.toFullString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2    );
        ret += "\n" + indentE(idtLvl + 1);
        return ret;
    }


    @Override
    public Grapher clone(){
        return new Grapher(equationsToGraph,equationsToUse,numcs, components);

    }

    /**
    * Note that this doesnt consider displays.
     * TODO: JAVADOC
     */
    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Grapher))
            return false;
        if(this == pObj)
            return true;
        Grapher pgrapher = (Grapher)pObj;
        if(!numcs.equals(pgrapher.numcs))
            return false;
        if(!equationsToGraph.equals(pgrapher.equationsToGraph()))
            return false;
        if(!equationsToUse.equals(pgrapher.equationsToUse()))
            return false;
        if(!components.equals(pgrapher.components()))
            return false;
        return true;
    }
}