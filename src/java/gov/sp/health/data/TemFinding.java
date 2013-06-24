/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.data;

/**
 *
 * @author buddhika
 */
public class TemFinding {
    String findingName;
    Double findingValue;

    public TemFinding(String findingName, Double findingValue) {
        this.findingName = findingName;
        this.findingValue = findingValue;
    }

    public TemFinding(String findingName) {
        this.findingName = findingName;
    }

    public TemFinding(Double findingValue) {
        this.findingValue = findingValue;
    }

    
    
    public String getFindingName() {
        return findingName;
    }

    public void setFindingName(String findingName) {
        this.findingName = findingName;
    }

    public Double getFindingValue() {
        return findingValue;
    }

    public void setFindingValue(Double findingValue) {
        this.findingValue = findingValue;
    }
    
    
}
