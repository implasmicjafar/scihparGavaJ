/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Render;

import Common.RefCounted;
import Controller.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Jafaru Mohammed
 */
public class RenderSurface extends RefCounted{
    
    private Controller m_pController = null;
    private SubScene m_pScene = null;
    private SubScene m_pOverlayScene = null;
    private boolean m_bUseShader = false;
    private boolean m_bZEnabled = false;
    private ViewPort m_pViewPort = null;
    
    private double m_dClientWidth = 0;
    private double m_dClientHeight = 0;
    
    private Scene m_pRootScene = null;
    private Group m_pRoot = null;
    private Group m_pSceneRoot = null;
    private Group m_pOverlaySceneRoot = null;
    private Rectangle m_pSceneClipShape = null;
    private Rectangle m_pOverlaySceneClipShape = null;
    
    public RenderSurface(double dClientWidth, double dClientHeight){
        super();
        
        m_dClientWidth = dClientWidth;
        m_dClientHeight = dClientHeight;
        
        m_pViewPort = new ViewPort();
        m_pViewPort.Ref();
        m_pController = null;
        
        m_pRootScene = CreateScene();
        
        m_pSceneRoot = new Group();
        m_pOverlaySceneRoot = new Group();
        
        m_pScene = new SubScene(m_pSceneRoot, m_pRootScene.getWidth(), m_pRootScene.getHeight());
        m_pOverlayScene = new SubScene(m_pOverlaySceneRoot, m_pRootScene.getWidth(), m_pRootScene.getHeight());
        
        AddDrawLayers(m_pSceneRoot);
        AddDrawLayers(m_pOverlaySceneRoot);
        
        m_bZEnabled = false;
        m_pViewPort.SetZEnabled(m_bZEnabled);
        
        m_pScene.setCamera(m_pViewPort.GetOverviewCamera());
        m_pOverlayScene.setCamera((m_pViewPort.GetOverlayCamera()));
        
        m_pSceneRoot.getChildren().add(m_pViewPort.GetOverviewCamera());
        m_pOverlaySceneRoot.getChildren().add(m_pViewPort.GetOverlayCamera());        
        
        m_pSceneClipShape = new Rectangle(0,0,dClientWidth,dClientHeight);
        m_pOverlaySceneClipShape = new Rectangle(0,0,dClientWidth,dClientHeight);
        
        m_pSceneClipShape.setFill(Color.TRANSPARENT);
        m_pOverlaySceneClipShape.setFill(Color.TRANSPARENT);
        m_pSceneClipShape.setStroke(Color.GREY);
        m_pOverlaySceneClipShape.setStroke(Color.GREY);
        m_pSceneClipShape.setVisible(false);
        m_pOverlaySceneClipShape.setVisible(false);
                               
        m_pOverlaySceneRoot.setClip(GetClip(dClientWidth, dClientHeight));
        m_pSceneRoot.setClip(GetClip(dClientWidth, dClientHeight)); 
        
        m_pSceneRoot.getChildren().add(m_pSceneClipShape);
        m_pOverlaySceneRoot.getChildren().add(m_pOverlaySceneClipShape);
        
        m_pRoot.getChildren().add(m_pOverlaySceneRoot);
        m_pRoot.getChildren().add(m_pSceneRoot);
        
    }

    public Controller GetController() {
        return m_pController;
    }

    public SubScene GetScene() {
        return m_pScene;
    }

    public SubScene GetOverlayScene() {
        return m_pOverlayScene;
    }

    public ViewPort GetViewPort() {
        return m_pViewPort;
    }
    
    
    
    private Shape GetClip(double width, double height){
        Rectangle mighty = new Rectangle(-10000, -10000, 10000, 10000); // very large rectangle
        Rectangle viewable = new Rectangle(0,0,width, height);
        return Path.subtract(viewable,mighty);
    }

    public Scene GetRootScene() {
        return m_pRootScene;
    }
    
    public void SetSceneClipRender(boolean how){
        if(m_pSceneClipShape == null)return;
        m_pSceneClipShape.setVisible(how);
    }
    
    public void SetOverlayScenClipRender(boolean how){
        if(m_pOverlaySceneClipShape == null)return;
        m_pOverlaySceneClipShape.setVisible(how);
    }
    
    public void DrawScene(boolean how){
        if(m_pScene == null)return;
        m_pScene.setVisible(how);
    }
    
    public void DrawOverlayScene(boolean how){
        if(m_pOverlayScene == null)return;
        m_pOverlayScene.setVisible(how);
    }
    
    private Scene CreateScene(){
        m_pRoot  =  new  Group();
        return (new  Scene(m_pRoot, Color.BLACK));        
    }
    
    public void SetZEnabled(boolean how){
        if(m_pViewPort == null)return;
        if(m_bZEnabled = how)return;
        m_bZEnabled = how;
        m_pViewPort.SetZEnabled(how);
    }
    
