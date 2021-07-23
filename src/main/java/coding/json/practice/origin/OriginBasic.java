package coding.json.practice.origin;

import coding.json.practice.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class OriginBasic {
    private static final ObjectMapper mapper = new ObjectMapper();

    public void basic() {
        String string = "{ \"id\" : 1, \"name\" : \"정혜우\" }";
        string = "[{\"id\":1,\"name\":\"GET method test ...\",\"details\":null},{\"id\":2,\"name\":\"Second Data in List Test\",\"details\":null}]";

        // 리스트가 Null 값일 때 허용하는 ?
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try{
            List<UserVO> result = mapper.readValue(string, new TypeReference<List<UserVO>>(){});
            result = mapper.readValue(string, mapper.getTypeFactory().constructCollectionType(List.class, UserVO.class));
            result = Arrays.asList(mapper.readValue(string, UserVO[].class));

            // string(json) > object
            result.forEach(r->log.info(this.getClass() + " string(json) > object list : " + r.getName()));

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            // object > json
            log.info(this.getClass() + " object > json " + json);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    // Gson은 String >> fromJson과 toJson으로

    // 역직렬화
    // JsonElement = JsonParser.parse(String)
    // JsonElement.getAsJsonObject().get("key").getAsInt()

    // 직직렬화
    // Gson().toJson(this);

    public void basicGetList() {
        String string =
            "{ " +
                "\"id\" : \"1\", " +
                "\"name\" : \"정혜우\", " +
                " \"details\" : [ \r\n"  +

                    "{\r\n" +

                        "\"alias\" : \"새우\" " +

                    "}, " +
                    "{\r\n" +

                        "\"alias\" : \"호떡\"" +

                    "}\r\n" +

                "] \r\n" +
            "}";

        try{
            // object
            UserVO user = mapper.readValue(string, UserVO.class);
            log.info(this.getClass() + " Object relation1 : " + user.getFriends().get(0).getRelation());

            // JsonNode
            JsonNode node = mapper.readValue(string, JsonNode.class);
            JsonNode details = node.get("details").get(0);
            String alias = details.get("alias").asText();
            log.info(this.getClass() + " JsonNode alias1 : " + alias);


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void basicPutList() throws JsonProcessingException {
        try {
            ObjectNode user1 = mapper.createObjectNode();
            user1.put("id", 1);
            user1.put("name", "basicPutList");

            ArrayNode array1 = mapper.createArrayNode();
            array1.add("새우");
            array1.add("호떡");
            user1.set("details", array1);

            ArrayNode arrayNode = mapper.createArrayNode();
            arrayNode.addAll(Arrays.asList(user1));

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
            log.info(this.getClass() + " prettyWrite : " + json);

            log.info(this.getClass() + " defaultWrite : " + mapper.writeValueAsString(arrayNode));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
