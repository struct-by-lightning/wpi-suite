
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker;
 
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
 
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
 

public class PlanningPoker implements IJanewayModule {
               
                private ArrayList<JanewayTabModel> tabs;
               
                public PlanningPoker() {
                    // Initialize the list of tabs (however, this module has only one tab)
                    tabs = new ArrayList<JanewayTabModel>();

                    // Create a JPanel to hold the toolbar for the tab
                    JPanel toolbarPanel = new JPanel();
                    toolbarPanel.add(new JLabel("PlanningPoker toolbar placeholder")); // add a label with some placeholder text
                    toolbarPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2)); // add a border so you can see the panel

                    // Create a JPanel to hold the main contents of the tab
                    JPanel mainPanel = new JPanel();
                    mainPanel.add(new JLabel("PlanningPoker placeholder"));
                    mainPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));

                    // Create a tab model that contains the toolbar panel and the main content panel
                    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);

                    // Add the tab to the list of tabs owned by this module
                    tabs.add(tab1);
                }
                /*
                * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
                */
                @Override
                public String getName() {
                                // TODO Auto-generated method stub
                                return "Planning Poker";
                }
 
                /*
                * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
                */
                @Override
                public List<JanewayTabModel> getTabs() {
                                // TODO Auto-generated method stub
                                return tabs;
                }
 
}