    public void SetController(Controller controller){
        if(controller != null)controller.Ref();
        if(m_pController != null){
            try {
                m_pController.UnRef();
            } catch (Exception ex) {
                Logger.getLogger(RenderSurface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        m_pController = controller;
    }
    
    private void AddDrawLayers(Group sceneRoot){
        if(sceneRoot == null)return;
        for(int i = 0; i < 16; i++){
            Group layer = new Group();
            layer.setVisible(false);
            sceneRoot.getChildren().add(layer);
        }
    }
    
    public void Resize(double dClientWidth, double dClientHeight){
        if((m_dClientWidth == dClientWidth) && (m_dClientHeight == dClientHeight)){
            return;
        }
        m_dClientWidth = dClientWidth;
        m_dClientHeight = dClientHeight;
        
        m_pOverlaySceneRoot.setClip(GetClip(dClientWidth, dClientHeight));
        m_pSceneRoot.setClip(GetClip(dClientWidth, dClientHeight)); 
               
        m_pSceneClipShape.setHeight(dClientHeight);
        m_pOverlaySceneClipShape.setHeight(dClientHeight);
        
        m_pSceneClipShape.setWidth(dClientWidth);
        m_pOverlaySceneClipShape.setWidth(dClientWidth);
    }
    
     /**
     * Method to add a drawable to the root group on a specific layer.
     * Returns the index of the drawable on the layer
     * @param drawable
     * @param layerIndex
     * @return 
     */
    public int AddDrawable(Drawable drawable, int layerIndex){
        if((layerIndex < 0) || (layerIndex > 15)) return -1;
        int preAddSize = ((Group)m_pSceneRoot.getChildren().get(layerIndex)).getChildren().size();
        ((Group)m_pSceneRoot.getChildren().get(layerIndex)).getChildren().add(drawable);
        return preAddSize;
    }
    
    /**
     * Method to add a drawable to the root group on a specific layer.
     * Returns the index of the drawable on the layer
     * @param drawable
     * @param layerIndex
     * @return 
     */
    public int AddDrawable(Node drawable, int layerIndex){
        if((layerIndex < 0) || (layerIndex > 15)) return -1;
        int preAddSize = ((Group)m_pSceneRoot.getChildren().get(layerIndex)).getChildren().size();
        ((Group)m_pSceneRoot.getChildren().get(layerIndex)).getChildren().add(drawable);
        return preAddSize;
    }
    
    /**
     * Method to add a drawable to the root group on a specific layer.
     * Returns the index of the drawable on the layer
     * @param drawable
     * @param layerIndex
     * @return 
     */
    public int AddOverlayDrawable(Node drawable, int layerIndex){
        if((layerIndex < 0) || (layerIndex > 15)) return -1;
        int preAddSize = ((Group)m_pOverlaySceneRoot.getChildren().get(layerIndex)).getChildren().size();
        ((Group)m_pOverlaySceneRoot.getChildren().get(layerIndex)).getChildren().add(drawable);
        return preAddSize;
    }
    
     /**
     * Access the root Node of a layer which is the layer group.
     * @param layerIndex
     * @return 
     */
    public Group GetOverviewLayerGroup(int layerIndex){
        if((layerIndex < 0) || (layerIndex > 15)) return null;
        return (Group)(m_pSceneRoot.getChildren().get(layerIndex));
    }
    
    /**
     * Access the root Node of a layer which is the layer group.
     * @param layerIndex
     * @return 
     */
    public Group GetOverlayLayerGroup(int layerIndex){
        if((layerIndex < 0) || (layerIndex > 15)) return null;
        return (Group)(m_pOverlaySceneRoot.getChildren().get(layerIndex));
    }
    
    /**
     * Set the visibility of a layer mask
     * @param layerIndex
     * @param status 
     */
    public void SetOverviewLayerMask(int layerIndex, boolean status ){
        if((layerIndex < 0) || (layerIndex > 15)) return;
        ((Group)m_pSceneRoot.getChildren().get(layerIndex)).setVisible(status);
    }
    
    /**]
     * Set the visibility of a layer mask
     * @param layerIndex
     * @param status 
     */
    public void SetOverlayLayerMask(int layerIndex, boolean status ){
        if((layerIndex < 0) || (layerIndex > 15)) return;
        ((Group)m_pOverlaySceneRoot.getChildren().get(layerIndex)).setVisible(status);
    }
    
    public void SetSceneBackgroundColor(Color color){
        if (m_pRootScene == null) return;
        m_pRootScene.setFill(color);
    }
    
    
    @Override
    public void Release() throws Exception{
        
        m_dClientWidth = 0;
        m_dClientHeight = 0;
        
        if(m_pViewPort != null){
            m_pViewPort.UnRef();
            m_pViewPort = null;
        }
        
        if(m_pRoot != null){
            m_pRoot.getChildren().clear();
        }
        if(m_pSceneRoot != null){
            m_pSceneRoot.getChildren().clear();
        }
        if(m_pOverlaySceneRoot != null){
            m_pOverlaySceneRoot.getChildren().clear();
        }
        
        m_pScene = null;
        m_pOverlayScene = null;
        m_pRootScene = null;
        m_pOverlaySceneRoot = null;
        m_pSceneRoot = null;
        m_pRoot = null;
        m_pSceneClipShape = null;
        m_pOverlaySceneClipShape = null;
        
        if(m_pController != null){
            m_pController.UnRef();
            m_pController = null;
        }
        
        super.Release();
    }
}
