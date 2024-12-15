package t;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import t.bo.UserBo;

import java.io.Serializable;

@Component
@Scope("session")
public class UserBean implements Serializable {

    @Autowired
    UserBo userBo;

    public void setUserBo(UserBo userBo) {
        this.userBo = userBo;
    }

    public String printMsgFromSpring() {
        return userBo.getMessage();
    }

}
