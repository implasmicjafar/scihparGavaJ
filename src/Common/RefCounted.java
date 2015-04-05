/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

/**
 *
 * @author T3ee
 */
public abstract class RefCounted {
    private int m_iRefCount = 0;
    
    public void Release() throws Exception{
        if(m_iRefCount > 0){
            throw new Exception("Release() prematurely called on RefCounted. Only unref should be used");
        }
        // No OP
    }
    
    public void Ref(){
        m_iRefCount++;
    }
    
    public void UnRef() throws Exception{
        if(m_iRefCount < 1){
            throw new Exception("Ref counting problem - unref called on a freed or non refed class");
        }
        m_iRefCount--;
        if(m_iRefCount == 0){
            Release();
        }        
    }
    
}
