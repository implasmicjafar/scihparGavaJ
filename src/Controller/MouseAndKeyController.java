/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import javax.swing.JComponent;

/**
 *
 * @author T3ee
 */
public class MouseAndKeyController extends Controller{
    
    protected Timeline tickTimeline = null;
    private boolean inTick = false;
    
    public MouseAndKeyController(JComponent parent){
        super();
        m_fOnCloseCB = new ArrayList<>();
        m_pControl = parent;
        m_pViewPort = null;
        m_pScene = null;
        m_bHasDblClicked = false;
        m_dMouseWheelZoomFactor = 0.95;
        m_bActive = false;
        m_bMouseIn = true;        
        tickTimeline = new Timeline(new KeyFrame(Duration.millis(50), ae -> this.PreTick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE);
        tickTimeline.play();        
    }
    
    private void PreTick(){
        if(m_bActive && !inTick){
            inTick = true;
            final MouseAndKeyController control = this;
            Platform.runLater(new Runnable() {                
                @Override
                public void run() {
                    control.Tick();
                    inTick = false;
                }
            });            
        }
    }

    @Override
    public void KeyDown(KeyEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void KeyUp(KeyEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void MouseUp(MouseEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void MouseWheelDown(ScrollEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void MouseWheelUp(ScrollEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void UpdateCursor() {
        //no-op
    }
    
    @Override
    public void Release() throws Exception{
        if(tickTimeline != null){
            tickTimeline.stop();
        }
        super.Release();
    }
    
    
}
