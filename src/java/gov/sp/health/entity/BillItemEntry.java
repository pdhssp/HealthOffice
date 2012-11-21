/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

/**
 *
 * @author Buddhika
 */
public class BillItemEntry {

    public BillItemEntry() {
    }
    
    private Long id;
    BillItem billItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public BillItem getBillItem() {
        if (billItem==null){
            billItem = new BillItem();
        }
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    @Override
    public String toString() {
        return "BillItemEntry{" + "id=" + id + ", item=" + billItem + '}';
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BillItemEntry)) {
            return false;
        }
        BillItemEntry other = (BillItemEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
