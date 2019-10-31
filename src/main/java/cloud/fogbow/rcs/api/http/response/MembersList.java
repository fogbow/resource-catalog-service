package cloud.fogbow.rcs.api.http.response;

import java.util.List;

import cloud.fogbow.rcs.core.models.ProviderMember;
import io.swagger.annotations.ApiModelProperty;

public class MembersList {
    
    @ApiModelProperty()
    List<ProviderMember> members;
    
    public MembersList() {}
    
    public MembersList(List<ProviderMember> members) {
        this.members = members;
    }

    public List<ProviderMember> getMembers() {
        return members;
    }

    public void setMembers(List<ProviderMember> members) {
        this.members = members;
    }
}
