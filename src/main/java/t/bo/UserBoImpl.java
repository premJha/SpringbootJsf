package t.bo;


import org.springframework.stereotype.Service;

@Service
public class UserBoImpl implements UserBo{
    @Override
    public String getMessage() {
        return "From BO prem ";
    }
}
