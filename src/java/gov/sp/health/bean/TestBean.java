/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import java.io.Serializable;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@RequestScoped
public class TestBean  implements Serializable {

    /**
     * Creates a new instance of TestBean
     */
    public TestBean() {
    }
    
    
    
    public String testApp(){
         JsfUtil.addSuccessMessage("Setted current");
         return "areas";
    }
    
}
