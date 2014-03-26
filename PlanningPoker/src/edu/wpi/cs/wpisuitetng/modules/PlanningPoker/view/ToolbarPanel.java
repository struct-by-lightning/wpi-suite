package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.janeway.Janeway;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;

/**
 * This panel contains the refresh button
 * 
 * 
 * @author Batyrlan Nurbekov
 * @author Sam Mailand
 * @author Zachary Zapatka
 * @author Miguel Mora
 *
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

    /** The refresh button */
    private final JButton startTimer;

    /**
     * Construct the panel.
     */
    public ToolbarPanel() {
    	
        // Make this panel transparent, we want to see the JToolbar gradient beneath it
        this.setOpaque(false);

        // Construct the refresh button and add it to this panel
        startTimer = new JButton("Start Timer");
        startTimer.setHorizontalAlignment(SwingConstants.RIGHT);
        startTimer.setFont(new Font("Arial", Font.PLAIN, 15));
        startTimer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent clicked) {
                	Janeway.send_email();                
        	}
        });
        
        
        JLabel lblPlanningPoker = new JLabel("Planning Poker");
        lblPlanningPoker.setHorizontalAlignment(SwingConstants.LEFT);
        lblPlanningPoker.setFont(new Font("Arial", Font.PLAIN, 28));
        add(lblPlanningPoker);
        
        Component rigidArea = Box.createRigidArea(new Dimension(500, 20));
        add(rigidArea);
        add(startTimer);
        

    }
}
