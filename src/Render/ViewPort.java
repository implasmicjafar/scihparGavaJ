/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Render;

import Common.RefCounted;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;

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
        this.SetWidth(10000.0);               
        objValid = true;
    }
    
    public void ScaleAroundLocalPoint(double xLocal, double yLocal, double factor){
        
    }
    
    public void SetZEnabled(boolean how){
        if(m_bZEnabled == how)return;
        m_bZEnabled = how;
        if(how){
            m_pOverviewCamera = new PerspectiveCamera();
            this.SetWidth(10000.0);      
        }else{
            m_pOverviewCamera = new ParallelCamera();  
            this.SetWidth(10000.0);
        }
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
