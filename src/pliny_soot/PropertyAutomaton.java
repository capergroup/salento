/* Author: Vijay Murali */

package pliny_soot;

import pliny_soot.predicates.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

import soot.Unit;

/** A property automaton */
public class PropertyAutomaton {
    
    /** Read properties from a file and create the automata */
    public static List<PropertyAutomaton> readProperties(File f) throws FileNotFoundException, IOException {
        assert f.exists() : "cannot find properties file " + f;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        List<PropertyAutomaton> properties = new ArrayList<PropertyAutomaton>();

        PropertyAutomaton p = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#")) /* comment */
                continue;

            if (line.startsWith("p#")) { /* new automaton */
                if (p != null)
                    properties.add(p);
                p = new PropertyAutomaton();
                continue;
            }

            Pattern regex = Pattern.compile("(\\d+)->(\\d+) (\\w+) (.*)");
            Matcher m = regex.matcher(line.trim());

            assert m.matches() : "malformed property transition: " + line;

            try {
                PropertyState from = new PropertyState(Integer.parseInt(m.group(1)));
                PropertyState to = new PropertyState(Integer.parseInt(m.group(2)));
                String type = m.group(3);
                String rest = m.group(4);

                /* Add new predicate types here*/
                Transition t = new Transition(from, to,
                        type.equals("equality")  ? new EqualityPredicate(rest) :
                        type.equals("call")      ? new CallPredicate(rest) :
                        type.equals("arity")     ? new ArityPredicate(rest) :
                        null);
                p.addTransition(t);
            } catch (NumberFormatException e) {
                assert false : "invalid state: " + line;
            }
        }

        if (p != null)
            properties.add(p);

        return properties;
    }

    /** The internal automaton state */
    private PropertyState state;

    /** List of transitions between states */
    List<Transition> transitions;

    public PropertyAutomaton() {
        state = new PropertyState(0);
        transitions = new ArrayList<Transition>();
    }

    public PropertyAutomaton(List<Transition> transitions) {
        state = new PropertyState(0);
        this.transitions = transitions;
    }

    public PropertyState getState() {
        return state;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    /** Update internal states of each automaton if a transition is enabled */
    public void post(Unit stmt) {
        int enabled = 0;
        for (Transition t : transitions)
            if (state.equals(t.from()) && t.enabled(stmt)) {
                state = t.to();
                enabled++;
            }

        assert enabled <= 1 : "more than one enabled trans from " + state;
    }

    /** Update internal information regarding predicates according to this statement */
    public static void apply(Unit stmt) {
        EqualityPredicate.apply(stmt);
        CallPredicate.apply(stmt);
        ArityPredicate.apply(stmt);
    }
}
