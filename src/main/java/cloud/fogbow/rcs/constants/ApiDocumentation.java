package cloud.fogbow.rcs.constants;

public class ApiDocumentation {
    
    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Resource Catalog Service (RCS) API";
        public static final String API_DESCRIPTION = "Describe this API here.";
    }
    
    public static class Model {
        public static final String MEMBERS = "{\n" + 
                "  \"members\": [\n" + 
                "    {\n" + 
                "      \"member\": \"member1\",\n" + 
                "      \"local\": true\n" + 
                "    },\n" + 
                "    {\n" + 
                "      \"member\": \"member2\",\n" + 
                "      \"local\": false\n" + 
                "    },\n" + 
                "    {\n" + 
                "      \"member\": \"member3\",\n" + 
                "      \"local\": false\n" + 
                "    }\n" + 
                "  ]\n" + 
                "}";
    }

    public static class Catalog {
        public static final String API = "Queries the members currently belonging to the federation";
        public static final String GET_OPERATION = "Returns a catalog of services available";
    }
}
