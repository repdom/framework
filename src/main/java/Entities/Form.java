package Entities;

public class Form {
    public String nombre;
    public String tipoAtributo;
    public String valortipoAtributo;
    public boolean pkCheckcbox;
    public boolean notNullCheckbox;
    public boolean CheckBoxUnique;
    public String fkTablaRelacionada;
    public String fkRelacion;

    public Form() {
    }

    public Form(String nombre, String tipoAtributo, boolean pkCheckcbox, boolean notNullCheckbox, boolean checkBoxUnique, String fkTablaRelacionada, String fkRelacion) {
        this.nombre = nombre;
        this.tipoAtributo = tipoAtributo;
        this.pkCheckcbox = pkCheckcbox;
        this.notNullCheckbox = notNullCheckbox;
        this.CheckBoxUnique = checkBoxUnique;
        this.fkTablaRelacionada = fkTablaRelacionada;
        this.fkRelacion = fkRelacion;
    }

    public String getFkTablaRelacionada() {
        return fkTablaRelacionada;
    }

    public void setFkTablaRelacionada(String fkTablaRelacionada) {
        this.fkTablaRelacionada = fkTablaRelacionada;
    }

    public String getFkRelacion() {
        return fkRelacion;
    }

    public void setFkRelacion(String fkRelacion) {
        this.fkRelacion = fkRelacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoAtributo() {
        return tipoAtributo;
    }

    public String getValortipoAtributo() {
        return valortipoAtributo;
    }

    public void setValortipoAtributo(String valortipoAtributo) {
        this.valortipoAtributo = valortipoAtributo;
    }

    public void setTipoAtributo(String tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public boolean isPkCheckcbox() {
        return pkCheckcbox;
    }

    public void setPkCheckcbox(boolean pkCheckcbox) {
        this.pkCheckcbox = pkCheckcbox;
    }

    public boolean isNotNullCheckbox() {
        return notNullCheckbox;
    }

    public void setNotNullCheckbox(boolean notNullCheckbox) {
        this.notNullCheckbox = notNullCheckbox;
    }

    public boolean isCheckBoxUnique() {
        return CheckBoxUnique;
    }

    public void setCheckBoxUnique(boolean checkBoxUnique) {
        CheckBoxUnique = checkBoxUnique;
    }
}
