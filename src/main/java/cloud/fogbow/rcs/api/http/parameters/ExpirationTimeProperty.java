package cloud.fogbow.rcs.api.http.parameters;

import cloud.fogbow.rcs.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ExpirationTimeProperty {
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.EXPIRATION_TIME)
    private String value;

    public ExpirationTimeProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
