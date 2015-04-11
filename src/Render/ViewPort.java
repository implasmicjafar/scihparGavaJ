/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Render;

import Common.RefCounted;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;

/**
 *
 * @author T3ee
 */
public final class ViewPort extends RefCounted {
    
    private Camera m_pOverviewCamera = null;
    private Camera m_pOverlayCamera = null;
    private boolean m_bZEnabled = false;
    private boolean objValid = false;
    
    public ViewPort(){
        super();
        // m_pViewCamera contains everything necessary to set the view or measure
        // the vieport. No need for raw matrix since one can rotate, translate, or invert the view as needed.              
        m_pOverviewCamera = new ParallelCamera();  
        m_pOverlayCamera = new ParallelCamera();
        //this.SetWidth(10000.0);               
        objValid = true;
    }
    
    public void ScaleAroundLocalPoint(double xLocal, double yLocal, double factor){
        Point2D pt =  m_pOverviewCamera.localToScene(xLocal, yLocal);
        
        double oneMinusF = 1.0 - factor;
        double Ba = (pt.getX() - m_pOverviewCamera.getTranslateX());
        double Bb = (pt.getY() - m_pOverviewCamera.getTranslateY());
        double Ca = oneMinusF * Ba;
        double Cb = 0;
        if(Ba != 0)
            Cb = (Bb * Ca)/Ba;        
        m_pOverviewCamera.setTranslateX(m_pOverviewCamera.getTranslateX() + Ca);
        m_pOverviewCamera.setTranslateY(m_pOverviewCamera.getTranslateY() + Cb);
        
        m_pOverviewCamera.setScaleX(m_pOverviewCamera.getScaleX() * factor);
        m_pOverviewCamera.setScaleY(m_pOverviewCamera.getScaleY() * factor);
        
    }
    
    public void SetZEnabled(boolean how){
        if(m_bZEnabled == how)return;
        m_bZEnabled = how;
        // No-op
    }
    
    public void SetWidth(double scaleValue, double verticalStretch){
        if(m_pOverviewCamera == null)return;
        m_pOverviewCamera.setScaleX(scaleValue);
        m_pOverviewCamera.setScaleY(scaleValue);
        
    }

    public Camera GetOverviewCamera() {
        return m_pOverviewCamera;
    }

    public Camera GetOverlayCamera() {
        return m_pOverlayCamera;
    }
    
    public void SetWidth(double scaleValue){
        if(m_pOverviewCamera == null)return;
        m_pOverviewCamera.setScaleX(scaleValue);
        m_pOverviewCamera.setScaleY(scaleValue);        
    }
    
    public void Resize(int iClientWidth, int iClientHeight){
        
    }
    
    public boolean isValid(){
        return objValid;
    }
    
    @Override
    public void Release() throws Exception{
        m_pOverviewCamera = null;
        m_pOverlayCamera = null;
        objValid = false;
        super.Release();
    }
}
