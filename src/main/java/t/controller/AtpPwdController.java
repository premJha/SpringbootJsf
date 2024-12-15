package t.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Controller;
import utils.env.Environment;

import java.io.IOException;
import java.util.List;

@Controller
public class AtpPwdController {
    private List<String> environments;

    private String selectedEnvironment;
    private String tenant;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getSelectedEnvironment() {
        return selectedEnvironment;
    }

    public void getAtpPwd() {
        System.out.println(tenant);
        System.out.println(selectedEnvironment);
    }

    public void setSelectedEnvironment(String selectedEnvironment) {
        this.selectedEnvironment = selectedEnvironment;
    }

    public List<String> getEnvironments() {
        try {
            environments=Environment.getAllEnvironments();
            System.out.println("");
        } catch (Exception e) {
            PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("ERROR:"+e.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("ERROR:"+e.getMessage()));
//                FacesContext.getCurrentInstance().getExternalContext().dispatch("home.faces");
//            throw new RuntimeException(e);

        }
        return environments;
    }

    public void setEnvironments(List<String> environments) {
        this.environments = environments;
    }
}
