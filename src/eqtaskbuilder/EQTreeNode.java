/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eqtaskbuilder;

import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author sai1boat
 */
public class EQTreeNode extends DefaultMutableTreeNode {
    
    HashMap userHashMap;
    boolean isZone;
    
    public EQTreeNode(String text){
        super(text);
        userHashMap = new HashMap();
        isZone = false;
    }
    
    public EQTreeNode(String text, boolean isZone){
        super(text);
        userHashMap = new HashMap();
        this.isZone = isZone;
    }
}
