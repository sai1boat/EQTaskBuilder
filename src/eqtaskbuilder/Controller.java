/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eqtaskbuilder;

import static eqtaskbuilder.MainWindow.con;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 *
 * @author anon
 */
public class Controller {
    
    
    
    public static void connect(){
        
        String ip = Preferences.userRoot().get("com.sai1boat.eq.eqtaskbuilder.ip", "");
        String un = Preferences.userRoot().get("com.sai1boat.eq.eqtaskbuilder.un", "");
        String pw = Preferences.userRoot().get("com.sai1boat.eq.eqtaskbuilder.pw", "");
        String schema = Preferences.userRoot().get("com.sai1boat.eq.eqtaskbuilder.schema", "");
        
        
        
        if(ip.equals("") || un.equals("") || pw.equals("") || con!=null){
            return;
        }
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+schema+"?user="+un+"&password="+pw);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage().substring(1, 400));
        }
        catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public static EQTreeNode getZoneTaskNodes(){
        
        if(con == null){
            EQTreeNode root = new EQTreeNode("Tasks By Zone");
            root.add(new EQTreeNode("Blank"));
            return root;
        }
        
        ResultSet rs_zoneTask = getZoneTaskResultSet();
        EQTreeNode root = new EQTreeNode("Tasks By Zone");
        try{
            String curZone = "";
            EQTreeNode curZoneNode = null;
            if(rs_zoneTask!=null){
                
                ResultSetMetaData meta = rs_zoneTask.getMetaData();
                
                while(rs_zoneTask.next()){
                    
                    //the zones are the folders
                    if(!curZone.equals(rs_zoneTask.getString("long_name"))){
                        
                        if(curZoneNode!=null)
                            root.add(curZoneNode);
                        
                        curZone = rs_zoneTask.getString("long_name");
                        curZoneNode = new EQTreeNode(curZone);
                        
                    }
                    
                    //the task
                    EQTreeNode node = new EQTreeNode(rs_zoneTask.getString("t.title"));
                    HashMap hm = new HashMap();
                    for(int i=1; i<meta.getColumnCount()+1;i++){
                        hm.put(meta.getColumnName(i), rs_zoneTask.getString(i));
                    }
                    node.userHashMap = hm;
                    
                    if(curZoneNode!=null)
                        curZoneNode.add(node);
                    
                }
            }
            
            if(rs_zoneTask!=null)
                rs_zoneTask.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        
        
        
        return root;
    }
    
    public static ResultSet  getZonesResultSet(){
        
        ResultSet rs_zones = null;
        try{
            rs_zones = con.prepareStatement("SELECT id, short_name, long_name FROM zones").executeQuery();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rs_zones;
    }
    
    public static ArrayList getZonesArrayList(){
        
        ResultSet rs_zones = getZonesResultSet();
        ArrayList zones = new ArrayList();
        try{
            if(rs_zones!=null){
                while(rs_zones.next()){
                    zones.add(rs_zones.getString("long_name"));
                    
                }
            }
            
            if(rs_zones!=null)
                rs_zones.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        return zones;
    }
    
    public static ResultSet getZoneTaskResultSet(){
        ResultSet rs_zoneTask = null;
        try{
            rs_zoneTask = con.prepareStatement("select z.id, z.long_name, z.short_name, t.id, t.duration, "
                    + "t.title, t.description, t.reward, t.rewardid, t.cashreward, t.xpreward, t.rewardmethod, "
                    + "t.startzone, t.minlevel, t.maxlevel, t.repeatable from zone z "
                    + "INNER JOIN tasks t ON t.startzone = z.id ORDER BY z.long_name").executeQuery();
            
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rs_zoneTask;
    }
    
    
    
    public static ResultSet getItemsByNameResultSet(String pattern){
        
        if(con==null)
            return null;
        
        ResultSet rs = null;
        
        try{

            rs = con.prepareStatement("SELECT i.id as id, i.name as name FROM items " +
                    " i WHERE LOWER(i.name) LIKE LOWER('%"+pattern+"%')").executeQuery();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rs;
    }
    
    
}