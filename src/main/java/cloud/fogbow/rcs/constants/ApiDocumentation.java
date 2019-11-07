package cloud.fogbow.rcs.constants;

public class ApiDocumentation {
    
    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Resource Catalog Service (RCS) API";
        public static final String API_DESCRIPTION = "Describe this API here."; // TODO add API description
    }
    
    public static class Model {
        public static final String MEMBERS_LIST = "{\n" 
               + "    \"members\": [\n" 
               + "        \"member1\",\n" 
               + "        \"member2\",\n" 
               + "        \"member3\"\n" 
               + "    ]\n" 
               + "}";
        
        public static final String SERVICES_LIST = "{\n"
               + "    \"services\": [\n"
               + "        {\n"
               + "            \"serviceType\": \"LOCAL\",\n"
               + "            \"location\": \"https://member1.org/doc\"\n"
               + "        }\n"
               + "    ]\n"
               + "}";
    }

    public static class Catalog {
        public static final String API = "Provides a service catalog of the federation members";
        public static final String GET_ALL_MEMBERS_OPERATION = "Return a list of members.";
        public static final String GET_ALL_SERVICES_BY_MEMBER_OPERATION = "List a catalog of services available by member.";
        public static final String MEMBER = "The provider member of the federation.";
    }
}
