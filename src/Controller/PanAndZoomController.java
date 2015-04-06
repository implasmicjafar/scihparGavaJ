/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.MouseInfo;
import java.awt.Point;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javax.swing.JComponent;

/**
 *
 * @authorJafaru Mohammed
 */
public class PanAndZoomController extends MouseAndKeyController {
    
    private static final double MAX_DT = 0.1;
    private static final double MAX_VEL = 10000000;
    private static final double ZVEL_DELTA = 0.04;
    private static final boolean SMOOTH_ZOOM = true;
    
    public enum PanZoomControllerState {Idle,Pan,Panning,Zoom,Zooming}
    
    private boolean m_bPanning = false;
    private double m_dPanX, m_dPanY, m_dZoomY, m_dPanVX, m_dPanVY, m_dPanVZ;
    private Point2D m_dZoomPt;
    private double m_dLastTick;
    private boolean m_bMouseZoom;
    private double m_dMzX, m_dMzY;
    private PanZoomControllerState m_eState;
    private final boolean m_bRightMouseZoom;
    
    public PanAndZoomController(JComponent parent, boolean bRightMouseZoom) {
        super(parent);
        m_dLastTick = 0.0;
        m_bPanning = false;
        m_bMouseZoom = false;
        m_bRightMouseZoom = bRightMouseZoom;
        m_dPanVX = 0.0;
        m_dPanVY = 0.0;
        m_dPanVZ = 0.0;
        m_dZoomY =  0;
        m_dPanY = 0;
        m_dPanX = 0;
        m_dMzX = 0;
        m_dMzY = 0;
        m_eState = PanZoomControllerState.Idle;
    }
    
    @Override
    public void Activate(){
        super.Activate();
    }
    
    @Override
    public void DeActivate(){
        super.DeActivate();
        m_dPanVX = 0.0;
        m_dPanVY = 0.0;
    }
    
    private double Getdt(){
        double currentTick = System.currentTimeMillis()*1.0;
        double resu = (currentTick - m_dLastTick)/1000.0;
        m_dLastTick = currentTick;
        
        if(resu < 0.0)resu = 0.0;
        if(resu > MAX_DT)resu = MAX_DT;
        
        return resu;
    }
    
    @Override
    public void KeyUp(KeyEvent evt){
        // no-op
    }
    
    @Override
    public void MouseDown(MouseEvent evt){
        super.MouseDown(evt);
        if(evt.getButton() == MouseButton.PRIMARY){
            m_dPanX = evt.getScreenX();
            m_dPanY = evt.getScreenY();
            m_bPanning = true;
            m_eState = PanZoomControllerState.Pan;
        }else if((evt.getButton() == MouseButton.SECONDARY) && m_bRightMouseZoom){
            m_dZoomY = evt.getScreenY();
            m_bMouseZoom = true;
            m_dMzX = evt.getScreenX();
            m_dMzY = evt.getScreenY();
        }
    }
    
    @Override
    public void MouseEnter(MouseEvent evt){
        super.MouseEnter(evt);
    }
    
    @Override
    public void MouseLeave(MouseEvent evt){
        m_bMouseZoom = false;
        m_bPanning = false;
        super.MouseLeave(evt);
    }
    
    @Override
    public void MouseMove(MouseEvent evt){
        if(m_bPanning){
            PanZoomControllerState caser = m_eState;
            if(m_eState == PanZoomControllerState.Panning) caser = PanZoomControllerState.Pan;
            switch(caser){
                case Pan: 
                    Point2D rLocal = m_pScene.screenToLocal(m_dPanX - evt.getScreenX(), m_dPanY - evt.getScreenY());
                    double dt = Getdt();
                    // have pan match mouse exactly when mouse button is down
                    m_pViewPort.GetOverviewCamera().setTranslateX(m_pViewPort.GetOverviewCamera().getTranslateX() + rLocal.getX());
                    m_pViewPort.GetOverviewCamera().setTranslateY(m_pViewPort.GetOverviewCamera().getTranslateY() + rLocal.getY());
                    m_dPanX = evt.getScreenX();
                    m_dPanY = evt.getScreenY();
                    m_eState = PanZoomControllerState.Panning;
                    // Calculate the pan velocity
                    if(dt > 0.0){
                        double vx = rLocal.getX() / dt;
                        double vy = rLocal.getY() / dt;
                        // smoothly update the pan velocity with user input
                        m_dPanVX = 0.5 * m_dPanVX + 0.5 * vx;
                        m_dPanVY = 0.5 * m_dPanVY + 0.5 * vy;                        
                    }
                    break;
            }
        }
        
        if(m_bMouseZoom){
            m_pViewPort.ScaleAroundLocalPoint(m_dMzX, m_dMzY, (1.0  + (evt.getScreenY() - m_dZoomY)*0.01));
            m_dZoomY = evt.getScreenY();
        }
        
        super.MouseMove(evt);
    }
    
