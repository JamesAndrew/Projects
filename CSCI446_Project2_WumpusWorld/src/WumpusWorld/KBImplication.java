package WumpusWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to instantiate implication sentences such as:
 *   A v B => C
 *   Shiny(C) => HasGold(C)
 *   Windy(C_(x-1,y)) ^ Windy(C_(x+1,y)) => HasPit(C)
 * 
 * @author David Rice
 * @version October 2016
 */
public class KBImplication 
{
    // left hand side atoms
    List<KBAtomConstant> premise;
    // right hand side atom
    KBAtomConstant consequent;
    // distingustes wheter the premise is made of disjunctions or conjunctions
    KBOperator operator;
    
    public KBImplication(List<KBAtomConstant> premise, KBAtomConstant consequent, KBOperator operator)
    {
        this.premise = premise;
        this.consequent = consequent;
        this.operator = operator;        
    }
}
