package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PlayingCardJPanel extends JPanel {

    private final int value;
    private boolean selected;
    private Image img;

    /**
     * Inner panels for displaying this card.
     */
    private final JPanel innerCardPanel;
    private final JLabel cardLabel;

    public PlayingCardJPanel(int value, boolean selected) {
        this.value = value;
        this.selected = selected;
        
        this.updateBorder();

        this.innerCardPanel = new CardImgPanel();
        this.cardLabel = new JLabel();

        innerCardPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        cardLabel.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        cardLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cardLabel.setText(new Integer(value).toString());

       GroupLayout innerCardPanelLayout = new GroupLayout(innerCardPanel);
        innerCardPanel.setLayout(innerCardPanelLayout);
        innerCardPanelLayout.setHorizontalGroup(
                innerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(innerCardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addContainerGap())
        );
        
        innerCardPanelLayout.setVerticalGroup(
                innerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(innerCardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                        .addContainerGap())
        );

        GroupLayout thisLayout = new GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
                thisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(innerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        thisLayout.setVerticalGroup(
                thisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(innerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }

    public void toggle() {
        this.selected = !this.selected;
        this.updateBorder();
    }
    
    public void select() {
        this.selected = true;
        this.updateBorder();
    }
    
    public void deselect() {
        this.selected = false;
        this.updateBorder();
    }

    private void updateBorder() {
        Color borderColor = (this.selected ? new Color(204, 255, 204) : new Color(255, 255, 255));
        this.setBorder(BorderFactory.createLineBorder(borderColor, 10));
    }

    public int getValue() {
        return (this.selected ? value : 0);
    }

}