    @Override
    public void KeyDown(KeyEvent evt){
        Point2D rMousePos;       
        int charValue = ((int)evt.getCharacter().charAt(0)); 
        if(charValue == 34){
            Point p = MouseInfo.getPointerInfo().getLocation();
            rMousePos = new Point2D(p.getX(),p.getY());            
            if(m_bMouseIn){
                Point2D rLocal = m_pScene.screenToLocal(rMousePos);
                if(SMOOTH_ZOOM){
                    m_dPanVZ = m_dPanVZ - ZVEL_DELTA*((m_dMouseWheelZoomFactor-0.95)*3+0.95);
                    m_dZoomPt = rLocal;
                }else{
                    m_pViewPort.ScaleAroundLocalPoint(rLocal.getX(), rLocal.getY(), 1/m_dMouseWheelZoomFactor);
                }
                evt.consume();
            }                                    
        }else if(charValue == 34){
            Point p = MouseInfo.getPointerInfo().getLocation();
            rMousePos = new Point2D(p.getX(),p.getY());
            if(m_bMouseIn){
                Point2D rLocal = m_pScene.screenToLocal(rMousePos);
                if(SMOOTH_ZOOM){
                    m_dPanVZ = m_dPanVZ + ZVEL_DELTA*((m_dMouseWheelZoomFactor-0.95)*3+0.95);
                    m_dZoomPt = rLocal;
                }else{
                    m_pViewPort.ScaleAroundLocalPoint(rLocal.getX(), rLocal.getY(), m_dMouseWheelZoomFactor);
                }
                evt.consume();
            }  
        }
    }
    
    @Override
    public void MouseWhellDown(MouseEvent evt){
        if(m_bMouseIn){
            Point2D rLocal = m_pScene.screenToLocal(evt.getScreenX(), evt.getScreenY());
            if(SMOOTH_ZOOM){
                m_dPanVZ = m_dPanVZ - ZVEL_DELTA*((m_dMouseWheelZoomFactor-0.95)*3+0.95);
                m_dZoomPt = rLocal;
            }else{
                m_pViewPort.ScaleAroundLocalPoint(rLocal.getX(), rLocal.getY(), 1/m_dMouseWheelZoomFactor);
            }
            evt.consume();
        }
    }
    
    
    @Override
    public void MouseWheelUp(MouseEvent evt){
        if(m_bMouseIn){
            Point2D rLocal = m_pScene.screenToLocal(evt.getScreenX(), evt.getScreenY());
            if(SMOOTH_ZOOM){
                m_dPanVZ = m_dPanVZ + ZVEL_DELTA*((m_dMouseWheelZoomFactor-0.95)*3+0.95);
                m_dZoomPt = rLocal;
            }else{
                m_pViewPort.ScaleAroundLocalPoint(rLocal.getX(), rLocal.getY(), m_dMouseWheelZoomFactor);
            }
        }
    }
    
    @Override
    public void MouseUp(MouseEvent evt){
        if(evt.getButton() == MouseButton.PRIMARY){
            m_bPanning = false;
        }else if((evt.getButton() == MouseButton.SECONDARY) && m_bRightMouseZoom){
            m_bMouseZoom = false; 
        }
        m_eState = PanZoomControllerState.Idle;
    }
    
    @Override
    protected void Tick(){
        super.Tick();
        double dt = Getdt();
        if((m_dPanVX != 0) || (m_dPanVY != 0)){
            //drift according to the velocity after the user lets go
            if(!m_bPanning){
                double dx = m_dPanVX*dt;
                double dy = m_dPanVY*dt;
                m_pViewPort.GetOverviewCamera().setTranslateX(m_pViewPort.GetOverviewCamera().getTranslateX() + dx);
                m_pViewPort.GetOverviewCamera().setTranslateX(m_pViewPort.GetOverviewCamera().getTranslateY() + dy);
            }
            
            // decay the pan velocity smoothly over time
            for(int i = 0; i < Math.round(dt*1000.0); i ++){
                m_dPanVX = m_dPanVX * 0.992; // decay with a time constant
                m_dPanVY = m_dPanVY * 0.992;
            }
            
            // clamp the velocity to zero when it gets slow
            if (Math.abs(m_dPanVX * m_dPanVX + m_dPanVY * m_dPanVY) < 10.0){
                m_dPanVX = 0.0;
                m_dPanVY = 0.0;
            }
        }
        if(SMOOTH_ZOOM && (m_dPanVZ != 0.0)){
            m_pViewPort.ScaleAroundLocalPoint(m_dZoomPt.getX(), m_dZoomPt.getY(), 1 - m_dPanVZ);
            for (int i = 0; i < Math.round(dt*1000.0); i++){
                m_dPanVZ = m_dPanVZ * 0.99; // decay with a time constant
            }
            if(Math.abs(m_dPanVZ) < 0.001) m_dPanVZ = 0.0;
        }
    }
    
    @Override
    public void UpdateCursor(){
        if((!m_bActive) || (!m_bMouseIn))return;
        Cursor c = Cursor.DEFAULT;
        switch(this.m_eState){
            case Pan: c = Cursor.OPEN_HAND; break;
            case Panning: c = Cursor.CLOSED_HAND; break;
            case Zoom: c = Cursor.DEFAULT; break;
            case Zooming: c = Cursor.DEFAULT; break;
            case Idle: c = Cursor.OPEN_HAND;                
        }
        m_pScene.setCursor(c);
    }
}
