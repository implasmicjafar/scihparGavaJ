/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagraphics;

import View.BasicView;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.swing.JFrame;

/**
 *
 * @author T3ee
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frame = new JFrame("BasicView");
        frame.setSize(1024, 1024);
        final BasicView view = new BasicView();
        frame.add(view);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (!view.isInitialized()) {

        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Rectangle r = new Rectangle();
                r.setX(50);
                r.setY(50);
                r.setWidth(500);
                r.setHeight(500);
                r.setArcWidth(20);
                r.setArcHeight(20);
                r.setFill(Color.BLUE);
               
                //view.GetRenderSurface().m_pRoot.getChildren().add(r);
                view.GetRenderSurface().AddDrawable(r, 0);
                view.GetRenderSurface().SetOverviewLayerMask(0, true);
                
                view.UpdateSize();
            }
        });

    }

}
