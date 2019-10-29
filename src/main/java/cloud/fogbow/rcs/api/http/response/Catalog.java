package cloud.fogbow.rcs.api.http.response;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class Catalog {

    @ApiModelProperty(example = "[\"member1\" ,\"member2\" ,\"member3\"]")
    private List<String> members;

    public Catalog(List<String> members) {
        this.members = members;
    }

    public Catalog() {
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}