/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Common.RefCounted;
import Render.ViewPort;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javax.swing.JComponent;

/**
 *
 * @author T3ee
 */
public abstract class Controller extends RefCounted{
    protected ViewPort m_pViewPort = null;
    protected SubScene m_pScene = null;
    protected SubScene m_pOverlayScene = null;
    protected Point2D m_pMousePos;
    protected Point2D m_pPrevMousePos;
    protected boolean m_bMouseDown = false;
    protected Point2D m_pMouseDownPos;
    protected MouseButton m_eMouseDownButton;
    protected double m_dMouseWheelZoomFactor;
    protected boolean m_bHasDblClicked = false;
    protected boolean m_bActive = false;
    protected boolean m_bMouseIn = false;
    protected ArrayList<CloseCallback> m_fOnCloseCB = null;
    protected MouseAndKeyCallbacks m_fMouseAndKeyCB = null;
    protected JComponent m_pControl = null;
    
    public void SetViewPort(ViewPort viewPort){
        if(viewPort == null)return;
        if(m_pViewPort != null)m_pViewPort.Ref();
        if(m_pViewPort != null)try {
            m_pViewPort.UnRef();
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_pViewPort = viewPort;
    }
    
    public void SetScene(SubScene scene){
        if(scene == null)return;
        m_pScene = scene;
    }
    
    public void SetOverlayScene(SubScene scene){
        if(scene == null)return;
        m_pOverlayScene = scene;
    }
    
    public void SetMouseWheelZoomFactor(double factor){
        m_dMouseWheelZoomFactor = factor;
    }
    
    public void MouseMove(javafx.scene.input.MouseEvent evt){
        if(m_fMouseAndKeyCB != null){
            m_fMouseAndKeyCB.OnMouseMove(evt);
        }
    }
    
    public void MouseDown(javafx.scene.input.MouseEvent evt){
        if(m_fMouseAndKeyCB != null){
            m_fMouseAndKeyCB.OnMouseDown(evt);
        }
    }
    
    public void MouseEnter(javafx.scene.input.MouseEvent evt){
        if(m_fMouseAndKeyCB != null){
            m_fMouseAndKeyCB.OnMouseEnter(evt);
        }
    }
    
    public void MouseLeave(javafx.scene.input.MouseEvent evt){
        if(m_fMouseAndKeyCB != null){
            m_fMouseAndKeyCB.OnMouseLeave(evt);
        }
    }
    
    public void DblClick(){
        // no-op
    }
    
    public void KeyPress(javafx.scene.input.KeyEvent evt){
        // no-op
    }
    
    // tick should be called in the rendering loop to allow the controller to
    // update itself
    protected void Tick(){
        if(m_bActive){
            UpdateCursor();
        }
    }
    
    public void Close(){
        if(m_fOnCloseCB == null)return;
        for(CloseCallback fOnClose :  m_fOnCloseCB){
            fOnClose.OnClose();
        }
    }
    
    public void RegisterForClose(CloseCallback fOnClose){
        if(m_fOnCloseCB == null) m_fOnCloseCB = new ArrayList<>();
        m_fOnCloseCB.add(fOnClose);
    }
    
    public void UnRegisterForClose(CloseCallback fOnClose){
        if(m_fOnCloseCB == null)return;
        if(m_fOnCloseCB.indexOf(fOnClose) != -1){
            m_fOnCloseCB.remove(fOnClose);
        }
    }
    
    public void Cancel(){
        // no-op
    }
    
    public void Activate(){
        if(m_bActive) return;
        m_bActive = true;
    }
    
    public void DeActivate(){
        m_bActive = false;
    }
    
    public void ResetMouseState(){
        m_bHasDblClicked = false;
    }

    public ViewPort GetViewPort() {
        return m_pViewPort;
    }

    public SubScene GetScene() {
        return m_pScene;
    }

    public SubScene GetOverlayScene() {
        return m_pOverlayScene;
    }

    public double GetMouseWheelZoomFactor() {
        return m_dMouseWheelZoomFactor;
    }

    public boolean isHasDblClicked() {
        return m_bHasDblClicked;
    }

    public void SetHasDblClicked(boolean m_bHasDblClicked) {
        this.m_bHasDblClicked = m_bHasDblClicked;
    }

    public MouseAndKeyCallbacks GetMouseAndKeyCB() {
        return m_fMouseAndKeyCB;
    }

    public void SetMouseAndKeyCB(MouseAndKeyCallbacks m_fMouseAndKeyCB) {
        this.m_fMouseAndKeyCB = m_fMouseAndKeyCB;
    }
    
    @Override
    public void Release() throws Exception{
        if(m_pViewPort != null)m_pViewPort.UnRef();
         if(m_fOnCloseCB != null)m_fOnCloseCB.clear();
         m_pViewPort = null;
         m_fOnCloseCB = null;
         m_fMouseAndKeyCB = null;
         m_pControl = null;
        super.Release();
    }
    
    
    abstract public void KeyDown(javafx.scene.input.KeyEvent evt);
    
    abstract public void KeyUp(javafx.scene.input.KeyEvent evt);
    
    abstract public void MouseUp(javafx.scene.input.MouseEvent evt);
    
    abstract public void MouseWhellDown(javafx.scene.input.MouseEvent evt);
    
    abstract public void MouseWheelUp(javafx.scene.input.MouseEvent evt);
    
    abstract protected void UpdateCursor();
    
}
